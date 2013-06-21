package pl.edu.agh.ki.mmorts.server.core.transaction;

/**
 * Base class for transaction-related exceptions.
 */
public class TransactionException extends RuntimeException {

    public TransactionException() {
        // empty
    }

    public TransactionException(String message) {
        super(message);
    }

    public TransactionException(Throwable cause) {
        super(cause);
    }

    public TransactionException(String message, Throwable cause) {
        super(message, cause);
    }

}
