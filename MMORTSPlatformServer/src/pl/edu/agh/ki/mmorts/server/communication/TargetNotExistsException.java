package pl.edu.agh.ki.mmorts.server.communication;

/**
 * Thrown when a message is send with a unicast address that is not registered
 * at the destination dispatcher.
 * 
 * @author los
 */
public class TargetNotExistsException extends CommunicationException {

    public TargetNotExistsException() {
        // empty
    }

    public TargetNotExistsException(String message) {
        super(message);
    }

    public TargetNotExistsException(Throwable cause) {
        super(cause);
    }

    public TargetNotExistsException(String message, Throwable cause) {
        super(message, cause);
    }

}
