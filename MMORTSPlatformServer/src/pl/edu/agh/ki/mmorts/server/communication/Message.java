package pl.edu.agh.ki.mmorts.server.communication;

/**
 * Abstract message, for now just a filler to make it compile. Can't remamber
 * what it was supposed to contain.
 * 
 * @author los
 */
public interface Message {

    /**
     * @return Address (unicast address or multicast group identifier) of the
     *         message target
     */
    String getAddress();

    /**
     * @return Messsage content, probably will change to something more useful
     */
    Object getContent();

}
