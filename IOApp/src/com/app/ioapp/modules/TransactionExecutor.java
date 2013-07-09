package com.app.ioapp.modules;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.List;

import pl.edu.agh.ki.mmorts.common.message.Message;
import android.annotation.SuppressLint;
import android.util.Log;

/**
 * Helper class containing per-thread state necessary for conducting
 * transactions.
 * 
 */
public class TransactionExecutor {



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

	private static final String ID = "State";

    /** State of the transaction */
    private TransactionExecutor.State state = State.NO_TRANS;

    /** Transaction stack */
    private final Deque<Continuation> executionStack;

    /** True if rollback handlers are being executed */
    private boolean rollback = false;

    /** Current transaction context */
    private Context context;

    /** Collection of local notifications */
    private final List<Message> notifications;

    /** Colletion of responses response messages */
    private final List<Message> responses;

    /**
     * Creates internal structures of the executor
     */
    // TODO: Android wants higher version, fix it
    @SuppressLint("NewApi")
	public TransactionExecutor() {
        executionStack = new ArrayDeque<Continuation>();
        notifications = new ArrayList<Message>();
        responses = new ArrayList<Message>();
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
    // TODO: Android wants higher version, fix it
    @SuppressLint("NewApi")
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
        responses.clear();
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
     * @return Current transaction state
     */
    public TransactionExecutor.State getState() {
        return state;
    }

    /**
     * @return {@code true} if there is an active, uncommited transaction,
     *         {@code false} otherwise
     */
    public boolean inTransaction() {
        return state == State.IN_TRANS;
    }

    /**
     * @return {@code true} if there is no current transaction (active or during
     *         post-commit phase), {@code false} otherwise
     */
    public boolean noTransaction() {
        return state == State.NO_TRANS;
    }

    /**
     * @return List of pending responses
     */
    public List<Message> responses() {
        return responses;
    }

    /**
     * Adds a new response message to the list
     * 
     * @param message
     *            Response message
     */
    public void addResponse(Message message) {
        responses.add(message);
    }

    /**
     * @return List of pending notifications
     */
    public List<Message> notifications() {
        return notifications;
    }

    /**
     * Adds a new notification to the list
     * 
     * @param message
     *            Notification to execute after transaction
     */
    public void addNotification(Message message) {
        notifications.add(message);
    }

    /**
     * Informs all the remaining continuations about the failure.
     * 
     * @param exc
     *            Exception that caused the failure
     */
    // TODO: Android requires new version, fix it
    @SuppressLint("NewApi")
	private void rollbackAll(Throwable exc) {
        rollback = true;
        while (!executionStack.isEmpty()) {
            Continuation cont = executionStack.pop();
            try {
                cont.failure(exc, context);
            } catch (Exception e) {
            	Log.e(ID,"Exception in a failure handler");
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
    // TODO: Android wants higher version, fix it
    @SuppressLint("NewApi")
	public void push(Continuation cont) {
        if (!rollback) {
            executionStack.push(cont);
        } else {
            throw new IllegalStateException(
                    "Cannot push continuation during rollback");
        }
    }
}