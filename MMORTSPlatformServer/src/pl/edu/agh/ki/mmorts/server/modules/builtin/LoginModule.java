package pl.edu.agh.ki.mmorts.server.modules.builtin;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.communication.Message;
import pl.edu.agh.ki.mmorts.server.data.PlayersManager;
import pl.edu.agh.ki.mmorts.server.modules.Module;

/**
 * Module responsible for receiving login messages, authenticating players and
 * sending back player state when necessary.
 */
public class LoginModule implements Module {
    
    private static final Logger logger = Logger.getLogger(LoginModule.class);
    
    /** Need players manager for data retrieval */
    private PlayersManager players;
    
    private Gateway gateway;

    @Inject
    public LoginModule(PlayersManager players, Gateway gateway) {
        logger.debug("Initializing");
        this.players = players;
        this.gateway = gateway;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void started() {
        // empty
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receive(Message message) {
        logger.debug("Message received");
        logger.debug(message);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void shutdown() {
        logger.debug("Shutting down");
    }

}
