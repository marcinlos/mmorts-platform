package com.app.ioapp.communication;

import pl.edu.agh.ki.mmorts.common.message.Message;


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
