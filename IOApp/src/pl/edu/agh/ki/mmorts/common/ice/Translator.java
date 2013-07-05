package pl.edu.agh.ki.mmorts.common.ice;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import pl.edu.agh.ki.mmorts.Response;
import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.common.message.MessagePack;
import pl.edu.agh.ki.mmorts.common.message.Messages;

/**
 * Utility class to translate between Ice messages/responses and corresponding
 * system objects. Contains various convenience methods for converting
 * collections, arrays of messages and {@code Response}s to and from Ice types
 * to system messages.
 * 
 */
public final class Translator {

    private Translator() {
        // non-instantiable
    }

    /**
     * Converts ordinary {@linkplain Message} to Ice Message structure, i.e.
     * {@linkplain pl.edu.agh.ki.mmorts.Message}.
     * 
     * @param msg
     *            Message to convert
     * @return Ice message representing serialized {@code msg}
     */
    public static pl.edu.agh.ki.mmorts.Message iceify(Message msg) {
        return new pl.edu.agh.ki.mmorts.Message(msg.convId,
                Messages.toBytes(msg));
    }

    /**
     * Converts a collection of {@linkplain Message} (i.e. messages used by the
     * system core) to an array of Ice messages. It can be used to construct
     * response object in the server side message channel.
     * 
     * @param msgs
     *            Collection of messages to convert
     * @return Array of messages converted to the Ice format
     */
    public static pl.edu.agh.ki.mmorts.Message[] iceify(Collection<Message> msgs) {
        pl.edu.agh.ki.mmorts.Message[] iceMsgs;
        iceMsgs = new pl.edu.agh.ki.mmorts.Message[msgs.size()];
        int i = 0;
        for (Message msg : msgs) {
            iceMsgs[i++] = iceify(msg);
        }
        return iceMsgs;
    }

    /**
     * Converts a collection of system messages to an Ice {@code Response}
     * structure.
     * 
     * @param ver
     *            Version of the application
     * @param msgs
     *            Collection of messages constituting the response
     * @return Ice {@code Response} structure
     */
    public static Response toIceResponse(int ver, Collection<Message> msgs) {
        return new Response(ver, iceify(msgs));
    }

    /**
     * Converts an array of system messages to an Ice {@code Response}
     * structure.
     * 
     * @param ver
     *            Version of the application
     * 
     * @param msgs
     *            Collection of messages constituting the response
     * @return Ice {@code Response} structure
     */
    public static Response toIceResponse(int ver, Message[] msgs) {
        return toIceResponse(ver, Arrays.asList(msgs));
    }

    /**
     * Converts Ice {@linkplain Message} to ordinary
     * {@linkplain pl.edu.agh.ki.mmorts.common.message.Message}.
     * 
     * @param msg
     *            Ice message to convert
     * @return Ordinary message
     */
    public static Message deiceify(pl.edu.agh.ki.mmorts.Message iceMsg) {
        return Messages.fromBytes(iceMsg.content);
    }

    /**
     * Converts an array of Ice messages to a list of messages in the format
     * used by the rest of the system.
     * 
     * @param iceMsgs
     *            Array of Ice messages
     * @return {@linkplain MessagePack} containing a sequence of system messages
     */
    public static MessagePack deicefy(int ver,
            pl.edu.agh.ki.mmorts.Message[] iceMsgs) {
        List<Message> msgs = new ArrayList<Message>(iceMsgs.length);
        for (pl.edu.agh.ki.mmorts.Message iceMsg : iceMsgs) {
            msgs.add(deiceify(iceMsg));
        }
        return new MessagePack(ver, msgs);
    }

    /**
     * Converts an Ice {@linkplain Response} object to a collection of system
     * messages.
     * 
     * @param response
     *            Ice {@code Response} structure
     * @return {@linkplain MessagePack} containing a sequence of system messages
     */
    public static MessagePack fromIceResponse(Response response) {
        return deicefy(response.version, response.messages);
    }
}
