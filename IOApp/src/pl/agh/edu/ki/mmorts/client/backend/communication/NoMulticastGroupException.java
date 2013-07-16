package pl.agh.edu.ki.mmorts.client.backend.communication;

/**
 * Thrown when a message is send with a multicast group address and the group is
 * not registered (i.e. has no members) at the destination dispatcher.
 */
public class NoMulticastGroupException extends CommunicationException {

    public NoMulticastGroupException() {
        // empty
    }

    public NoMulticastGroupException(String message) {
        super(message);
    }

    public NoMulticastGroupException(Throwable cause) {
        super(cause);
    }

    public NoMulticastGroupException(String message, Throwable cause) {
        super(message, cause);
    }

}
