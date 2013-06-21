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
 * 
 * <p>
 * As for the behaviour of messaging system in case of exceptions:
 * <ul>
 * <li>If an exception occurs inside the transaction, all the messages sent
 * before it by {@link #sendDelayed} and {@link #sendResponse} are discarded.
 * Any further response messages are delivered.
 * <li>If an exception occurs inside the commit handler, all the messages are
 * discarded - this situation is considered abnormal
 * </ul>
 * 
 * @author los
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
     * Sends a response to the client. They are witheld until the completion of
     * transaction handlers. Can be called during the transaction or inside the
     * transaction handlers. In the first case messages are treated as a part of
     * the transaction, hence are not delivered if the transaction fails.
     * 
     * @param message
     */
    void output(Message message);

    /**
     * Adds an item to the execution queue of a transaction.
     * 
     * @param cont
     *            Action to execute
     */
    void later(Continuation cont);

}
