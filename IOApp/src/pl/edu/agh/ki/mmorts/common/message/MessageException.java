package pl.edu.agh.ki.mmorts.common.message;

/**
 * Base exception class for reporting message-related problems. 
 * 
 * @see Message
 */
public class MessageException extends RuntimeException {

    public MessageException() {
        // empty
    }

    public MessageException(String message) {
        super(message);
    }

    public MessageException(Throwable cause) {
        super(cause);
    }

    public MessageException(String message, Throwable cause) {
        super(message, cause);
    }

}
