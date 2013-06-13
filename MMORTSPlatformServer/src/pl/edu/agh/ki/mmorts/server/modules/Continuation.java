package pl.edu.agh.ki.mmorts.server.modules;

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
    void execute(Context context);

}
