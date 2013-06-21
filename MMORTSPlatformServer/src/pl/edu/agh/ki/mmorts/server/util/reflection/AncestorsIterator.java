package pl.edu.agh.ki.mmorts.server.util.reflection;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * Iterator class designed for traversing the inheritance DAG of a given class
 * or interface.
 * <p>
 * Order in case of complex multiple-inheritance based hierarchies is for now
 * left undefined.
 * 
 * @author los
 */
public class AncestorsIterator implements Iterator<Class<?>> {

    /** For traversing the graph */
    private Deque<Class<?>> queue = new ArrayDeque<Class<?>>();

    /** To prevent visiting a node (e.g. an interface) multiple times */
    private Set<Class<?>> visited = new HashSet<Class<?>>();

    /**
     * Creates an iterator walking up the class tree beginning at {@code clazz}.
     * 
     * @param clazz
     *            Starting point
     */
    public AncestorsIterator(Class<?> clazz) {
        queue.offer(clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean hasNext() {
        return !queue.isEmpty();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public Class<?> next() {
        Class<?> clazz = queue.removeFirst();
        visited.add(clazz);
        push(clazz.getSuperclass());
        for (Class<?> iface : clazz.getInterfaces()) {
            push(iface);
        }
        return clazz;
    }

    /**
     * Unsupported for this iterator.
     */
    @Override
    public void remove() {
        throw new UnsupportedOperationException("Cannot modify class hierarchy");
    }

    private void push(Class<?> clazz) {
        if (clazz != null && !visited.contains(clazz)) {
            queue.offer(clazz);
            visited.add(clazz);
        }
    }

}
