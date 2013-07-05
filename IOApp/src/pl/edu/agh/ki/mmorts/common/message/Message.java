package pl.edu.agh.ki.mmorts.common.message;

import java.io.Serializable;

/**
 * Class representing one logical message between system modules, possibly
 * residing in different dispatchers (like in client-server communication). It
 * is a basic unit of communication. Modules work primarily as message handlers.
 * 
 * <p>
 * The message has the following components:
 * <ul>
 * <li><b>source</b> - address of the component that issued a message, or
 * alternatively - an address to send a reply to. (Note: this two meanings could
 * have been separated to provide for easier message flow control, but were
 * chosen not to, since no significant use cases emerged so far)
 * 
 * <li><b>target</b> - address of the component/multicast group that the message
 * is to be delivered to
 * 
 * <li><b>mode</b> - cardinality of the intended message recipient set (one/many
 * -> unicast/multicast)
 * 
 * <li><b>request</b> - arbitrary string, used to filter and dispatch messages
 * inside the module. It should use lowercase, dash-separated format, e.g.
 * {@code some-fancy-request}. Should be an imperative sentence when describing
 * a request (e.g. {@code create-building}), interrogative when describing a
 * check for some condition (e.g. {@code is-logged}, {@code has-item}), passive
 * for notifications (e.g. {@code item-bought}).
 * 
 * <p>
 * URL-like embedding request parameters is discouraged, since it is inefficient
 * and error-prone. In general, consider using the {@code data} field instead.
 * One exception may be properties getters - the property name may be encoded in
 * the request string for convenience.
 * 
 * <li><b>data</b> - arbitrary Java object to be carried with the message.
 * 
 * </ul>
 * 
 * <p>
 * The message is immutable - it is enforced by the language except for the
 * {@code data} field, which is arbitrary, hence possibly mutable. This fact is
 * of no particular use for this moment, but it is concievable that at some
 * later point system implementors might want to take advantage of this fact,
 * and so using a mutable objects as {@code data} field is discouraged.
 * 
 * <p>
 * The message is intended to be serializable. It is vital for the messages sent
 * across the dispatcher bounds (i.e. by the network) to carry serializable
 * data as a {@code data} field, since network communication uses java
 * serialization. For local-only communication serialization is not required.
 * 
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
     * Checks if the carried value is of the type passed as the argument.
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
     * Convenience method, creates a unicast response for a message - with
     * target as a srouce, source as a target, same conversation id and
     * {@code null} data. Original message must be unicast, otherwise an
     * {@code IllegalArgumentException} is thrown.
     * 
     * @param request
     *            Request string of the response
     * @return New message conforming to a above specification
     */
    public Message response(String request) {
        return response(request, null);
    }

    /**
     * Creates a unicast response for a message - with target as a srouce,
     * source as a target, same conversation id. Original message must be
     * unicast.
     * 
     * @param request
     *            Request string of the response
     * @param data
     *            Data carried in the response
     * @return New message conforming to a above specification
     */
    public Message response(String request, Object data) {
        if (!isUnicast()) {
            throw new IllegalArgumentException("Specify unicast address!");
        }
        return new Message(convId, target, source, Mode.UNICAST, request, data);
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
    public Message response(String src, String request, Object data) {
        return new Message(convId, src, source, Mode.UNICAST, request, data);
    }

    /**
     * Constructor is accessible, but consider using one of the factory methods
     * in {@linkplain Messages} to create instances.
     * 
     * @param convId
     *            Id of a conversation whose part is this particular message
     * @param source
     *            Address of the message source
     * @param target
     *            Target address
     * @param mode
     *            Unicast/multicast
     * @param request
     *            Request string describing the message purpose
     * @param data
     *            Data carried by the message
     * 
     * @throws NullPointerException
     *             If target, source, request or mode are {@code null}
     */
    public Message(int convId, String source, String target, Mode mode,
            String request, Object data) {
        if (target == null) {
            throw new IllegalArgumentException("Target address may not be null");
        }
        if (source == null) {
            throw new IllegalArgumentException("Source address may not be null");
        }
        if (request == null) {
            throw new IllegalArgumentException("Request may not be null");
        }
        if (mode == null) {
            throw new IllegalArgumentException("Mode may not be null");
        }
        this.convId = convId;
        this.source = source;
        this.target = target;
        this.mode = mode;
        this.request = request;
        this.data = data;
    }

    /**
     * Provides a fairly readable string representation of the message. Uses
     * {@code data}'s {@code toString} method. Useful for debugging purpose.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Msg[");
        sb.append("id=").append(convId).append(", ");
        sb.append(source).append(" -> ").append(target);
        sb.append(" (").append(mode).append("), ");
        sb.append("req=").append(request);
        if (data != null) {
            sb.append(", content=[").append(data).append("]");
        }
        sb.append("]");
        return sb.toString();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        } else if (o instanceof Message) {
            Message m = (Message) o;
            return convId == m.convId && source.equals(m.source)
                    && target.equals(m.target) && request.equals(m.request)
                    && mode == m.mode && data == null ? m.data == null : data
                    .equals(m.data);
        } else {
            return false;
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        int result = (data == null ? 0 : data.hashCode());
        result = 31 * result + convId;
        result = 31 * result + source.hashCode();
        result = 31 * result + target.hashCode();
        result = 31 * result + request.hashCode();
        result = 31 * result + mode.hashCode();
        return result;
    }
}
