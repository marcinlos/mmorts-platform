package pl.edu.agh.ki.mmorts.server.communication;

/**
 * Thrown by the dispatcher when a message is sent with a unicast address, and
 * no receiver is registered with this address at the destination dispatcher.
 * 
 * @author los
 */
public class NoReceiverException extends CommunicationException {

    public NoReceiverException() {
        // empty
    }

    public NoReceiverException(String message) {
        super(message);
    }

    public NoReceiverException(Throwable cause) {
        super(cause);
    }

    public NoReceiverException(String message, Throwable cause) {
        super(message, cause);
    }

}
