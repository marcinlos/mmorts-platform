package pl.edu.agh.ki.mmorts.client.backend.util.reflection;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Iterator yielding all the superclasses of a given class (including this 
 * class itself). 
 * 
 */
public class SuperClassIterator implements Iterator<Class<?>> {
    
    private Class<?> clazz;

    public SuperClassIterator(Class<?> clazz) {
        this.clazz = clazz;
    }

    @Override
    public boolean hasNext() {
        return clazz.getSuperclass() != null;
    }

    @Override
    public Class<?> next() {
        if (! hasNext()) {
            throw new NoSuchElementException();
        }
        return clazz = clazz.getSuperclass();
    }

    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot modify class hierarchy");
    }

}
