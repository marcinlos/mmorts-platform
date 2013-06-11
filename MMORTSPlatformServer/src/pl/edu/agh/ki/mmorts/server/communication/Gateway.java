package pl.edu.agh.ki.mmorts.server.communication;

/**
 * Dispatcher interface for use of the client modules. Allows sending messages
 * with both unicast and multicast target addresses.
 */
public interface Gateway {

    /**
     * Sends a message to an unicast address.
     * 
     * @param message
     *            Message to be delivered
     * @param address
     *            Unicast address of the message intended receiver
     * 
     * @throws NoReceiverException
     *             If the address has no receiver at the target dispatcher
     * @throws CommunicationException
     *             If some other communication error occurs
     */
    void sendTo(Message message, String address);

    /**
     * Sends a message to a multicast group.
     * 
     * @param mesage
     *            Message to be delivered to members of a multicast group
     * @param category
     *            Multicast address of the group to be delivered a message
     * 
     * @throws NoMulticastGroupException
     *             If the address does not identify existing, registered group
     *             at the target dispatcher
     */
    void send(Message mesage, String category);

}
