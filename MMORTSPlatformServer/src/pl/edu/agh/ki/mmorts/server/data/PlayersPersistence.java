package pl.edu.agh.ki.mmorts.server.data;

import javax.inject.Inject;

import org.apache.log4j.Logger;

/**
 * Implementation of {@linkplain PlayersManager}, providing CRUD operations
 * for players and playesr data caching.
 */
public class PlayersPersistence implements PlayersManager {

    private static final Logger logger = Logger.getLogger(PlayersPersistence.class); 
    
    /** Players database access */
    private PlayersDAO players;
    
    @Inject
    public PlayersPersistence(PlayersDAO players) {
        logger.debug("Begin initialization...");
        this.players = players;
        
        logger.debug("Succesfully initialized");
    }
    
}
