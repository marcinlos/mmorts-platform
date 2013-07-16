package pl.edu.agh.ki.mmorts.client.backend.common.message;

/**
 * Invoked when an attempt to deserialize a message fails due to invalid data,
 * i.e. the data that does not correspond to any message.
 * 
 * @see Messages#fromBytes(byte[])
 */
public class InvalidBinaryFormatException extends MessageSerializationException {

    public InvalidBinaryFormatException() {
        // empty
    }

    public InvalidBinaryFormatException(String message) {
        super(message);
    }

    public InvalidBinaryFormatException(Throwable cause) {
        super(cause);
    }

    public InvalidBinaryFormatException(String message, Throwable cause) {
        super(message, cause);
    }

}
