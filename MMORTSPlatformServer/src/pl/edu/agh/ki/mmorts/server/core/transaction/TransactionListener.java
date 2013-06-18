package pl.edu.agh.ki.mmorts.server.core.transaction;

/**
 * Interface of a transaction callback. Used by the transaction manager and
 * transaction participants to react on the transaction final outcome.
 * 
 * @author los
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
