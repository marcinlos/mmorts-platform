package pl.edu.agh.ki.mmorts.server.core;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.Main;
import pl.edu.agh.ki.mmorts.server.communication.Dispatcher;
import pl.edu.agh.ki.mmorts.server.communication.IceDispatcher;

/**
 * First class to be instantiated in {@link Main#main(String[])}. Responsible
 * for the initialization of the whole server.
 * 
 * <p>Performs roughly the following operations (in order):
 * <ul>
 * <li>Reads configuration files
 * <li>Sets up {@linkplain Dispatcher}
 * <li>Creates & initializes modules, based on the configuration
 * <li>Initializes database connection, creates persistence interfaces
 * </li>
 */
public class Init {
    
    private static final Logger logger = Logger.getLogger(Init.class);
    
    private Dispatcher dispatcher;
    
    
    public Init(String[] args) {
        logger.info("Begin server initialization");
        dispatcher = new IceDispatcher(args);
        
        logger.info("Server successfully initialized");
    }
    
    /**
     * 
     */
    private void readConfig() {
        
    }

}
