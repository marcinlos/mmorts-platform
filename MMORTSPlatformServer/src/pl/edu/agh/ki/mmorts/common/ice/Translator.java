package pl.edu.agh.ki.mmorts.common.ice;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import pl.agh.edu.ki.mmorts.Message;

/**
 * Utility class to translate between Ice messages/responses and corresponding
 * system objects.
 */
public class Translator {

    private Translator() {
        // non-instantiable
    }

    /**
     * Converts ordinary {@link pl.edu.agh.ki.mmorts.common.message.Message} to
     * Ice {@link Message}.
     * 
     * @param msg
     *            Message to convert
     * @return Ice message representing serialized {@code msg}
     */
    public static Message iceify(pl.edu.agh.ki.mmorts.common.message.Message msg) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ObjectOutput objOut = new ObjectOutputStream(out);
            objOut.writeObject(msg);
            return new Message(msg.getConversationId(), out.toByteArray());
        } catch (IOException e) {
            throw new Error("Shouldn't happen");
        }
    }

    /**
     * Converts Ice {@link Message} to ordinary
     * {@link pl.edu.agh.ki.mmorts.common.message.Message}.
     * 
     * @param msg
     *            Ice message to convert
     * @return Ordinary message
     */
    public static pl.edu.agh.ki.mmorts.common.message.Message deiceify(
            Message msg) {
        try {
            ByteArrayInputStream in = new ByteArrayInputStream(msg.content);
            ObjectInput objIn = new ObjectInputStream(in);
            return (pl.edu.agh.ki.mmorts.common.message.Message) objIn
                    .readObject();
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Invalid Message format", e);
        } catch (IOException e) {
            throw new Error("Shouldn't happen");
        }
    }
}
