package pl.edu.agh.ki.mmorts.server.communication;


/**
 * Interface representing messaging service. Provides the necessary input and
 * output capabilities, necessary for bidirectional communication.
 * 
 * @author los
 * @see MessageReceiver
 */
public interface MessageChannel {

    /**
     * Sets channel's message receiver and begins the receiving process.
     * Provides the input capabilities.
     * 
     * @param receiver
     *            Receiver to which all the incoming messages shall be delivered
     *            for further processing. Should be non-{@code null}.
     */
    void startReceiving(MessageReceiver receiver);

}
