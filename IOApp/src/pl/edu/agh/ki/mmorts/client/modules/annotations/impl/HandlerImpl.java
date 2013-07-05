package pl.edu.agh.ki.mmorts.client.modules.annotations.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import com.app.ioapp.modules.Context;

import pl.edu.agh.ki.mmorts.common.message.Message;

/**
 * Default {@linkplain Handler} implementation, uses a method object and an
 * argument mapper to perform invocations.
 * 
 * @author los
 */
public class HandlerImpl implements Handler {

    /** Method to invoke */
    public final Method method;

    /** Used argument mapper */
    public final ArgMapper mapper;

    
    public HandlerImpl(Method method, ArgMapper mapper) {
        this.method = method;
        this.mapper = mapper;
    }

    /**
     * {@inheritDoc}
     */
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
