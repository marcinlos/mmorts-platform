package pl.edu.agh.ki.mmorts.server.communication;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.modules.Continuation;
import pl.edu.agh.ki.mmorts.server.modules.ModuleLogicException;

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
 * Exception semantics during the transaction are somewhat complicated. Every
 * transaction (assuming an {@linkplain Error} does not occur ) follows the
 * following scenario:
 * <ul>
 * <li>If no exception has been thrown during the main transaction body, commit
 * handlers and post-transaction action stack is executed.
 * <ul>
 * <li>If no exception occurs during the post-commit phase, response is sent to
 * the client.
 * <li>If any exception is thrown in the commit handlers, the transaction is
 * deemed unsuccessful and the remaining listeners are executed as during the
 * rollback. Exceptions during their executions are ignored. Pending delayed
 * messages are ignored, exception is sent as a response to the request.
 * <li>If an exception is thrown in the post-commit communication, it is
 * considered an error. Pending delayed messages are discarded and the
 * continuations remaining on the stack have their {@link Continuation#failure}
 * executed. Exceptions during their execution are ignored. Exception that
 * caused the failure is sent as the request response.
 * </ul>
 * 
 * <li>If the {@linkplain ModuleLogicException} is thrown inside the
 * transaction, it is treated as a legitimate rollback request. Pending delayed
 * messages are discarded. Transaction is rolled back - continuations remaining
 * on the stack have their {@link Continuation#failure} invoked (errors during
 * their execution are ignored), transaction listeners are executed (errors are
 * ignored as well), and the post-rollbackphase follows.
 * <ul>
 * <li>If no exception is thrown during the post-rollback, the response messages
 * are sent to the client
 * <li>If an exception is thrown in the post-rollback communication, it is
 * considered an error. Pending delayed messages are discarded and the
 * continuations remaining on the stack have their {@link Continuation#failure}
 * executed. Exceptions during their execution are ignored. Exception that
 * caused the failure is sent as the request response.
 * </ul>
 * 
 * <li>If any other exception occurs inside the transaction, it is considered an
 * error. All pending delayed messages are discarded. Continuations remaining on
 * the stack have their {@linkplain Continuation#failure} invoked, the
 * transaction is rolled back. Post-rollback phase <strong>IS NOT
 * EXECUTED</strong>. The details on the raised exception are sent to the client
 * as the response.
 * </ul>
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
