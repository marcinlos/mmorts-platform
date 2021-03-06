package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

/**
 * Thrown upon a failure of the invocation.
 * 
 * @author los
 */
public class InvocationException extends MappingException {

    public InvocationException() {
        // emtpy
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
