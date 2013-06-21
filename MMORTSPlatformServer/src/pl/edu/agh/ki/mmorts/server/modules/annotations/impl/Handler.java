package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Context;

/**
 * 
 * @author los
 */
public interface Handler {
    
    void handle(Object o, Message msg, Context ctx);

}
