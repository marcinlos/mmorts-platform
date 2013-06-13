package pl.edu.agh.ki.mmorts.common.message;

import java.io.Serializable;

/**
 * Abstract message, for now just a filler to make it compile. Can't remamber
 * what it was supposed to contain.
 * 
 * <p>
 * Most likely it will be passed around using Java's native serialization.
 */
public interface Message extends Serializable {

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
     * @return Textual type identifier (e.g. "authenticate-request")
     */
    String getType();

    /**
     * @return Message delivery mode
     */
    Mode getMode();

    /**
     * @return Messsage content, probably will change to something more useful
     */
    Object getContent();

}
