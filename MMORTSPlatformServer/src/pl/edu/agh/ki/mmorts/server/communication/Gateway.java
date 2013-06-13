package pl.edu.agh.ki.mmorts.server.communication;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Continuation;

/**
 * Dispatcher interface for use of the client modules. Allows sending messages
 * with both unicast and multicast target addresses.
 * 
 * <p>
 * The operations concerning one request are processed in a single logical
 * transaction. In the transaction scope there are well-defined rules governing
 * the execution order of all the control-flow operations. All the operations
 * are not invoked immediately, but instead are recorded for later processing.
 * 
 * <p>
 * Execution of actions is performed in a LIFO order (stack), which results in a
 * familiar, method-call-like semantics. One unintuitive consequence is that in
 * order to set up a continuation that is to be invoked after some sent
 * messsages are processed, it needs to be pushed before sending the messages.
 * In general, invocation order is the reverse of that of actions registration.
 */
public interface Gateway extends ServiceLocator {

    /**
     * Immediately sends a message. All the required data (whether it is
     * local/remote, uni/multicast etc) is contained inside the {@code message}.
     * 
     * @param mesage
     *            Message to be sent
     * @throws TargetNotExistsException
     *             If the message target does not exists
     */
    void send(Message mesage);

    /**
     * Sends a message at the successful commit of the current transaction. Can
     * be called <b>only</b> during the transaction.
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
