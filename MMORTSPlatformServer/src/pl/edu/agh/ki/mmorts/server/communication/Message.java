package pl.edu.agh.ki.mmorts.server.communication;

/**
 * Abstract message, for now just a filler to make it compile. Can't remamber
 * what it was supposed to contain.
 * 
 * @author los
 */
public interface Message {

    /**
     * @return Address of the message target
     */
    Address getAddress();

    /**
     * @return Message delivery mode
     */
    Mode getMode();
    
    /**
     * @return Messsage content, probably will change to something more useful
     */
    Object getContent();

}
