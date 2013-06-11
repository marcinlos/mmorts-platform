package pl.edu.agh.ki.mmorts.server.data;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.core.ModuleContainer;

/**
 * Implementation of {@linkplain PlayersManager}, providing CRUD operations
 * for players and playesr data caching.
 */
public class PlayersPersistence implements PlayersManager {

    /** Players database access */
    private PlayersDAO players;
    
    private static final Logger logger = Logger.getLogger(PlayersPersistence.class); 
    
    public PlayersPersistence() {
        logger.debug("Begin initialization...");
        
        
        logger.debug("Succesfully initialized");
    }
    
    
    void setPlayers(PlayersDAO players) {
        this.players = players;
    }

}
