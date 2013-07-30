package pl.edu.agh.ki.mmorts.client.backend.communication.ice;

import pl.edu.agh.ki.mmorts.Callback_Dispatcher_deliver;
import pl.edu.agh.ki.mmorts.DispatcherPrx;
import pl.edu.agh.ki.mmorts.DispatcherPrxHelper;
import pl.edu.agh.ki.mmorts.Response;
import pl.edu.agh.ki.mmorts.client.backend.common.ice.Translator;
import pl.edu.agh.ki.mmorts.client.backend.common.message.Message;
import pl.edu.agh.ki.mmorts.client.backend.common.message.MessagePack;
import pl.edu.agh.ki.mmorts.client.backend.communication.MessageOutputChannel;
import pl.edu.agh.ki.mmorts.client.backend.communication.ResponseCallback;
import Ice.LocalException;
import Ice.ObjectPrx;


/**
 * Example implementation of the client side message source.
 * 
 */
public class IceOutputChannel implements MessageOutputChannel {

    /** Ice main object */
    private Ice.Communicator ice;

    /** Proxy of the server dispatcher */
    private DispatcherPrx dispatcher;

    /**
     * Creates a new {@code IceOutputChannel} based on the string arguments in the
     * format as in the command line invocation.
     * 
     * @param args
     */
    public IceOutputChannel(Ice.Properties iceProperties) {
    	Ice.InitializationData id = new Ice.InitializationData();
    	id.properties = iceProperties;
    	ice = Ice.Util.initialize(id);
        // get the dispatcher - hardcoded property name
        String str = ice.getProperties().getProperty("MMORTSServer.Proxy");
        System.out.println("@@@@@@@@@-->" + str);
        ObjectPrx obj = ice.stringToProxy(str);
        System.out.print("Obtaining server reference...");
        System.out.flush();
        
        try {
            dispatcher = DispatcherPrxHelper.checkedCast(obj);
            // if cast fails, the result is null - no exception!
            if (dispatcher == null) {
                System.err.println("Failed to obtain dispatcher reference");
                //TODO: TEMPORARILY!!!
                System.exit(1);
            }
            System.out.println("done");
        } catch (Ice.ConnectionRefusedException e) {
            System.out.println("\nConnection refused, is server running?");
            
            //TODO: TEMPORARILY!!!
            
            shutdown();
            throw new RuntimeException(e);
        }
        
        
        
    }

    /**
     * Shutdown & cleanup method
     */
//    @OnShutdown
    public void shutdown() {
        try {
            // deactivate the object
            System.out.println("Shutting down Ice channel");
            ice.destroy();
        } catch (Ice.LocalException e) {
            System.err.println("Error while shutting down Ice communicator");
            System.err.println(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public MessagePack send(Message message) {
        Response response = dispatcher.deliver(Translator.iceify(message));
        return Translator.fromIceResponse(response);
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAsync(Message message) {
        dispatcher.begin_deliver(Translator.iceify(message));
    }
    
    /**
     * {@inheritDoc}
     */
    @Override
    public void sendAsync(Message message, final ResponseCallback cb) {
        dispatcher.begin_deliver(Translator.iceify(message), new Callback(cb));
    }

    /**
     * Auxilary class implementing the AMI callback interface
     */
    private static final class Callback extends Callback_Dispatcher_deliver {
        private final ResponseCallback cb;

        private Callback(ResponseCallback cb) {
            this.cb = cb;
        }

        @Override
        public void exception(LocalException __ex) {
            cb.failed(__ex);
        }

        @Override
        public void response(Response __ret) {
            cb.responded(Translator.fromIceResponse(__ret));
        }
    }

}
