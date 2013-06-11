package pl.agh.edu.ki.mmorts.server.util.reflection;

/**
 * Exception thrown to indicate failed reflective method invocation
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
