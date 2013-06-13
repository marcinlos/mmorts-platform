package pl.edu.agh.ki.mmorts.server.modules;

import pl.edu.agh.ki.mmorts.server.communication.Message;

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
     * Called in the second phase of moduleClass initialization. The
     * communication environment is fully operational, it is valid to
     * communicate with other modules inside this method.
     */
    void started();

    /**
     * Called when a relevant message (uni/multicast) has been delivered to the
     * dispatcher.
     * 
     * @param message
     *            Message of interest (i.e. matching module's unicast address or
     *            one of its' multicast groups)
     * @param context
     *            Per-transaction general-purpose data store, intended as the
     *            stash to pass arbitrary information between modules.
     */
    void receive(Message message, Context context);

    /**
     * Called during the server's shutdown sequence.
     */
    void shutdown();
}
