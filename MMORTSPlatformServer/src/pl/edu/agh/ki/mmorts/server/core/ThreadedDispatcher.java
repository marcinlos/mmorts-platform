package pl.edu.agh.ki.mmorts.server.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.agh.edu.ki.mmorts.server.config.Config;
import pl.edu.agh.ki.mmorts.server.communication.Message;
import pl.edu.agh.ki.mmorts.server.communication.MessageChannel;
import pl.edu.agh.ki.mmorts.server.communication.MessageReceiver;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.modules.Continuation;

import com.google.inject.name.Named;

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

    @Inject
    @Named("sv.dispatcher.threads.init")
    private int threadsInit;

    @Inject
    @Named("sv.dispatcher.threads.max")
    private int threadsMax;

    @Inject
    @Named("sv.dispatcher.threads.keepalive")
    private int keepalive;

    /** Thread pool used to handle incoming messages */
    private ExecutorService threadPool;

    @OnInit
    void init() {
        logger.debug("Initializing");
        createThreadPool();
        channel.startReceiving(this);
    }

    /**
     * Initializes the thread pool used to dispatch messages
     */
    private void createThreadPool() {
        String msg = String.format(
                "Initializing the pool (init=%d, max=%d, keepalive=%d s)",
                threadsInit, threadsMax, keepalive);
        logger.debug(msg);
        threadPool = new ThreadPoolExecutor(threadsInit, threadsMax, keepalive,
                TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
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
            terminateTheadPool();
            shutdownModules();
            logger.debug("Dispatcher shat down");
        } catch (Exception e) {
            logger.error("Error while shutting down the communication channel");
            throw new RuntimeException(e);
        }
    }

    /**
     * Orders thread pool shutdown and waits for the messages being processed.
     */
    private void terminateTheadPool() {
        logger.debug("Waiting for pending messages to finish");
        threadPool.shutdown();
        try {
            // Arbitrary value, seems to be of no particular importance
            threadPool.awaitTermination(5, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            logger.warn("Interrupted while awaiting thread pool termination");
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

    /**
     * {@inheritDoc}
     */
    @Override
    public void later(Continuation cont) {
        // TODO Auto-generated method stub
        
    }

}
