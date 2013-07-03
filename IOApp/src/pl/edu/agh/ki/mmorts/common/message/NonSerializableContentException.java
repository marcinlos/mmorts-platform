package pl.edu.agh.ki.mmorts.common.message;

/**
 * Thrown upon an attempt to serialize a message whose content is not
 * serializable (either directly, e.g. does not implement {@code Serializable},
 * or some object deeper inside is not serializable.
 * 
 * <p>
 * It is worth noting that such situation (i.e. not-really-serializable
 * {@code Serializable}) is not precluded statically. It is thus perfectly valid
 * to use non-serializable objects as message's {@code data} field value, as
 * long as this message is not sent accross dispatcher boundaries.
 * 
 * @author los
 */
public class NonSerializableContentException extends
        MessageSerializationException {

    public NonSerializableContentException() {
        // empty
    }

    public NonSerializableContentException(String message) {
        super(message);
    }

    public NonSerializableContentException(Throwable cause) {
        super(cause);
    }

    public NonSerializableContentException(String message, Throwable cause) {
        super(message, cause);
    }

}
