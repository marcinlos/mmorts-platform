package pl.edu.agh.ki.mmorts.common.message;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInput;
import java.io.DataInputStream;
import java.io.DataOutput;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class Messages {

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
     * Creates a new multicast message from the given data
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

    public static byte[] toBytes(Message message) {
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            DataOutput dataOut = new DataOutputStream(out);
            dataOut.writeInt(message.convId);
            dataOut.writeUTF(message.source);
            dataOut.writeUTF(message.target);
            dataOut.writeInt(message.mode.ordinal());
            dataOut.writeUTF(message.request);
            ObjectOutput objOut = new ObjectOutputStream(out);
            objOut.writeObject(message.data);
            return out.toByteArray();
        } catch (IOException e) {
            // Probably non-serializability
            throw new RuntimeException(e);
        }
    }
    
    public static Message fromBytes(byte[] bytes) {
        try {
            ByteArrayInputStream out = new ByteArrayInputStream(bytes);
            DataInput dataOut = new DataInputStream(out);
            int convId = dataOut.readInt();
            String source = dataOut.readUTF();
            String target = dataOut.readUTF();
            Mode mode = Mode.fromInt(dataOut.readInt());
            String request = dataOut.readUTF();
            ObjectInput objIn = new ObjectInputStream(out);
            Object data = objIn.readObject();
            return new Message(convId, source, target, mode, request, data);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new RuntimeException("Invalid binary message", e);
        } catch (IOException e) {
            // Shouldn't happen
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

}
