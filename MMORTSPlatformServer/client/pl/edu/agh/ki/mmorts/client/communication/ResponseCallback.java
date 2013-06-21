package pl.edu.agh.ki.mmorts.client.communication;

import pl.edu.agh.ki.mmorts.common.message.MessagePack;

/**
 * Message callback, invoked upon receiving the response for previously sent
 * message.
 * 
 * @author los
 * @see MessageOutputChannel
 */
public interface ResponseCallback {

    /**
     * Invoked when the message was successfully delivered and processed, and an
     * ordinary response has been returned.
     * 
     * @param messages
     *            Messages constituing the response
     */
    void responded(MessagePack messages);

    /**
     * Invoked when the message delivery/processing has failed at some point.
     * 
     * @param e
     *            Exception that occured during message processing
     */
    void failed(Exception e);

}
