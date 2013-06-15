package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Context;

public class HandlerImpl implements Handler {

    public final Invoker invoker;
    public final ArgumentMapper mapper;
    
    public HandlerImpl(Invoker invoker, ArgumentMapper mapper) {
        this.invoker = invoker;
        this.mapper = mapper;
    }

    @Override
    public void handle(Message msg, Context ctx) {
        Object[] args = mapper.map(msg, ctx);
        invoker.invoke(args);
    }
    

}
