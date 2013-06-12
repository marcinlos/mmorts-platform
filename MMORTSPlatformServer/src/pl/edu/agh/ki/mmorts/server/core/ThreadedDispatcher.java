package pl.edu.agh.ki.mmorts.server.core;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.communication.Message;
import pl.edu.agh.ki.mmorts.server.communication.MessageChannel;
import pl.edu.agh.ki.mmorts.server.communication.MessageReceiver;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;

/**
 * Default implementation of a message dispatcher.
 */
public class ThreadedDispatcher extends ModuleContainer implements
        MessageReceiver {

    private static final Logger logger = Logger
            .getLogger(ThreadedDispatcher.class);

    /** Configuration object */
    @Inject
    private Config config;

    /** Message service */
    @Inject
    private MessageChannel channel;
    
    private 
    

    @OnInit
    void init() {
        logger.debug("Initializing");
        channel.startReceiving(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void receive(Message message) {
        logger.info("bum: " + message);
    }

    /**
     * Shutdown callback, notifies modules.
     */
    @OnShutdown
    public void shutdown() {
        logger.debug("Shutting down the dispatcher");
        try {
            shutdownModules();
            logger.debug("Dispatcher shat down");
        } catch (Exception e) {
            logger.error("Error while shutting down the communication channel");
            throw new RuntimeException(e);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendTo(Message message, String address) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void send(Message mesage, String category) {
        // TODO Auto-generated method stub

    }

}
