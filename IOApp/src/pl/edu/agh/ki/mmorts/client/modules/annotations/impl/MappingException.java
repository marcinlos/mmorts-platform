package pl.edu.agh.ki.mmorts.client.modules.annotations.impl;

/**
 * Base class for exceptions caused by the message mapping problems.
 * 
 */
public class MappingException extends RuntimeException {

    public MappingException() {
        // empty
    }

    public MappingException(String message) {
        super(message);
    }

    public MappingException(Throwable cause) {
        super(cause);
    }

    public MappingException(String message, Throwable cause) {
        super(message, cause);
    }

}
