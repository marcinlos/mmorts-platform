package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

/**
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
