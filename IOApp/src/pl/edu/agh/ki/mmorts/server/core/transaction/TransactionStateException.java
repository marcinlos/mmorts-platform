package pl.edu.agh.ki.mmorts.server.core.transaction;

/**
 * Thrown when a method that requires a particular transaction state is invoked
 * in invalid state.
 */
public class TransactionStateException extends TransactionException {

    public TransactionStateException() {
        // empty
    }

    public TransactionStateException(String message) {
        super(message);
    }

    public TransactionStateException(Throwable cause) {
        super(cause);
    }

    public TransactionStateException(String message, Throwable cause) {
        super(message, cause);
    }

}
