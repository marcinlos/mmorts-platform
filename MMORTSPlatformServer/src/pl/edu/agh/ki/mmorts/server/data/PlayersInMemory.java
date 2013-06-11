package pl.edu.agh.ki.mmorts.server.data;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;

/**
 * Mock implementation of players database access
 */
public class PlayersInMemory implements Database {
    
    private static final Logger logger = Logger.getLogger(PlayersInMemory.class);

    private Config config;
    
    @Inject
    public PlayersInMemory(Config config) {
        logger.debug("Initializing in-memory database");
        this.config = config;
    }

}
