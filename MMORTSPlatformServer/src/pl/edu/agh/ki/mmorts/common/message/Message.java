package pl.edu.agh.ki.mmorts.common.message;

import java.io.Serializable;

/**
 * Message passed around between modules and on the server <---> client line.
 */
public class Message implements Serializable {

    /** Identifier of the conversation */
    public final int convId;

    /** Source of the message */
    public final String source;

    /** Address of the target */
    public final String target;

    /** Mode of message delivery */
    public final Mode mode;

    /** Request - kind of like URL, identifies the operation */
    public final String request;

    /** Data carried with the message */
    public final Object data;

    /**
     * Type-safe method to retrieve data carried by the message. Checks whether
     * carried data is of the type {@code clazz}, and if so returns the data.
     * Otherwise, it returns {@code null}.
     * 
     * @param clazz
     *            Class of the message data
     * @return Carried data if the type matches, false otherwise
     */
    public <T> T get(Class<T> clazz) {
        if (carries(clazz)) {
            return clazz.cast(data);
        } else {
            return null;
        }
    }

    /**
     * Checks if the carried type is the one passed as the argument.
     * 
     * @param clazz
     *            Class to check carried data against
     * @return {@code true} if the types match, {@code false} otherwise
     */
    public <T> boolean carries(Class<T> clazz) {
        return clazz.isInstance(data);
    }

    /**
     * @return {@code true} if the message is unicast
     */
    public boolean isUnicast() {
        return mode == Mode.UNICAST;
    }

    /**
     * @return {@code true} if the message is multicast
     */
    public boolean isMulticast() {
        return mode == Mode.MULTICAST;
    }

    /**
     * Creates a unicast response for a message - with target as a srouce,
     * source as a target, same conversation id and {@code null} data. Original
     * message must be unicast.
     * 
     * @param newRequest
     *            Request string of the response
     * @return New message conforming to a above specification
     */
    public Message response(String newRequest) {
        return response(newRequest, null);
    }

    /**
     * Creates a unicast response for a message with a specific source address,
     * source as a target, same conversation id and {@code null} data.
     * 
     * @param src
     *            Source of the response
     * 
     * @param newRequest
     *            Request string of the response
     * @return New message conforming to a above specification
     */
    public Message response(String src, String newRequest) {
        return response(src, newRequest, null);
    }

    /**
     * Creates a unicast response for a message - with target as a srouce,
     * source as a target, same conversation id. Original message must be
     * unicast.
     * 
     * @param newRequest
     *            Request string of the response
     * @param newData
     *            Data carried in the response
     * @return New message conforming to a above specification
     */
    public Message response(String newRequest, Object newData) {
        if (mode != Mode.UNICAST) {
            throw new IllegalArgumentException("Specify unicast address!");
        }
        return new Message(convId, target, source, Mode.UNICAST, newRequest,
                newData);
    }

    /**
     * Creates a unicast response for a message with a specific source address,
     * source as a target, same conversation id and {@code null} data.
     * 
     * @param src
     *            Source of the response
     * @param newRequest
     *            Request string of the response
     * @param newData
     *            Data carried in the response
     * @return New message conforming to a above specification
     */
    public Message response(String src, String newRequest, Object newData) {
        return new Message(convId, src, source, Mode.UNICAST, newRequest,
                newData);
    }

    /**
     * Constructor is accessible, but consider using one of the factory methods
     * in {@linkplain Messages} to create instances.
     */
    public Message(int convId, String source, String target, Mode mode,
            String request, Object data) {
        if (target == null) {
            throw new NullPointerException("Target address may not be null");
        }
        this.convId = convId;
        this.source = source;
        this.target = target;
        this.mode = mode;
        this.request = request;
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Msg[");
        return sb.append("id=").append(convId).append(", ").append(source)
                .append(" -> ").append(target).append(" (").append(mode)
                .append("), request=").append(request).append(", content=[")
                .append(data).append("]").append("]").toString();
    }

}
