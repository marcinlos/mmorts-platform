package com.app.ioapp.communication;

/**
 * Partial implementation, merely managing the message receiver.
 */
public abstract class AbstractChannel implements MessageChannel {

    /** Message receiver object */
    private Gateway receiver;

    /**
     * @return Previously set message receiver
     */
    protected Gateway getReceiver() {
        return receiver;
    }

    /**
     * Forwards the message to the associated {@code MessageReceiver}. Does not
     * check for {@code null}!
     * 
     * @param message
     */
    public void forwardMessage(Message message) {
        receiver.receive(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void startReceiving(Gateway receiver) {
        if (receiver != null) {
            this.receiver = receiver;
        } else {
            throw new IllegalArgumentException("null receiver not allowed");
        }
    }

}
