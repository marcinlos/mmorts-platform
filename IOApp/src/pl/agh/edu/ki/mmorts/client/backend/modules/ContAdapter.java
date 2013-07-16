package pl.agh.edu.ki.mmorts.client.backend.modules;



/** 
 * Adaptor class to remove some {@linkplain Continuation} boilerplate.
 * 
 */
public class ContAdapter implements Continuation {

    /**
     * {@inheritDoc}
     * 
     * <p>Empty implementation.
     */
    @Override
    public void execute(Context context) {
        // empty
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Empty implementation.
     */
    @Override
    public void failure(Throwable e, Context context) {
        // empty
    }

}
