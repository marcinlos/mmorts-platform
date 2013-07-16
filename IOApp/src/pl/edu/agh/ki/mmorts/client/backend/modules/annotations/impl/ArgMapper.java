package pl.edu.agh.ki.mmorts.client.backend.modules.annotations.impl;

import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;



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
    Object[] map(Message msg, TransactionContext ctx);

}
