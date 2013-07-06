package pl.edu.agh.ki.mmorts.client.modules.annotations.impl;

import com.app.ioapp.modules.Context;

import pl.edu.agh.ki.mmorts.common.message.Message;

/**
 * Trivial argument mapper, does not perform any transformation of arguments.
 * 
 */
public class TrivialArgMapper implements ArgMapper {

    @Override
    public Object[] map(Message msg, Context ctx) {
        return new Object[] { msg, ctx };
    }

}
