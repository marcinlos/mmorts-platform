package pl.edu.agh.ki.mmorts.client.backend.modules.annotations.impl;

import pl.edu.agh.ki.mmorts.common.message.Message;

import com.app.ioapp.modules.Context;

/**
 * General message receive invocation handler.
 * 
 */
public interface Handler {

    /**
     * Handle a message receive
     * 
     * @param o
     *            Module object that received the message
     * @param msg
     *            Message
     * @param ctx
     *            Transaction context
     */
    void handle(Object o, Message msg, Context ctx);

}
