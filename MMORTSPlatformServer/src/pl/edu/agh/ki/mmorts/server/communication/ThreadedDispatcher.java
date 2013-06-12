package pl.edu.agh.ki.mmorts.server.communication;

import java.util.Arrays;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.core.Dispatcher;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.modules.Module;

/**
 * Default implementation of a message dispatcher.
 */
public class ThreadedDispatcher implements Gateway, Dispatcher, MessageReceiver {

    private static final Logger logger = Logger
            .getLogger(ThreadedDispatcher.class);

    /** Configuration object */
    private Config config;

    /** Message service */
    private MessageChannel channel;

    @Inject
    public ThreadedDispatcher(Config config, MessageChannel channel) {
        logger.debug("Initializing");
        this.config = config;
        this.channel = channel;
        channel.startReceiving(this);
    }

    @Override
    public void registerModules(Module... modules) {
        registerModules(Arrays.asList(modules));
    }

    @Override
    public void receive(Message message) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerModules(Iterable<? extends Module> modules) {
        // TODO Auto-generated method stub

    }

    @Override
    public void registerUnicastReceiver(Module module, String category) {
        // TODO Auto-generated method stub

    }

    /**
     * Shutdown callback, notifies modules.
     */
    @OnShutdown
    public void shutdown() {
        logger.debug("Shutting down the dispatcher");
        try {
            // TODO: Shut down modules
            logger.debug("Dispatcher shat down");
        } catch (Exception e) {
            logger.error("Error while shutting down the communication channel");
            throw new RuntimeException(e);
        }
    }

    @Override
    public void sendTo(Message message, String address) {
        // TODO Auto-generated method stub

    }

    @Override
    public void send(Message mesage, String category) {
        // TODO Auto-generated method stub

    }

}
