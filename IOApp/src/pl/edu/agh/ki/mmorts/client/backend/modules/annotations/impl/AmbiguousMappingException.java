package pl.edu.agh.ki.mmorts.client.backend.modules.annotations.impl;

/**
 * Thrown when there is no single best match for a message.
 * 
 */
public class AmbiguousMappingException extends MappingException {

    public AmbiguousMappingException() {
        // empty
    }

    public AmbiguousMappingException(String message, Throwable cause) {
        super(message, cause);
    }

    public AmbiguousMappingException(String message) {
        super(message);
    }

    public AmbiguousMappingException(Throwable cause) {
        super(cause);
    }

}
