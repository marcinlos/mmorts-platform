package pl.edu.agh.ki.mmorts.server.core;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.common.message.Address;
import pl.edu.agh.ki.mmorts.common.message.Destination;
import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.common.message.Mode;
import pl.edu.agh.ki.mmorts.server.communication.MessageChannel;
import pl.edu.agh.ki.mmorts.server.communication.MessageReceiver;
import pl.edu.agh.ki.mmorts.server.communication.Response;
import pl.edu.agh.ki.mmorts.server.communication.ServiceLocator;
import pl.edu.agh.ki.mmorts.server.communication.ServiceLocatorDelgate;
import pl.edu.agh.ki.mmorts.server.communication.TargetNotExistsException;
import pl.edu.agh.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionListener;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.Continuation;
import pl.edu.agh.ki.mmorts.server.modules.Module;

import com.google.inject.name.Named;

/**
 * Default implementation of a message dispatcher.
 */
public class ThreadedDispatcher extends ModuleContainer implements
        MessageReceiver {

    private static final Logger logger = Logger
            .getLogger(ThreadedDispatcher.class);

    /** Configuration object */
    @Inject
    private Config config;

    /** Message service */
    @Inject
    private MessageChannel channel;

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

    /** Implementation of service locator */
    private ServiceLocator services = new ServiceLocatorDelgate();

    /**
     * Enum describing possible thread states.
     */
    private enum State {

        /** No transaction is currently active */
        NO_TRANS,

        /** There is an active transaction */
        IN_TRANS,

        /** Transaction commit/rollback listeners are executing */
        POST_TRANS
    }

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
     * Helper class containing per-thread state necessary for conducting
     * transactions.
     */
    private static class TransactionExecutor {

        private static final Logger logger = Logger
                .getLogger(TransactionExecutor.class);

        /** State of the transaction */
        private State state = State.NO_TRANS;

        /** Transaction stack */
        private Deque<Continuation> executionStack = new ArrayDeque<Continuation>();

        /** True if rollback handlers are being executed */
        private boolean rollback = false;

        /** Current transaction context */
        private Context context;
        
        /** Collection of local notifications */
        private List<Message> notifications = new ArrayList<Message>();
        
        /** Colletion of withheld response messages */
        private List<Message> withheld = new ArrayList<Message>();
        
        /** Run the transaction */
        public void run() throws Exception {
            state = State.IN_TRANS;
            // context per transaction
            context = new Context();
            try {
                while (!executionStack.isEmpty()) {
                    Continuation cont = executionStack.pop();
                    cont.execute(context);
                }
            } catch (Exception e) {
                // Remove all the stored messages
                clearMessageStore();
                rollbackAll(e);
                throw e;
            } finally {
                state = State.POST_TRANS;
            }
        }
        
        /** Removes all the stored messages (local and responses) */
        public void clearMessageStore() {
            notifications.clear();
            withheld.clear();
        }

        /** Definitely finish the current transaction, clear state */
        public void clear() {
            state = State.NO_TRANS;
            context = null;
        }

        /**
         * @return Current transaction context
         */
        public Context getContext() {
            return context;
        }

        /**
         * @return {@code true} if the rollback is in progress
         */
        public boolean duringRollback() {
            return rollback;
        }

        /**
         * Informs all the remaining continuations about the failure.
         * 
         * @param exc
         *            Exception that caused the failure
         */
        private void rollbackAll(Throwable exc) {
            rollback = true;
            while (!executionStack.isEmpty()) {
                Continuation cont = executionStack.pop();
                try {
                    cont.failure(exc, context);
                } catch (Exception e) {
                    logger.error("Exception in a failure handler", e);
                }
            }
            rollback = false;
        }

        /**
         * Pushes a continuation on the execution stack
         * 
         * @param cont
         *            Continuation to push
         */
        public void push(Continuation cont) {
            if (!rollback) {
                executionStack.push(cont);
            } else {
                throw new IllegalStateException(
                        "Cannot push continuation during rollback");
            }
        }
    }

    private class DeliverMessage implements Continuation {

        private Message message;

        public DeliverMessage(Message message) {
            this.message = message;
        }

        @Override
        public void execute(Context context) {

        }

        @Override
        public void failure(Throwable e, Context context) {

        }

    }

    private boolean targetExistsLocally(Message message) {
        String addr = message.getAddress().internal;
        if (message.getMode() == Mode.UNICAST) {
            return unicast.containsKey(addr);
        } else {
            // Don't validate multicast
            return true;
        }
    }

    /**
     * @return Current transaction state
     */
    private State getState() {
        return executor.get().state;
    }

    /**
     * @return {@code true} if a transaction is currently being executed
     */
    private boolean inTransaction() {
        return getState() == State.IN_TRANS;
    }

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
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receive(Message message, Response response) {
        String details = messageDetails(message);
        logger.debug("Message received: \n" + details);
        // async execute
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                // begin message transaction
                tm.begin();
                try {
                    // realize transaction
                    executor.get().run();
                    tm.commit();
                } catch (Exception e) {
                    logger.debug("Transaction rolled back due to exception", e);
                    tm.rollback();
                } finally {
                    // After commit/rollback reset the executor
                    executor.get().clear();
                }
            }
        });
        logger.debug("Message submitted to the thread pool");
    }

    /**
     * Produces a stringized message representation
     */
    private static String messageDetails(Message message) {
        StringBuilder sb = new StringBuilder();
        sb.append("\tTarget: " + message.getAddress()).append('\n')
                .append("\tSource: " + message.getSource()).append('\n');
        return sb.toString();
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
        if (!inTransaction()) {
            throw new IllegalStateException("send() called outside "
                    + "the transaction");
        }
        // if local address check if target exists
        if (message.getAddress().type() == Destination.LOCAL) {
            if (!targetExistsLocally(message)) {
                throw new TargetNotExistsException();
            } else {
                
            }
        } else {
            throw new IllegalArgumentException("Remote message in send()");
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendResponse(Message message) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendDelayed(final Message message) {
        // Only during the transaction
        if (!inTransaction()) {
            throw new IllegalStateException("sendDelayed called outside "
                    + "the transaction (" + getState() + ")");
        }
        // if local address check if target exists
        if (message.getAddress().type() == Destination.LOCAL) {
            if (!targetExistsLocally(message)) {
                throw new TargetNotExistsException();
            }
        }
        // execute actual message dispatching after the transaction
        // TODO: save it somewhere and execute in one transaction listener,
        // maybe?
        tm.getCurrent().addListener(new TransactionListener() {
            @Override
            public void rollback() {
                // empty
            }

            @Override
            public void commit() {
                try {
                    dispatchInThisThread(message);
                } catch (Exception e) {
                    logger.error("Error during message dispatching", e);
                }
            }
        });
    }

    /**
     * Dispatches the message in the current thread, all the way down to calling
     * {@link Module#receive}.
     */
    private void dispatchInThisThread(Message message) {
        Address addr = message.getAddress();
        if (message.getMode() == Mode.UNICAST) {
            Module module = modules.get(addr.internal).module;
            module.receive(message, executor.get().getContext());
        } else {
            // Multicast
            Iterable<Module> interested = multicast.get(addr);
            for (Module module : interested) {
                module.receive(message, executor.get().getContext());
            }
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void later(Continuation cont) {
        if (getState() != State.IN_TRANS) {
            throw new IllegalStateException("later() called outside the "
                    + "transaction (" + getState() + ")");
        }
        executor.get().push(cont);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Delegates to the {@link ServiceLocatorDelgate}
     */
    @Override
    public <T> void register(Class<? super T> service, T provider) {
        logger.debug("Registering " + provider.getClass() + " as " + service);
        services.register(service, provider);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Delegates to the {@link ServiceLocatorDelgate}
     */
    @Override
    public <T> void registerIfAbsent(Class<? super T> service, T provider) {
        logger.debug("Registering " + provider.getClass() + " as " + service);
        services.registerIfAbsent(service, provider);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Delegates to the {@link ServiceLocatorDelgate}
     */
    @Override
    public <T> T lookup(Class<T> service) {
        return services.lookup(service);
    }

}
