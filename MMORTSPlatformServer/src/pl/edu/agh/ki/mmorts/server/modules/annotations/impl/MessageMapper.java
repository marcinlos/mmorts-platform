package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.annotations.MessageMapping;

public class MessageMapper {
    
    static class Entry {
        MessageMapping desc;
        Handler handler;
        
        void handle(Message msg, Context ctx) {
            handler.handle(msg, ctx);
        }
    }
    
    

    public MessageMapper() {
        // TODO Auto-generated constructor stub
    }

}
