package com.app.ioapp.modules;

import pl.edu.agh.ki.mmorts.common.message.Message;


/**
 * Interface of the module, basic unit of functionality.
 * 
 * <p>
 * Modules are message-based request handlers, reacting on messages delivered by
 * the dispatcher.
 * 
 * Interface of the module used during initialization process.
 */
public interface CommunicatingModule extends Module {


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
     *            TODO: javadoc
     */
    void receive(Message message, Context ctx);
    
    
    /**
     * Called during the server's shutdown sequence.
     */
    void shutdown();
}
