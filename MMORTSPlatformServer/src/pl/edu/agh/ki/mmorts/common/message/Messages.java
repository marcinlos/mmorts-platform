package pl.edu.agh.ki.mmorts.common.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.NotSerializableException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.OutputStream;

/**
 * Helper class for creation and serialization/deserialization of system message
 * objects.
 */
public final class Messages {

    private Messages() {
        // non-instantiable
    }

    /**
     * Creates a new unicast message from the given parameters
     */
    public static Message unicast(int convId, String source, String target,
            String request, Object data) {
        return new Message(convId, source, target, Mode.UNICAST, request, data);
    }

    /**
     * Creates a new unicast message from the given parameters, with
     * {@code null} data.
     */
    public static Message unicast(int convId, String source, String target,
            String request) {
        return new Message(convId, source, target, Mode.UNICAST, request, null);
    }

    /**
     * Creates a new multicast message carrying the given data
     */
    public static Message multicast(int convId, String source, String target,
            String request, Object data) {
        return new Message(convId, source, target, Mode.MULTICAST, request,
                data);
    }

    /**
     * Creates a new unicast message from the given parameters, with
     * {@code null} data.
     */
    public static Message multicast(int convId, String source, String target,
            String request) {
        return new Message(convId, source, target, Mode.MULTICAST, request,
                null);
    }

    /**
     * Serializes the message and writes to to an output stream. If the
     * serialization fails, message is partially written to a stream - no
     * atomicity guarantees are provided.
     * 
     * @param out
     *            Output stream to serialize the message to
     * @param message
     *            Messge to be serialized
     * @throws NonSerializableContentException
     *             If the message content cannot be java-serialized
     * @throws MessageSerializationException
     *             If other problem occurs during writing to stream
     * @see #toBytes(Message)
     */
    public static void writeTo(OutputStream out, Message message) {
        try {
            DataOutput dataOut = new DataOutputStream(out);
            dataOut.writeInt(message.convId);
            dataOut.writeUTF(message.source);
            dataOut.writeUTF(message.target);
            dataOut.writeInt(message.mode.ordinal());
            dataOut.writeUTF(message.request);
            ObjectOutput objOut = new ObjectOutputStream(out);
            objOut.writeObject(message.data);
        } catch (NotSerializableException e) {
            throw new NonSerializableContentException(e);
        } catch (IOException e) {
            // Other serialization-related exception
            throw new MessageSerializationException(e);
        }
    }

    /**
     * Converts the message to an array of bytes, using custom format for the
     * message-specific elements and plain old java serialisation for the
     * generic data carried by the component.
     * 
     * @param message
     *            Message to be serialized
     * @return Byte array containing serialized message
     * @throws NonSerializableContentException
     *             If the message content cannot be java-serialized
     * @throws MessageSerializationException
     *             If other problem occurs during message serialization
     * @see #writeTo(OutputStream, Message)
     */
    public static byte[] toBytes(Message message) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        writeTo(out, message);
        return out.toByteArray();
    }

    /**
     * Reads a serialized message from the input stream.
     * 
     * @param in
     *            Input stream to read serialized message data from
     * @return Deserialized {@linkplain Message}
     * @throws InvalidBinaryFormatException
     *             If the data provided by the byte stream is not a valid
     *             serialized message
     * @throws MessageSerializationException
     *             If some other problem occurs during the deserialization
     */
    public static Message readFrom(InputStream in) {
        try {
            DataInput dataIn = new DataInputStream(in);
            // convers
            int convId = dataIn.readInt();
            String source = dataIn.readUTF();
            String target = dataIn.readUTF();
            int modeInt = dataIn.readInt();
            if (modeInt < 0 || modeInt >= Mode.values().length) {
                throw new InvalidBinaryFormatException("Invalid mode ordinal ("
                        + modeInt + ")");
            }
            Mode mode = Mode.values()[modeInt];
            String request = dataIn.readUTF();
            ObjectInput objIn = new ObjectInputStream(in);
            Object data = objIn.readObject();
            return new Message(convId, source, target, mode, request, data);
        } catch (EOFException e) {
            throw new InvalidBinaryFormatException("Binary data too short", e);
        } catch (IOException e) {
            throw new InvalidBinaryFormatException(e);
        } catch (ClassNotFoundException e) {
            throw new MessageSerializationException("Invalid data class", e);
        }
    }

    /**
     * Converts an array of bytes representing some serialized message into this
     * message.
     * 
     * @param bytes
     *            Byte array with the serialized message
     * @return Deserialized {@linkplain Message}
     * @throws InvalidBinaryFormatException
     *             If the data provided by the byte stream is not a valid
     *             serialized message
     * @throws MessageSerializationException
     *             If some other problem occurs during the deserialization
     * @see #readFrom(InputStream)
     */
    public static Message fromBytes(byte[] bytes) {
        ByteArrayInputStream in = new ByteArrayInputStream(bytes);
        return readFrom(in);
    }

}
