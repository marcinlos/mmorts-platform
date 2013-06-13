package pl.edu.agh.ki.mmorts.server.data;

import java.io.IOException;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;

/**
 * Mock implementation of players database access
 */
public class PlayersInMemory implements Database {
    
    private static final Logger logger = Logger.getLogger(PlayersInMemory.class);

    @Inject
    private Config config;
    
    public PlayersInMemory() {
        logger.debug("Initializing in-memory database");
    }

    @OnShutdown
    public void close() throws IOException {
        logger.debug("Closing");
    }

}
