package pl.edu.agh.ki.mmorts.server.core.transaction;

/**
 * Interface for transaction begin interceptor.
 * 
 * @author los
 */
public interface TransactionsBeginListener {

    /**
     * Called when a new transaction begins.
     * 
     * @param transaction
     *            {@linkplain Transaction} object associated with the
     *            transaction
     */
    void begin(Transaction transaction);

}
