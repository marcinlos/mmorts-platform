package pl.edu.agh.ki.mmorts.client.communication;

import java.util.List;

import pl.edu.agh.ki.mmorts.common.message.Message;

/**
 * Interface of a message output channel, providing output capabilities
 * necessery to initate a communication.
 * 
 * @author los
 */
public interface MessageOutputChannel {

    /**
     * Sends the message synchronously, awaits the response.
     * 
     * @param message
     *            Message to be synchronously delivered
     * @return Messages constituing the response
     */
    List<Message> send(Message message);

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
