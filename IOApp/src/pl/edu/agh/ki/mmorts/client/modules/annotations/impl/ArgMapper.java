package pl.edu.agh.ki.mmorts.client.modules.annotations.impl;

import pl.edu.agh.ki.mmorts.common.message.Message;

import com.app.ioapp.modules.Context;

/**
 * Transforms usual {@link Module#receive} arguments into some other argument
 * list.
 * 
 */
public interface ArgMapper {

    /**
     * Transform the arguments to some other form
     * 
     * @param msg Message
     * @param ctx Transaction context
     * @return Some argument list
     */
    Object[] map(Message msg, Context ctx);

}
