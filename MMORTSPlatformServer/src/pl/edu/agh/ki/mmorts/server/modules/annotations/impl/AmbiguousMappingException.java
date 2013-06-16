package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

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
