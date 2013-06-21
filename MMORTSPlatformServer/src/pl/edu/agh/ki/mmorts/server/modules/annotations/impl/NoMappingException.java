package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

/**
 * Thrown when the message does not match criteria of any message handler in the
 * module.
 * 
 * @author los
 */
public class NoMappingException extends MappingException {

    public NoMappingException() {
        // empty
    }

    public NoMappingException(String message) {
        super(message);
    }

    public NoMappingException(Throwable cause) {
        super(cause);
    }

    public NoMappingException(String message, Throwable cause) {
        super(message, cause);
    }

}
