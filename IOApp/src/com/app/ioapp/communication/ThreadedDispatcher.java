package com.app.ioapp.communication;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.common.message.Mode;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionListener;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import android.annotation.SuppressLint;

import com.app.ioapp.config.Config;
import com.app.ioapp.modules.CommunicatingModule;
import com.app.ioapp.modules.Context;
import com.app.ioapp.modules.Continuation;
import com.app.ioapp.modules.Module;

/**
 * Default implementation of a message dispatcher.
 */
public class ThreadedDispatcher implements MessageReceiver, Dispatcher {

    /** Configuration object */
    private Config config;

    /** Message service */
    private MessageChannel channel;
    
    // TODO: get it from somewhere
    private TransactionManager tm;

    public ThreadedDispatcher(Config config, MessageChannel channel) {
        this.config = config;
        this.channel = channel;
        channel.startReceiving(this);
    }

    @Override
    public void registerModules(CommunicatingModule... modules) {
        registerModules(Arrays.asList(modules));
    }


    @Override
    public void registerModules(Iterable<? extends CommunicatingModule> modules) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerUnicastReceiver(CommunicatingModule module,
            String category) {
        // TODO Auto-generated method stub

    }
    
    
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
    @SuppressLint("NewApi")
    // TODO: ArrayDeque requires better android, provide it or change to sth else
    private static class TransactionExecutor {

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
                    // TODO: this is an error, do sth
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

    private boolean targetExistsLocally(Message message) {
        /*String addr = message.target;
        if (message.isUnicast()) {
            return unicast.containsKey(addr);
        } else {
            // Don't validate multicast
            return true;
        }*/
        // TODO: check if the target module is loaded
        return true;
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void receive(final Message message) {
        // async execute
        // begin message transaction
        tm.begin();
        try {
            doSend(message);
            // realize transaction
            executor.get().run();
            try {
                tm.commit();
                runPostCommitStack();
            } catch (Exception e) {
                // TODO: do sth about, maybe?
                // Well, transaction failed, modules will know by the 
                // commit/rollback listeners, so maybe not, dunno. Think about 
                // it.  
            }
        } catch (Exception e) {
            tm.rollback();
        } finally {
            // After commit/rollback reset the executor
            executor.get().clear();
        }
    }

    /**
     * Runs the "rest", sequence of messages and events generated by the
     * transaction listeners, after the transaction has finished
     */
    private void runPostCommitStack() {
        try {
            for (Message message : executor.get().notifications) {
                try {
                    dispatchInThisThread(message);
                } catch (Exception e) {
                    // TODO: exception
                }
            }
            executor.get().runStack();
        } catch (Exception e) {
         // TODO: exception
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
     * Bypasses state checing, used to push first message
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
                    // TODO: exception
                }
            }
        });
    }

    /**
     * Dispatches the message in the current thread, all the way down to calling
     * {@link Module#receive}.
     */
    private void dispatchInThisThread(Message message) {
        String addr = message.target;
        if (message.mode == Mode.UNICAST) {
            // TODO: check if it exists
            //if (unicast.containsKey(addr)) {
                // TODO: lookup the module
                CommunicatingModule module = null/*unicast.get(addr)*/;
                module.receive(message, executor.get().getContext());
            /*} else {
                throw new TargetNotExistsException(addr);
            }*/
        } else {
            // Multicast
            // TODO: lookup the multicast group
            Iterable<CommunicatingModule> interested = null/*multicast.get(addr)*/;
            if (interested != null) {
                for (CommunicatingModule module : interested) {
                    module.receive(message, executor.get().getContext());
                }
            } else {
                // TODO: multicast group does not exist, 
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



}
