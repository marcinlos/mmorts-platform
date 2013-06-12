package pl.edu.agh.ki.mmorts.server.core.transaction;

import java.util.ArrayDeque;
import java.util.Queue;

import org.apache.log4j.Logger;

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
public class SequentialTM {

    private static final Logger logger = Logger.getLogger(SequentialTM.class);

    /** Listeners queue */
    private Queue<TransactionListener> listeners = new ArrayDeque<TransactionListener>();

    /**
     * Adds a transaction listener.
     * 
     * @param listener Listener to be registered
     */
    public void addListener(TransactionListener listener) {
        listeners.add(listener);
    }

    /**
     * Calls {@link TransactionListener#commit()} methods of all the listeners.
     * If some of them fails, the transaction is assumed to be unsuccessful, and
     * the remaining listeners are rolled back.
     * 
     * <p>
     * After execution of this method, listeners queue will be empty.
     */
    public void commit() {
        try {
            while (!listeners.isEmpty()) {
                TransactionListener cb = listeners.poll();
                cb.commit();
            }
        } catch (Exception e) {
            logger.error("Exception inside the commit callback", e);
            rollback();
        }
    }

    /**
     * Calls {@linkplain TransactionListener#rollback()} methods of all the
     * listeners. Unlike in the {@linkplain #commit()} method, failures of the
     * calls do not interrupt the loop. All the listeners are rolled back
     * regardless of individual failures.
     */
    private void rollback() {
        while (!listeners.isEmpty()) {
            TransactionListener cb = listeners.poll();
            try {
                cb.rollback();
            } catch (Exception e) {
                logger.error("Exception inside rollback callback", e);
            }
        }
    }

}
