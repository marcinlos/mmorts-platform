package pl.edu.agh.ki.mmorts.server.modules;

/**
 * Simple {@code Runnable}-like interface used as a primitive control flow
 * construct in the transaction scope.
 * 
 * @author los
 */
public interface Continuation {

    /**
     * Statements to be executed.
     * 
     * @param context
     *            Transaction context
     */
    void execute(Context context);

    /**
     * Statements to be executed in case of a transaction failure while the
     * continuation was still not consumed, waiting on the transaction stack.
     * 
     * @param e
     *            Exception that initiated the rollback
     * @param context
     *            Transaction context
     */
    void failure(Throwable e, Context context);

}
