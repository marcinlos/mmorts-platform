package pl.edu.agh.ki.mmorts.common.message;

import java.util.List;

public class MessagePack {
    
    /** Version of the application */
    public final int version;
    
    /** Sequence of messages */
    public final List<Message> messages;

    public MessagePack(int version, List<Message> messages) {
        this.version = version;
        this.messages = messages;
    }


}
