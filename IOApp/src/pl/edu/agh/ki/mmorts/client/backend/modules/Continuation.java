package pl.edu.agh.ki.mmorts.client.backend.modules;


/**
 * Simple {@code Runnable}-like interface used as a primitive control flow
 * construct in the transaction scope.
 */
public interface Continuation {

    /**
     * Statements to be executed.
     * 
     * @param context
     *            Transaction context
     */
    void execute(TransactionContext context);

    /**
     * Statements to be executed in case of a transaction failure while the
     * continuation was still not consumed, waiting on the transaction stack.
     * 
     * @param e
     *            Exception that initiated the rollback
     * @param context
     *            Transaction context
     */
    void failure(Throwable e, TransactionContext context);

}
