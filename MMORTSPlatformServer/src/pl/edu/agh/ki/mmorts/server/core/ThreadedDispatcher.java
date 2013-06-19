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

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.common.message.Mode;
import pl.edu.agh.ki.mmorts.server.communication.MessageInputChannel;
import pl.edu.agh.ki.mmorts.server.communication.MessageReceiver;
import pl.edu.agh.ki.mmorts.server.communication.Response;
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
 * 
 * @author los
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
     * Helper class containing per-thread state necessary for conducting
     * transactions.
     */
    private static class TransactionExecutor {

        private static final Logger logger = Logger
                .getLogger(TransactionExecutor.class);
        
        /**
         * Enum describing possible thread states.
         */
        public enum State {

            /** No transaction is currently active */
            NO_TRANS,

            /** There is an active transaction */
            IN_TRANS,

            /** Transaction commit/rollback listeners are executing */
            POST_TRANS
        }

        /** State of the transaction */
        private State state = State.NO_TRANS;

        /** Transaction stack */
        private final Deque<Continuation> executionStack;

        /** True if rollback handlers are being executed */
        private boolean rollback = false;

        /** Current transaction context */
        private Context context;

        /** Collection of local notifications */
        private final List<Message> notifications;

        /** Colletion of withheld response messages */
        private final List<Message> withheld;
        
        public TransactionExecutor() {
            executionStack = new ArrayDeque<Continuation>();
            notifications = new ArrayList<Message>();
            withheld = new ArrayList<Message>();
        }

        /** Run the transaction */
        public void run() throws Exception {
            state = State.IN_TRANS;
            // context per transaction
            context = new Context();
            try {
                runStack();
            } finally {
                state = State.POST_TRANS;
            }
        }

        /** Runs the execution stack, does not initate a transaction */
        public void runStack() throws Exception {
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
            }
        }

        /** Removes all the stored messages (local and responses) */
        public void clearMessageStore() {
            notifications.clear();
            withheld.clear();
        }

        /** Definitely finish the current transaction, clear state */
        public void clear() {
            clearMessageStore();
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
            dispatchInThisThread(message);
        }

        @Override
        public void failure(Throwable e, Context context) {
            // empty
        }

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

    private TransactionExecutor executor() {
        return executor.get();
    }

    /**
     * @return Current transaction state
     */
    private TransactionExecutor.State getState() {
        return executor().state;
    }

    /**
     * @return {@code true} if a transaction is currently being executed
     */
    private boolean inTransaction() {
        return getState() == TransactionExecutor.State.IN_TRANS;
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
                logger.debug("Begin message transaction");
                // begin message transaction
                tm.begin();
                try {
                    doSend(message);
                    // realize transaction
                    executor.get().run();
                    try {
                        logger.debug("Transaction commited, executing listeners");
                        tm.commit();
                        runPostCommitStack();
                    } catch (Exception e) {
                        logger.error("Exception inside a commit handler", e);
                        response.failed(e);
                    }
                } catch (Exception e) {
                    logger.debug("Transaction rolled back due to exception", e);
                    tm.rollback();
                } finally {
                    // finally send client messages
                    response.send(executor().withheld);
                    // After commit/rollback reset the executor
                    executor().clear();
                }
            }
        });
        logger.debug("Message submitted to the thread pool");
    }

    /**
     * Runs the "rest", sequence of messages and events generated by the
     * transaction listeners, after the transaction has finished
     */
    private void runPostCommitStack() {
        try {
            for (Message message : executor().notifications) {
                try {
                    dispatchInThisThread(message);
                } catch (Exception e) {
                    logger.error("Exception while processing post-commit "
                            + "notifications", e);
                }
            }
            executor().runStack();
        } catch (Exception e) {
            logger.error("Exception during the post-commit phase", e);
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
        if (!inTransaction()) {
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
            executor.get().push(new DeliverMessage(message));
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void output(Message message) {
        // TODO: some validation, maybe?
        executor().withheld.add(message);
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
        if (!targetExistsLocally(message)) {
            throw new TargetNotExistsException();
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
        final String addr = message.target;
        if (message.mode == Mode.UNICAST) {
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
        if (inTransaction()) {
            throw new IllegalStateException("later() called outside the "
                    + "transaction (" + getState() + ")");
        }
        executor().push(cont);
    }

}
