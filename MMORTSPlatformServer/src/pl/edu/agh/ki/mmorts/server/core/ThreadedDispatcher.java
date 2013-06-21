package pl.edu.agh.ki.mmorts.server.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.communication.MessageInputChannel;
import pl.edu.agh.ki.mmorts.server.communication.MessageReceiver;
import pl.edu.agh.ki.mmorts.server.communication.Response;
import pl.edu.agh.ki.mmorts.server.communication.TargetNotExistsException;
import pl.edu.agh.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.Continuation;
import pl.edu.agh.ki.mmorts.server.modules.Module;
import pl.edu.agh.ki.mmorts.server.modules.util.ContAdapter;

import com.google.inject.name.Named;

/**
 * Default implementation of a message dispatcher.
 * 
 * @author los
 */
public class ThreadedDispatcher extends AbstractModuleContainer implements
        MessageReceiver, Dispatcher {

    private static final Logger logger = Logger
            .getLogger(ThreadedDispatcher.class);

    /** Configuration object */
    @Inject
    private Config config;

    /** Message service */
    @Inject
    private MessageInputChannel channel;

    /** Transaction manager */
    @Inject
    private TransactionManager tm;

    @Inject
    @Named("sv.dispatcher.threads.init")
    private int threadsInit;

    @Inject
    @Named("sv.dispatcher.threads.max")
    private int threadsMax;

    @Inject
    @Named("sv.dispatcher.threads.keepalive")
    private int keepalive;

    /** Thread pool used to handle incoming messages */
    private ExecutorService threadPool;

    /**
     * Thread-local transaction executor
     */
    private static final ThreadLocal<TransactionExecutor> executor = new ThreadLocal<TransactionExecutor>() {
        @Override
        protected TransactionExecutor initialValue() {
            return new TransactionExecutor();
        }
    };

    /**
     * Creates a {@code Continuation} encapsulating an operation of message
     * delivery.
     * 
     * @param message
     *            Message to deliver
     * @return {@code Continuation} wrapping an action of delivering the message
     */
    private Continuation deliveryAction(final Message message) {
        return new ContAdapter() {
            @Override
            public void execute(Context context) {
                deliver(message);
            }
        };
    }

    /**
     * Validates message's destination. If the message is labeled as unicast, it
     * checks if there is a module associated with {@code message.target}
     * unicast address. Else it does nothing, as the empty multicast group is
     * considered a valid target.
     * 
     * @param message
     *            Message to validate target address of
     * @return {@code true} if the address has not been found invalid
     */
    private boolean targetExistsLocally(Message message) {
        String addr = message.target;
        if (message.isUnicast()) {
            return unicast().containsKey(addr);
        } else {
            // Don't validate multicast
            return true;
        }
    }

    /**
     * @return Current transaction executor
     */
    private TransactionExecutor executor() {
        return executor.get();
    }

    /**
     * Creates the thread pool and initializes the communication
     */
    @OnInit
    void init() {
        logger.debug("Initializing");
        createThreadPool();
        channel.startReceiving(this);
    }

    /**
     * Initializes the thread pool used to dispatch messages
     */
    private void createThreadPool() {
        String msg = String.format(
                "Initializing the pool (init=%d, max=%d, keepalive=%d s)",
                threadsInit, threadsMax, keepalive);
        logger.debug(msg);
        threadPool = new ThreadPoolExecutor(threadsInit, threadsMax, keepalive,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        // Executors.newFixedThreadPool(10);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receive(final Message message, final Response response) {
        logger.debug("Message received: \n" + message);
        // async execute
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                dispatchMessage(message, response);
            }
        });
        logger.debug("Message submitted to the thread pool");
    }

    /**
     * Dispatches the message and performs the full message processing
     * transaction.
     * 
     * @param message
     *            Message to process
     * @param response
     *            Response object
     */
    private void dispatchMessage(Message message, Response response) {
        logger.debug("Begin message transaction");
        // begin message transaction
        tm.begin();
        try {
            // lock the module list
            readLock.lock();
            doSend(message);
            // realize transaction
            executor().run();
            try {
                logger.debug("Transaction commited, executing listeners");
                tm.commit();
                runPostCommitStack();
                response.send(executor().responses());
            } catch (Exception e) {
                logger.error("Exception inside a commit handler", e);
                response.failed(e);
            }
        } catch (Exception e) {
            logger.debug("Transaction rolled back due to exception", e);
            tm.rollback();
            response.send(executor().responses());
        } finally {
            // After commit/rollback reset the executor
            executor().clear();
            // unlock the module list
            readLock.unlock();
        }
    }

    /**
     * Runs the "rest", sequence of messages and events generated by the
     * transaction listeners, after the transaction has finished
     * 
     * @throws Exception
     *             If there was an exception during this phase of communication
     */
    private void runPostCommitStack() throws Exception {
        try {
            for (Message message : executor().notifications()) {
                try {
                    deliver(message);
                } catch (TargetNotExistsException e) {
                    logger.error("Notification for nonexistant module", e);
                } catch (Exception e) {
                    logger.error("Exception while processing post-commit "
                            + "notifications", e);
                    throw e;
                }
            }
            executor().runStack();
        } catch (Exception e) {
            logger.error("Exception during the post-commit phase", e);
            throw e;
        }
    }

    /**
     * Shutdown callback, notifies modules.
     */
    @OnShutdown
    public void shutdown() {
        logger.debug("Shutting down the dispatcher");
        try {
            terminateTheadPool();
            shutdownModules();
            logger.debug("Dispatcher shat down");
        } catch (Exception e) {
            logger.error("Error while shutting down the communication channel");
            throw new RuntimeException(e);
        }
    }

    /**
     * Orders thread pool shutdown and waits for the messages being processed.
     */
    private void terminateTheadPool() {
        logger.debug("Waiting for pending messages to finish");
        threadPool.shutdown();
        try {
            // Arbitrary value, seems to be of no particular importance
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn("Interrupted while awaiting thread pool termination");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Message message) {
        // Only during the transaction
        if (executor().noTransaction()) {
            throw new IllegalStateException("send() called outside "
                    + "the transaction");
        }
        doSend(message);
    }

    /**
     * Bypasses state checing, used to push first message to initiate the
     * execution stack.
     * */
    private void doSend(Message message) {
        if (!targetExistsLocally(message)) {
            throw new TargetNotExistsException();
        } else {
            executor().push(deliveryAction(message));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void output(Message message) {
        // TODO: some validation, maybe?
        executor().addResponse(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendDelayed(final Message message) {
        // Only during the transaction
        if (!executor().inTransaction()) {
            throw new IllegalStateException("sendDelayed called outside "
                    + "the transaction (" + executor().getState() + ")");
        }
        if (!targetExistsLocally(message)) {
            throw new TargetNotExistsException(message.target);
        }
        // execute actual message dispatching after the transaction
        executor().addNotification(message);
    }

    /**
     * Delivers the message to the appropriate target module, and executes its'
     * {@linkplain Module#receive} in the current thread.
     */
    private void deliver(Message message) {
        final String addr = message.target;
        if (message.isUnicast()) {
            Module module = unicast().get(addr);
            if (module != null) {
                module.receive(message, executor().getContext());
            } else {
                throw new TargetNotExistsException(addr);
            }
        } else {
            // Multicast
            Iterable<Module> interested = multicast().get(addr);
            if (interested != null) {
                for (Module module : interested) {
                    module.receive(message, executor().getContext());
                }
            } else {
                logger.warn("Message to nonexistant multicast group [" + addr
                        + "]");
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void later(Continuation cont) {
        if (executor().noTransaction()) {
            throw new IllegalStateException("later() called outside the "
                    + "transaction (" + executor().getState() + ")");
        }
        executor().push(cont);
    }

}
