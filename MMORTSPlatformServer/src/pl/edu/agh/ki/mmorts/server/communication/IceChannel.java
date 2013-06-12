package pl.edu.agh.ki.mmorts.server.communication;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.agh.edu.ki.mmorts.server.config.MissingRequiredPropertiesException;
import pl.edu.agh.ki.mmorts.server.core.Dispatcher;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;

/**
 * Concrete {@linkplain Gateway} and {@linkplain Dispatcher} implementation,
 * using Ice.
 * 
 * @author los
 */
public class IceChannel extends AbstractChannel {

    private static final Logger logger = Logger.getLogger(IceChannel.class);

    /** Name of the config property denoting ice communicator args */
    public static final String ICE_ARGS = "sv.dispatcher.ice.args";

    /** Ice object */
    private Ice.Communicator ice;

    private Config config;

    /**
     * Constructor used to inject configuration.
     * 
     * @param config
     *            Injected configuration
     */
    @Inject
    public IceChannel(Config config) {
        logger.debug("Begin initialization");
        this.config = config;
        initIce(getArgs());
        logger.debug("Successfully initialized");
    }

    /**
     * Builds Ice communicator arguments from the data in the configuration
     */
    private String[] getArgs() {
        String argString = config.getString(ICE_ARGS);
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
        } catch (Ice.LocalException e) {
            logger.fatal("Error while initializing Ice communicator", e);
            try {
                if (ice != null) {
                    ice.shutdown();
                }
            } catch (Exception e1) {
                logger.fatal("Cannot shutdown ice after init failure", e1);
            }
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


    /**
     * {@inheritDoc}
     * 
     * TODO: Establish some protocol and actually DO SOMETHING
     */
    @Override
    public void sendMessage(Message message) {
        logger.debug("Message sent: " + message);
        // ????
    }

}
