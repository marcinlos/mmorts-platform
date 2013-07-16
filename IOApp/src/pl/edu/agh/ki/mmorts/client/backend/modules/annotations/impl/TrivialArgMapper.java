package pl.edu.agh.ki.mmorts.client.backend.modules.annotations.impl;

import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.modules.TransactionContext;



/**
 * Trivial argument mapper, does not perform any transformation of arguments.
 * 
 */
public class TrivialArgMapper implements ArgMapper {

    @Override
    public Object[] map(Message msg, TransactionContext ctx) {
        return new Object[] { msg, ctx };
    }

}
