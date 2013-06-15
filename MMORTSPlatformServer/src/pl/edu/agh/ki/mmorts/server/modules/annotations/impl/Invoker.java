package pl.edu.agh.ki.mmorts.server.modules.annotations.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class Invoker {

    public final Method method;
    public final Object target;
    
    public Invoker(Method method, Object target) {
        this.method = method;
        this.target = target;
    }

    public void invoke(Object[] args) {
        try {
            method.invoke(target, args);
        } catch (IllegalArgumentException e) {
            throw new InvocationException(this, e);
        } catch (IllegalAccessException e) {
            throw new InvocationException(this, e);
        } catch (InvocationTargetException e) {
            throw new InvocationException(this, e);
        }
    }

}
