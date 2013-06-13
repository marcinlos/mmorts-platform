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
import pl.edu.agh.ki.mmorts.server.communication.ServiceLocator;
import pl.edu.agh.ki.mmorts.server.communication.ServiceLocatorDelgate;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.modules.Context;
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
    
    /** Transaction manager */
    @Inject
    private TransactionManager tm;

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
    
    /** Implementation of service locator */
    private ServiceLocator services = new ServiceLocatorDelgate();

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
        String details = messageDetails(message);
        logger.debug("Message received: \n" + details);
        
        // begin message transaction
        Context ctx = new Context();
        tm.begin();
        try {
            // realize transaction
            tm.commit();
        } catch (Exception e) {
            logger.debug("Transaction rolled back due to an exception", e);
            tm.rollback();
        }
    }

    /**
     * Produces a stringized message representation
     */
    private static String messageDetails(Message message) {
        StringBuilder sb = new StringBuilder();
        sb.append("\tTarget: " + message.getAddress()).append('\n')
          .append("\tSource: " + message.getSource()).append('\n');
        return sb.toString();
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
    public void send(Message message) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void sendDelayed(Message mesage) {
        // TODO Auto-generated method stub

    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void later(Continuation cont) {
        // TODO Auto-generated method stub
        
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Delegates to the {@link ServiceLocatorDelgate}
     */
    @Override
    public <T> void register(Class<? super T> service, T provider) {
        services.register(service, provider);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Delegates to the {@link ServiceLocatorDelgate}
     */
    @Override
    public <T> void registerIfAbsent(Class<? super T> service, T provider) {
        services.registerIfAbsent(service, provider);
    }

    /**
     * {@inheritDoc}
     * 
     * <p>Delegates to the {@link ServiceLocatorDelgate}
     */
    @Override
    public <T> T lookup(Class<T> service) {
        return services.lookup(service);
    }


}
