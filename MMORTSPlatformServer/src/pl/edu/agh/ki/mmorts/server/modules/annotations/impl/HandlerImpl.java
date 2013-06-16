package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Context;

public class HandlerImpl implements Handler {

    public final Method method;
    public final ArgMapper mapper;
    
    public HandlerImpl(Method method, ArgMapper mapper) {
        this.method = method;
        this.mapper = mapper;
    }

    @Override
    public void handle(Object target, Message msg, Context ctx) {
        Object[] args = mapper.map(msg, ctx);
        try {
            method.invoke(target, args);
        } catch (IllegalArgumentException e) {
            throw new InvocationException(e);
        } catch (IllegalAccessException e) {
            throw new InvocationException(e);
        } catch (InvocationTargetException e) {
            throw new InvocationException(e);
        }
    }
    

}
