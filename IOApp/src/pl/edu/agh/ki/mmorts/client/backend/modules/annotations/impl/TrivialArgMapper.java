package pl.edu.agh.ki.mmorts.client.backend.modules.annotations.impl;

import pl.edu.agh.ki.mmorts.common.message.Message;

import com.app.ioapp.modules.Context;

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
