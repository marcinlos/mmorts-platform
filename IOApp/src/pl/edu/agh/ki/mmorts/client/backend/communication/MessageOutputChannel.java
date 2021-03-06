package pl.edu.agh.ki.mmorts.client.backend.communication;

import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.common.message.MessagePack;

/**
 * Interface of a message output channel, providing output capabilities
 * necessary to initiate a communication.
 * 
 */
public interface MessageOutputChannel {

    /**
     * Sends the message synchronously, awaits the response.
     * 
     * @param message
     *            Message to be synchronously delivered
     * @return Messages constituing the response
     */
    MessagePack send(Message message);

    /**
     * Sends the message asynchronously, ignores the response. Debug and
     * profiling purpose only.
     * 
     * @param message
     *            Message to be delivered
     */
    void sendAsync(Message message);

    /**
     * Sends the message asynchronously. Upon receiving the response, associated
     * callback is invoked with either returned message list or an exception.
     * 
     * @param message
     *            Message to be delivered
     * @param cb
     *            Callback invoked upon receiving the response
     */
    void sendAsync(Message message, ResponseCallback cb);

}
