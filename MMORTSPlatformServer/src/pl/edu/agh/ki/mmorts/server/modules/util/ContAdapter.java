package pl.edu.agh.ki.mmorts.server.modules.util;

import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.Continuation;

/**
 * Adaptor class to remove some {@linkplain Continuation} boilerplate. It
 * provides empty implementation of both its' methods.
 * 
 * @author los
 */
public class ContAdapter implements Continuation {

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Empty implementation.
     * </p>
     */
    @Override
    public void execute(Context context) {
        // empty
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Empty implementation.
     * </p>
     */
    @Override
    public void failure(Throwable e, Context context) {
        // empty
    }

}
