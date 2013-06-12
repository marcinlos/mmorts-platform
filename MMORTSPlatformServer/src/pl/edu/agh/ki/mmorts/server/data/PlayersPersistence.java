package pl.edu.agh.ki.mmorts.server.data;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;

/**
 * Implementation of {@linkplain PlayersManager}, providing CRUD operations
 * for database and playesr data caching.
 */
public class PlayersPersistence implements PlayersManager {

    private static final Logger logger = Logger.getLogger(PlayersPersistence.class); 
    
    /** Database access */
    private Database database;
    
    private Config config;
    
    @Inject
    public PlayersPersistence(Database database) {
        logger.debug("Begin initialization...");
        this.database = database;
        
        logger.debug("Succesfully initialized");
    }
    
    @Inject
    public void setConfig(Config config) {
        
    }
    
}
