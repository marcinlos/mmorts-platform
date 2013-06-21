package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.Module;

/**
 * Transforms usual {@link Module#receive} arguments into some other argument
 * list.
 * 
 * @author los
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
