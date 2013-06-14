package pl.edu.agh.ki.mmorts.common.ice;

import pl.edu.agh.ki.mmorts.Message;
import pl.edu.agh.ki.mmorts.common.message.Messages;

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
        return new Message(msg.convId, Messages.toBytes(msg));
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
        return Messages.fromBytes(msg.content);
    }
}
