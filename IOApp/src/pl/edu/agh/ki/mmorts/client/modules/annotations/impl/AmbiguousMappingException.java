package pl.edu.agh.ki.mmorts.client.modules.annotations.impl;

/**
 * Thrown when there is no single best match for a message.
 * 
 * @author los
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
