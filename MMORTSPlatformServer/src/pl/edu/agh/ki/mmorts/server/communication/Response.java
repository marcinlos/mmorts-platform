package pl.edu.agh.ki.mmorts.server.communication;

import pl.edu.agh.ki.mmorts.common.message.Message;

/**
 * Simple callback interface providing a way to response to a request in a fully
 * asynchronous manner.
 * 
 * @see MessageChannel
 */
public interface Response {

    /**
     * Sends a sequence of messages through the output channel to the source of
     * the original message that initiated the communication.
     * 
     * @param messages
     *            Messages to send as a response
     * @throws AlreadyRespondedException
     *             When the method is invoked second time or more
     */
    void send(Message... messages);

}
