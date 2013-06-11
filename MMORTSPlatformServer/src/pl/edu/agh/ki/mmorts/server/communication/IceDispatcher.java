package pl.edu.agh.ki.mmorts.server.communication;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.core.Dispatcher;
import pl.edu.agh.ki.mmorts.server.modules.Module;

/**
 * Concrete {@linkplain Gateway} and {@linkplain Dispatcher} implementation, using
 * Ice.
 * 
 * @author los
 */
public class IceDispatcher extends AbstractDispatcher {
    
    private static final Logger logger = Logger.getLogger(IceDispatcher.class);

    /** Ice object */
    private Ice.Communicator ice;
    
    
    public IceDispatcher(String[] args) {
        logger.debug("Begin initialization");
        initIce(args);
        logger.debug("Successfully initialized");
    }

    
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
    

    private void shutdownIce() {
        logger.debug("Shutting down Ice communicator");
        try {
            if (ice != null) {
                ice.shutdown();
            }
        } catch (Ice.LocalException e) {
            logger.error("Error while shutting down Ice communicator", e);
        }
        
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void registerModules(Module... modules) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void registerModules(Iterable<? extends Module> modules) {
        // TODO Auto-generated method stub
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        logger.debug("Shutting down");
        shutdownIce();
        logger.debug("Done");
    }

}
