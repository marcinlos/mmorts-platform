package com.app.ioapp.modules;

import javax.inject.Inject;

import pl.edu.agh.ki.mmorts.client.communication.MessageOutputChannel;
import pl.edu.agh.ki.mmorts.client.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.common.message.Message;

import android.util.Log;

import com.app.ioapp.communication.MessageReceiver;
import com.app.ioapp.communication.TargetNotExistsException;
import com.app.ioapp.config.Config;

/**
 * Partial implementation of a message dispatcher.
 * 
 */
public abstract class AbstractDispatcher extends AbstractModuleContainer
        implements MessageReceiver, Dispatcher {
	
	private static final String ID = "AbstractDispatcher";


    /** Configuration object */
    @Inject
    private Config config;

    /** Message service */
    @Inject
    private MessageOutputChannel channel;

    /** Transaction manager */
    @Inject
    private TransactionManager tm;

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
    protected abstract TransactionExecutor executor();

    /**
     * Creates the thread pool and initializes the communication. It is not
     * annotated with {@linkplain OnInit} due to unspecified ordering of calling
     * {@code OnInit} methods and the fact this step should generally be the
     * last, as it assumes the dispatcher is fully prepared for handling
     * incoming messages. Ensure it's called in the concrete implementation
     * dispatcher.
     */
    protected void init() {
//        channel.startReceiving(this);
    }

    /**
     * @return Input message channel
     */
    protected MessageOutputChannel channel() {
        return channel;
    }

    /**
     * Dispatches the message and performs the full message processing
     * transaction.
     * 
     * @param message
     *            Message to process
     */
    protected void dispatchMessage(Message message) {
    	Log.e(ID,"Begin message transaction");
        // begin message transaction
        try {
            // lock the module list
            readLock.lock();
            tm.begin();
            doSend(message);
            // realize transaction
            executor().run();
            try {
            	Log.e(ID,"Transaction commited, executing listeners");
                tm.commit();
                runPostCommitStack();
            } catch (Exception e) {
            	Log.e(ID,"Exception inside a commit handler");
            }
        } catch (Exception e) {
        	Log.e(ID,"Transaction rolled back due to exception");
            tm.rollback();
        } finally {
            // After commit/rollback reset the executor
            executor().clear();
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
                	Log.e(ID,"Notification for nonexistant module");
                } catch (Exception e) {
                	Log.e(ID,"Exception while processing post-commit notifications");
                    throw e;
                }
            }
            executor().runStack();
        } catch (Exception e) {
        	Log.e(ID,"Exception during the post-commit phase");
            throw e;
        }
    }

    /**
     * Shutdown callback, notifies modules.
     */
//    @OnShutdown
    public void shutdown() {
    	Log.e(ID,"Shutting down the dispatcher");
        try {
            onShutdown();
            shutdownModules();
            Log.e(ID,"Dispatcher shut down");
        } catch (Exception e) {
        	Log.e(ID,"Error while shutting down the communication channel");
            throw new RuntimeException(e);
        }
    }

    /**
     * Invoked during shutdown, before shutting down modules
     */
    protected abstract void onShutdown();

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
     * Bypasses state checking, used to push first message to initiate the
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
            CommunicatingModule module = unicast().get(addr);
            if (module != null) {
                module.receive(message, executor().getContext());
            } else {
                throw new TargetNotExistsException(addr);
            }
        } else {
            // Multicast
            Iterable<CommunicatingModule> interested = multicast().get(addr);
            if (interested != null) {
                for (CommunicatingModule module : interested) {
                    module.receive(message, executor().getContext());
                }
            } else {
            	Log.e(ID,"Message to nonexistant multicast group [" + addr + "]");
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
