package pl.edu.agh.ki.mmorts.server.data;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * Implementation of {@linkplain PlayersManager}, providing CRUD operations
 * for database and playesr data caching.
 */
public class PlayersPersistence implements PlayersManager {

    private static final Logger logger = Logger.getLogger(PlayersPersistence.class); 
    
    /** Database access */
    private Database database;
    
    @Inject
    public PlayersPersistence(Database database) {
        logger.debug("Begin initialization...");
        this.database = database;
        
        logger.debug("Succesfully initialized");
    }
    
}
