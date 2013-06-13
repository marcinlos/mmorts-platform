package pl.edu.agh.ki.mmorts.server.core;

/**
 * Base exception class for initialization failures
 */
public class InitException extends RuntimeException {

    public InitException() {
        // empty
    }

    public InitException(String message) {
        super(message);
    }

    public InitException(Throwable cause) {
        super(cause);
    }

    public InitException(String message, Throwable cause) {
        super(message, cause);
    }

}
