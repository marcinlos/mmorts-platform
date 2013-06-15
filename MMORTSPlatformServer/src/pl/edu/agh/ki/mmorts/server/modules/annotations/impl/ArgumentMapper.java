package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Context;

public interface ArgumentMapper {
    
    Object[] map(Message msg, Context ctx);

}
