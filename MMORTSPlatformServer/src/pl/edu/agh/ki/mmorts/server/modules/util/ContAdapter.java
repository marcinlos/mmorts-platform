package pl.edu.agh.ki.mmorts.server.modules.util;

import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.Continuation;

/** 
 * Adaptor class to remove some {@linkplain Continuation} boilerplate.
 */
public class ContAdapter implements Continuation {

    /**
     * {@inheritDoc}
     */
    @Override
    public void execute(Context context) {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void failure(Throwable e, Context context) {
        // empty
    }

}
