package pl.edu.agh.ki.mmorts.server.communication;

import pl.edu.agh.ki.mmorts.common.message.Message;

/**
 * Partial implementation, merely managing the message receiver.
 * 
 * @author los
 */
public abstract class AbstractChannel implements MessageChannel {

    /** Message receiver object */
    private MessageReceiver receiver;

    /**
     * @return Previously set message receiver
     */
    protected MessageReceiver getReceiver() {
        return receiver;
    }

    /**
     * Forwards the message to the associated {@code MessageReceiver}. Does not
     * check for {@code null}!
     * 
     * @param message
     *            Message to forward
     * @param response
     *            Response callback for this message
     */
    public void forwardMessage(Message message, Response response) {
        receiver.receive(message, response);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startReceiving(MessageReceiver receiver) {
        if (receiver != null) {
            this.receiver = receiver;
        } else {
            throw new IllegalArgumentException("null receiver not allowed");
        }
    }

}
