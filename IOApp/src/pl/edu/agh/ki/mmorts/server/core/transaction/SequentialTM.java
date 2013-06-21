package pl.edu.agh.ki.mmorts.server.core.transaction;

import android.annotation.SuppressLint;
import java.util.ArrayDeque;
import java.util.Deque;

/**
 * "One-thread" transaction manager. Simple implementation designed to be used
 * as a building block for full threaded transaction manager.
 * 
 * <p>
 * It is not per se associated with any thread, it is well possible to use it in
 * multiple threads simultaneously, as long as the proper external
 * synchronization is provided, as the methods of {@code TransactionManager} are
 * not thread-safe. This is because it is intended to be used by the single
 * thread, or on a manager per thread basis.
 */
@SuppressLint("NewApi")
//TODO: apparently he needs better android, provide it or change the deque 
// to sth else
public class SequentialTM {


    /** Listeners queue */
    private Deque<TransactionListener> listeners = new ArrayDeque<TransactionListener>();

    /**
     * Adds a transaction listener.
     * 
     * @param listener
     *            Listener to be registered
     */
    public void addListener(TransactionListener listener) {
        listeners.push(listener);
    }

    /**
     * @return New {@linkplain Transaction} implementation
     */
    public Transaction createTx() {
        return new Transaction() {
            @Override
            public void addListener(TransactionListener listener) {
                SequentialTM.this.addListener(listener);
            }
        };
    }

    /**
     * Calls {@link TransactionListener#commit()} methods of all the listeners.
     * If some of them fails, the transaction is assumed to be unsuccessful, and
     * the remaining listeners are rolled back. The exception that caused the
     * failure is rethrown, wrapped in a {@code RuntimeException}.
     * 
     * <p>
     * After execution of this method, listeners queue will be empty.
     */
    public void commit() {
        try {
            while (!listeners.isEmpty()) {
                TransactionListener cb = listeners.pop();
                cb.commit();
            }
        } catch (RuntimeException e) {
            rollback();
            throw new RuntimeException("Inside a commit handler", e);
        }
    }

    /**
     * Calls {@linkplain TransactionListener#rollback()} methods of all the
     * listeners. Unlike in the {@linkplain #commit()} method, failures of the
     * calls do not interrupt the loop. All the listeners are rolled back
     * regardless of individual failures.
     */
    public void rollback() {
        while (!listeners.isEmpty()) {
            TransactionListener cb = listeners.pop();
            try {
                cb.rollback();
            } catch (Exception e) {
                // TODO: logger
            }
        }
    }

}
