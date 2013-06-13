package pl.edu.agh.ki.mmorts.server.modules.builtin;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.communication.Gateway;
import pl.edu.agh.ki.mmorts.server.communication.Message;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.data.PlayersManager;
import pl.edu.agh.ki.mmorts.server.modules.Context;
import pl.edu.agh.ki.mmorts.server.modules.Module;

import com.google.inject.Inject;

/**
 * Module responsible for receiving login messages, authenticating players and
 * sending back player state when necessary.
 */
public class LoginModule implements Module {
    
    private static final Logger logger = Logger.getLogger(LoginModule.class);
    
    @Inject(optional = true)
    private Config config;
    
    /** Need players manager for data retrieval */
    @Inject(optional = true)
    private PlayersManager players;
    
    @Inject(optional = true)
    private Gateway gateway;
    
    @Inject(optional = true)
    private TransactionManager tm;
    

    public LoginModule(/*PlayersManager players, Gateway gateway*/) {
        logger.debug("Initializing");
//        this.players = players;
//        this.gateway = gateway;
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
    public void receive(Message message, Context ctx) {
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
