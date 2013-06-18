package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

/**
 * 
 * @author los
 */
public class UnmappedArgumentException extends MappingException {

    public UnmappedArgumentException() {
        // empty
    }

    public UnmappedArgumentException(String message) {
        super(message);
    }

    public UnmappedArgumentException(Throwable cause) {
        super(cause);
    }

    public UnmappedArgumentException(String message, Throwable cause) {
        super(message, cause);
    }

}
