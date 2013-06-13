package pl.edu.agh.ki.mmorts.server.communication;

import pl.edu.agh.ki.mmorts.server.modules.Continuation;

/**
 * Dispatcher interface for use of the client modules. Allows sending messages
 * with both unicast and multicast target addresses.
 */
public interface Gateway extends ServiceLocator {

    /**
     * Immediately sends a message. All the required data (whether it is
     * local/remote, uni/multicast etc) is contained inside the {@code message}.
     * 
     * @param mesage
     *            Message to be sent
     * @throws NoMulticastGroupException
     *             If the address does not identify existing, registered group
     *             at the target dispatcher
     */
    void send(Message mesage);

    /**
     * Sends a message at the successful commit of the current transaction.
     * 
     * @param message
     *            Message to be sent at the end of transaction
     */
    void sendDelayed(Message message);

    /**
     * Adds an item to the execution queue ofa transaction.
     * 
     * @param cont
     *            Action to execute
     */
    void later(Continuation cont);
    
    

}
