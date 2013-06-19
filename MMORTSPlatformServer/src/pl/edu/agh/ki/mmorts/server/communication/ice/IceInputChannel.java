package pl.edu.agh.ki.mmorts.server.communication.ice;

import java.util.Arrays;
import java.util.Collection;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.AMD_Dispatcher_deliver;
import pl.edu.agh.ki.mmorts.server.communication.Response;
import pl.edu.agh.ki.mmorts._DispatcherDisp;
import pl.edu.agh.ki.mmorts.common.ice.Translator;
import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.communication.AbstractChannel;
import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.communication.MessageReceiver;
import pl.edu.agh.ki.mmorts.server.config.MissingRequiredPropertiesException;
import pl.edu.agh.ki.mmorts.server.core.Dispatcher;
import pl.edu.agh.ki.mmorts.server.core.InitException;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import Ice.Current;
import Ice.Identity;

import com.google.inject.name.Named;

/**
 * Concrete {@linkplain Gateway} and {@linkplain Dispatcher} implementation,
 * using Ice.
 * 
 * @author los
 */
public class IceInputChannel extends AbstractChannel {

    private static final Logger logger = Logger.getLogger(IceInputChannel.class);

    /** Name of the property containing desired adapter name */
    private static final String ADAPTER_NAME = "Adapter.Name";

    /** Name of the config property denoting ice communicator args */
    public static final String ICE_ARGS = "sv.dispatcher.ice.args";

    /** Ice argument string */
    @Inject
    @Named(ICE_ARGS)
    private String argString;

    /** Ice object */
    private Ice.Communicator ice;

    /** Ice adapter */
    private Ice.ObjectAdapter adapter;

    /** Servant */
    private DispatcherImpl impl = new DispatcherImpl();

    /**
     * Initialization method creating Ice infrastructure
     */
    @OnInit
    private void init() {
        logger.debug("Begin initialization");
        initIce(getArgs());
        logger.debug("Successfully initialized");
    }

    /**
     * Builds Ice communicator arguments from the data in the configuration
     */
    private String[] getArgs() {
        if (argString != null) {
            return argString.split("\\s+");
        } else {
            throw new MissingRequiredPropertiesException(ICE_ARGS);
        }
    }

    /**
     * Initializes Ice communicator.
     * 
     * @param args
     *            Ice initialization arguments, same as usually given in the
     *            command line
     */
    private void initIce(String[] args) {
        logger.debug("Initializing Ice communicator");
        try {
            ice = Ice.Util.initialize(args);
            logger.debug("Ice communicator initialized");
            String adapterName = getProperty(ADAPTER_NAME);
            adapter = ice.createObjectAdapter(adapterName);
            logger.debug("Ice adapter initialized (" + adapterName + ")");
            Identity id = ice.stringToIdentity("Dispatcher");
            adapter.add(impl, id);
            logger.debug("Servant added");
        } catch (Ice.LocalException e) {
            fatalShutdown(e);
        }
    }

    /**
     * Helper function for shutting down Ice in case of an emergency
     * 
     * @param e Exception that caused the shutdown
     */
    private void fatalShutdown(Ice.LocalException e) {
        logger.fatal("Error while initializing Ice communicator", e);
        try {
            if (ice != null) {
                ice.shutdown();
            }
        } catch (Exception e1) {
            logger.fatal("Cannot shutdown ice after init failure", e1);
        }
        throw new InitException("Ice init failure", e);
    }

    /**
     * Shortcut to ice communicator properties.
     */
    private String getProperty(String prop) {
        return ice.getProperties().getProperty(prop);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>
     * Activates an object adapter created previously during initialization.
     */
    @Override
    public void startReceiving(MessageReceiver receiver) {
        super.startReceiving(receiver);
        try {
            adapter.activate();
            logger.debug("Adapter activated");
        } catch (Ice.LocalException e) {
            fatalShutdown(e);
        }
    }

    /**
     * Actual Ice object used to communicate with the client.
     */
    class DispatcherImpl extends _DispatcherDisp {

        @Override
        public void deliver_async(AMD_Dispatcher_deliver __cb,
                pl.edu.agh.ki.mmorts.Message msg, Current __current) {
            // Forward message to the associated receiver
            forwardMessage(Translator.deiceify(msg), new Resp(__cb));
        }
    }

    /**
     * Implementation of the callback, translating message to Ice format and
     * sending it as a response.
     */
    private class Resp implements Response {

        private final AMD_Dispatcher_deliver __cb;

        private Resp(AMD_Dispatcher_deliver __cb) {
            this.__cb = __cb;
        }

        @Override
        public void send(Collection<Message> messages) {
            __cb.ice_response(Translator.toIceResponse(messages));
        }

        @Override
        public void send(Message... messages) {
            send(Arrays.asList(messages));
        }

        @Override
        public void failed(Exception e) {
            __cb.ice_exception(e);
        }
    }

    /**
     * Shuts down Ice communicator, logs potential errors
     */
    private void shutdownIce() {
        logger.debug("Shutting down Ice communicator");
        try {
            if (ice != null) {
                ice.destroy();
                logger.debug("Ice communicator shut down");
            }
        } catch (Ice.LocalException e) {
            logger.error("Ice error while shutting down Ice communicator", e);
            throw new RuntimeException(e);
        } catch (Exception e) {
            logger.error("Unexpected error while shutting down Ice "
                    + "communicator", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Cleanup method
     */
    @OnShutdown
    public void shutdown() {
        logger.debug("Shutting down");
        shutdownIce();
        logger.debug("Done");
    }

}
