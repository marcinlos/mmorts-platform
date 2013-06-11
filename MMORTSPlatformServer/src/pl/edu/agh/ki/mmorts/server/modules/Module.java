package pl.edu.agh.ki.mmorts.server.modules;

/**
 * Interface for initialization process.
 */
public interface Module {

    /**
     * Called in the first phase of module initailization, before the
     * communication environment is fully operational.
     */
    void init();

    /**
     * Called in the second phase of module initialization. The communication
     * environment is fully operational, it is valid to communicate with other
     * modules inside this method.
     */
    void started();

}
