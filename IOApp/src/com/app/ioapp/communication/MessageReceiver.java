package com.app.ioapp.communication;

/**
 * Entity receiving messages from the {@linkplain MessageChannel}.
 */
public interface MessageReceiver {
    
    /**
     * Called when a new message is to be delivered.
     * 
     * @param message Received message
     */
    void receive(Message message);

}
