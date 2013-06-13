package pl.edu.agh.ki.mmorts.server.communication;

/**
 * Thrown when the {@code Response#send(Message...)} method call is attempted
 * more than once.
 */
public class AlreadyRespondedException extends IllegalStateException {

    public AlreadyRespondedException() {
        // empty
    }

    public AlreadyRespondedException(String s) {
        super(s);
    }

    public AlreadyRespondedException(Throwable cause) {
        super(cause);
    }

    public AlreadyRespondedException(String message, Throwable cause) {
        super(message, cause);
    }

}
