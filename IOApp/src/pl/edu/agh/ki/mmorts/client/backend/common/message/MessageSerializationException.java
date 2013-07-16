package pl.edu.agh.ki.mmorts.client.backend.common.message;

/**
 * Thrown to indicate a problem during message serialization, like presence of
 * non-serializable object in the message's content, invalid binary data,
 * invalid/unknown data class etc.
 * 
 */
public class MessageSerializationException extends MessageException {

    public MessageSerializationException() {
        // empty
    }

    public MessageSerializationException(String message) {
        super(message);
    }

    public MessageSerializationException(Throwable cause) {
        super(cause);
    }

    public MessageSerializationException(String message, Throwable cause) {
        super(message, cause);
    }

}
