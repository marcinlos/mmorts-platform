package pl.edu.agh.ki.mmorts.server.modules;

/**
 * Interface of the moduleClass, basic unit of functionality.
 * 
 * <p>
 * Modules are message-based request handlers, reacting on messages delivered by
 * the dispatcher.
 * 
 * Interface of the moduleClass used during initialization process.
 */
public interface Module {

    /**
     * Called in the first phase of moduleClass initailization, before the
     * communication environment is fully operational.
     */
    void init();

    /**
     * Called in the second phase of moduleClass initialization. The communication
     * environment is fully operational, it is valid to communicate with other
     * modules inside this method.
     */
    void started();

}
