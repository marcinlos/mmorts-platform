package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Context;

/**
 * Trivial argument mapper, does not perform any transformation of arguments.
 * 
 * @author los
 */
public class TrivialArgMapper implements ArgMapper {

    @Override
    public Object[] map(Message msg, Context ctx) {
        return new Object[] { msg, ctx };
    }

}
