package com.app.ioapp.communication;

import pl.edu.agh.ki.mmorts.common.message.MessagePack;

/**
 * Entity receiving messages from the {@linkplain MessageInputChannel}.
 * 
 */
public interface MessageReceiver {

    /**
     * Called when a new message is to be delivered.
     * 
     * @param message
     *            Received message
     * @param response
     *            Callback interface for sending the response
     */
    void receive(MessagePack message);

}
