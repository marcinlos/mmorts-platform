package pl.edu.agh.ki.mmorts.server.core.transaction;

/**
 * Class designed to provide simple transactionality of message processing. It
 * works by providing simple begin/commit/rollback interface and a possibility
 * to register commit/rollback callbacks. This way transaction participants are
 * able to guarantee atomicity, as long as they provide appropriate callbacks
 * and care is taken of locking the underlying resources.
 */
public interface TransactionManager {
    
    /**
     * Registers a new transaction listener.
     * 
     * @param listener Listener to be registered
     */
    void addListener(TransactionsBeginListener listener);
    
    /**
     * Unregisters a new transaction listener.
     * 
     * @param listener Listener to be unregistered
     */
    void removeListener(TransactionsBeginListener listener);
    
    /**
     * @return Currently active transaction, or {@code null} if there is none.
     */
    Transaction getCurrent();

    /**
     * Starts a new transaction.
     * 
     * @return Transaction object
     * @throws TransactionStateException
     *             If there is already an active transaction
     */
    Transaction begin();

    /**
     * Commit the current transaction, run listeners.
     * 
     * @throws TransactionStateException
     *             If there is no currently active transaction
     */
    void commit();

    /**
     * Rollback the current transaction, run listeners.
     * 
     * @throws TransactionStateException
     *             If there is no currently active transaction
     */
    void rollback();

}
