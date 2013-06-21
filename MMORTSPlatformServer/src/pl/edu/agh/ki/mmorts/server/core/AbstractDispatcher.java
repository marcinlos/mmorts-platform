package pl.edu.agh.ki.mmorts.server.core;

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
import pl.edu.agh.ki.mmorts.server.modules.ModuleLogicException;
import pl.edu.agh.ki.mmorts.server.modules.util.ContAdapter;

/**
 * Partial implementation of a message dispatcher.
 * 
 * @author los
 */
public abstract class AbstractDispatcher extends DefaultModuleContainer
        implements MessageReceiver, Dispatcher {

    private static final Logger logger = Logger
            .getLogger(AbstractDispatcher.class);

    /** Configuration object */
    @Inject
    private Config config;

    /** Message service */
    @Inject
    private MessageInputChannel channel;

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
        channel.startReceiving(this);
    }

    /**
     * @return Input message channel
     */
    protected MessageInputChannel channel() {
        return channel;
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
    protected void dispatchMessage(Message message, Response response) {
        logger.debug("Begin message transaction");
        // begin message transaction
        try {
            // lock the module list to ensure consistency across the transaction
            readLock.lock();
            tm.begin();
            // place the message on the stack
            doSend(message);
            // realize transaction
            executor().run();
            try {
                logger.debug("Transaction commited, executing listeners");
                tm.commit();
                runPostCommitStack();
                response.send(version(), executor().responses());
            } catch (Exception e) {
                logger.error("Exception inside a commit handler", e);
                response.failed(e);
            }
        } catch (ModuleLogicException e) {
            logger.debug("Transaction rolled back due to exception", e);
            // saved messages were cleared by the executor inside the run()
            tm.rollback();
            try {
                // handlers may have sent some messages
                runPostCommitStack();
                response.send(version(), executor().responses());
            } catch (Exception e1) {
                logger.error("Exception during post-rollback communication", e1);
                response.failed(e1);
            }
        } catch (Exception e) {
            // other exception - this is an error
            logger.error("Unexpected exception during request processing", e);
            tm.rollback();
            response.failed(e);
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
                    doSend(message);
                } catch (TargetNotExistsException e) {
                    // this will result in a fatal exception later on anyway,
                    // it is kind of ignored here to ensure consistency of
                    // exception semantics
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
            onShutdown();
            shutdownModules();
            logger.debug("Dispatcher shat down");
        } catch (Exception e) {
            logger.error("Error while shutting down the communication channel");
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
