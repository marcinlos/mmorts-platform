package pl.edu.agh.ki.mmorts.client.core.transaction;

/**
 * Interface of a transaction callback. Used by the transaction manager and
 * transaction participants to react on the transaction final outcome.
 */
public interface TransactionListener {

    /**
     * Invoked upon transaction commit
     */
    void commit();

    /**
     * Invoked upon transaction rollback
     */
    void rollback();

}
