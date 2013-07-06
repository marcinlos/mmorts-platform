package pl.edu.agh.ki.mmorts.client.modules.annotations.impl;

import com.app.ioapp.modules.Context;

import pl.edu.agh.ki.mmorts.common.message.Message;

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
