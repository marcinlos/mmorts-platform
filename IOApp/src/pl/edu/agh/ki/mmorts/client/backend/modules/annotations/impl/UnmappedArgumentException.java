package pl.edu.agh.ki.mmorts.client.backend.modules.annotations.impl;

/**
 * Thrown when the chosen method contains arguments for which no sensible
 * interpretation could have been found, and thus it is not known what value
 * should they be given at the time of invocation.
 * 
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
