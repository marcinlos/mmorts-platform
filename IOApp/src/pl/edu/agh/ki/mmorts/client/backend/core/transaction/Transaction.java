package pl.edu.agh.ki.mmorts.client.backend.core.transaction;

/**
 * Interface of a single transaction.
 */
public interface Transaction {

    /**
     * Adds a transaction listener receiving notification about transaction
     * commit/rollback. Listeners are notified in a LIFO order (stack).
     * 
     * @param listener
     *            Listener to add
     * @throws TransactionStateException
     *             If the transaction has already finished, i.e. has been
     *             commited or rolled back
     */
    void addListener(TransactionListener listener);

}
