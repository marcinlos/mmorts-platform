package pl.edu.agh.ki.mmorts.server.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import com.google.inject.name.Named;

import pl.edu.agh.ki.mmorts.common.message.Message;
import pl.edu.agh.ki.mmorts.server.communication.Response;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;

public class ThreadedDispatcher extends AbstractDispatcher {

    private static final Logger logger = Logger
            .getLogger(ThreadedDispatcher.class);
    
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

    /**
     * Thread-local transaction executor
     */
    private static final ThreadLocal<TransactionExecutor> executor = new ThreadLocal<TransactionExecutor>() {
        @Override
        protected TransactionExecutor initialValue() {
            return new TransactionExecutor();
        }
    };

    /**
     * @return Current transaction executor
     */
    protected TransactionExecutor executor() {
        return executor.get();
    }
    
    /**
     * Creates the thread pool and initializes the communication
     */
    @OnInit
    @Override
    protected void init() {
        logger.debug("Initializing");
        createThreadPool();
        super.init();
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
     * Orders thread pool shutdown and waits for the messages being processed.
     */
    @Override
    protected void onShutdown() {
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
    public void receive(final Message message, final Response response) {
        logger.debug("Message received: \n" + message);
        // async execute
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                dispatchMessage(message, response);
            }
        });
        logger.debug("Message submitted to the thread pool");
    }

}
