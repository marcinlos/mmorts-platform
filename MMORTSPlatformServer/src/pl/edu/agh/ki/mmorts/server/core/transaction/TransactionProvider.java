package pl.edu.agh.ki.mmorts.server.core.transaction;

/**
 * Simple interface allowing to retrieve currently active transaction.
 * 
 * @author los
 */
public interface TransactionProvider {

    /**
     * @return Currently active transaction, or {@code null} if there is none.
     */
    Transaction getCurrent();

}
