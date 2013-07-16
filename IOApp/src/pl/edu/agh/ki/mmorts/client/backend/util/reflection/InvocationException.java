package pl.edu.agh.ki.mmorts.client.backend.util.reflection;

/**
 * Exception thrown to indicate failed reflective method invocation.
 * 
 */
public class InvocationException extends RuntimeException {

    public InvocationException() {
        // empty
    }

    public InvocationException(String message) {
        super(message);
    }

    public InvocationException(Throwable cause) {
        super(cause);
    }

    public InvocationException(String message, Throwable cause) {
        super(message, cause);
    }

}
