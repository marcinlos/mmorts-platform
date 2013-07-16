package pl.edu.agh.ki.mmorts.client.backend.modules.annotations.impl;

import pl.agh.edu.ki.mmorts.client.backend.modules.Context;
import pl.edu.agh.ki.mmorts.common.message.Message;


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
