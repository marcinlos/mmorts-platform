package pl.edu.agh.ki.mmorts.server.communication;

/**
 * Abstract message, for now just a filler to make it compile. Can't remamber
 * what it was supposed to contain.
 */
public interface Message {

    /**
     * @return Address of the message target
     */
    Address getAddress();
    
    /**
     * @return Address of the message source
     */
    Address getSource();
    
    /**
     * @return Conversation identifier (used e.g. for load balancing)
     */
    int getConversationId();

    /**
     * @return Message delivery mode
     */
    Mode getMode();
    
    /**
     * @return Messsage content, probably will change to something more useful
     */
    Object getContent();

}
