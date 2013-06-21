package pl.edu.agh.ki.mmorts.server.communication;

import java.util.Collection;

import pl.edu.agh.ki.mmorts.common.message.Message;

/**
 * Simple callback interface providing a way to response to a request in a fully
 * asynchronous manner.
 * 
 * @author los
 * @see MessageInputChannel
 */
public interface Response {

    /**
     * Sends a sequence of messages through the output channel to the source of
     * the original message that initiated the communication.
     * 
     * @param version
     *            Version of the server
     * 
     * @param messages
     *            Messages to send as a response
     * @throws AlreadyRespondedException
     *             When the method is invoked second time or more
     */
    void send(int version, Message... messages);

    /**
     * Sends a sequence of messages through the output channel to the source of
     * the original message that initiated the communication.
     * 
     * @param version
     *            Version of the server
     * @param messages
     *            Messages to send as a response
     * @throws AlreadyRespondedException
     *             When the method is invoked second time or more
     */
    void send(int version, Collection<Message> messages);

    /**
     * Sends an exception as a response.
     * 
     * @param e
     *            Exception to be sent as a response
     */
    void failed(Exception e);

}
