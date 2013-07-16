package pl.agh.edu.ki.mmorts.client.backend.communication;

import pl.agh.edu.ki.mmorts.client.backend.modules.Continuation;
import pl.edu.agh.ki.mmorts.common.message.Message;


/**
 * Dispatcher interface for use of the client modules. Allows sending messages
 * with both unicast and multicast target addresses.
 */
public interface Gateway {

    /**
     * Immediately sends a local message. All the required data (whether it is
     * uni/multicast etc) is contained inside the {@code message}.
     * 
     * @param mesage
     *            Message to be sent
     * @throws TargetNotExistsException
     *             If the message target does not exists
     */
    void send(Message mesage);

    /**
     * Sends a local message at the successful <b>commit</b> of the current
     * transaction. Can be called <b>only</b> during the transaction.
     * 
     * @param message
     *            Message to be sent at the end of transaction
     */
    void sendDelayed(Message message);

    /**
     * Adds an item to the execution queue of a transaction.
     * 
     * @param cont
     *            Action to execute
     */
    void later(Continuation cont);

}

