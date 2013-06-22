package pl.edu.agh.ki.mmorts.common.message;

import java.util.List;

/**
 * Message pack sent by the server to the client as a response. Contains
 * application version to let the client know about potential version changes
 * caused by runtime module manipulation, and the message sequence constituting
 * the actual response content.
 * 
 * @author los
 */
public class MessagePack {

    /** Version of the application */
    public final int version;

    /** Sequence of messages */
    public final List<Message> messages;

    /**
     * Creates a message pack using a version number and the message list
     * 
     * @param version Server version number
     * @param messages Message list
     */
    public MessagePack(int version, List<Message> messages) {
        this.version = version;
        this.messages = messages;
    }

}
