package pl.edu.agh.ki.mmorts.common.message;

public class Messages {


    private Messages() {
        // non-instantiable
    }
    
    public static MessageImpl create(Address target, Address source,
            int convId, String type, Mode mode, Object content) {
        return new MessageImpl(target, source, convId, type, mode, content);
    }
    

}
