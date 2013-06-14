package pl.edu.agh.ki.mmorts.server.data;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

import javax.inject.Inject;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.core.Init;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;



/**
 * Class which provides simple connection pooling for custom databases. Class is
 * thread-safe and provides managing of maximum allowed connections 'as is'. It
 * could be extended by other to manage other pooling features. Initialization
 * of connection is resolved lazily during taking one.
 * 
 * @see SimpleConnectionPool#getConnection()
 * 
 * @author drew
 * 
 */
public class SimpleConnectionPool {
    private static final Logger logger = Logger
            .getLogger(SimpleConnectionPool.class);

    @Inject
    @Named("db.maxConnections")
    private int maxConnections;
    
    
    private int createdConnections;
    private BlockingQueue<Connection> connectionPool;

    @Inject
    private ConnectionCreator creator;
    
    public SimpleConnectionPool() { }

    /**
     * Creates a connection with given size and using custom creator. However
     * creator has to provide JDBC Connection. It's worthy to note here, that
     * creator's method of creating connections is going to be call {@code maxConnections}
     * times.
     * 
     * @param maxPoolSize
     *          size of pool
     * @param creator
     *          creator of connection
     */   
    @OnInit
    public void init(){
        logger.debug("Connection pool initialization started");
        connectionPool = new ArrayBlockingQueue<Connection>(maxConnections, true);
        logger.debug("Connection pool initialization ended succesfully");
    }

    /**
     * Getting one connection from the pool. The strategy of creating pool is
     * not as production's should be now. During each of the getting the new
     * connection is initialized if there is a place for new one(e.g. weren't
     * all connections initialized earlier).
     * <p>
     * This method blocks if there is no connections in pool!
     * </p>
     * 
     * @return free connection to work on
     * @throws Exception
     *             //TODO!!
     */
    public synchronized Connection getConnection() throws NoConnectionException{
        logger.debug("Getting connection");
        if (createdConnections < maxConnections) {
            logger.debug("Adding new connection to pool");
            try {
                connectionPool.add(creator.createConnection());
                ++maxConnections;
            } catch (SQLException e) {

                if (createdConnections == 0) {
                    throw new NoConnectionException();
                } else {
                    String warnMessage = String
                            .format("Cannot create connection. However, %d connections exists",
                                    createdConnections);
                    logger.warn(warnMessage, e);
                }
                e.printStackTrace();
            }
        }
        try {

            return connectionPool.take();
        } catch (InterruptedException e) {
            // TODO - what here?
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Returns connection to pool. If the pull is full (e.g. there are
     * maxPoolSize connections) then trying to return connection will only log
     * this situation. So it's very important not to return superfluous
     * connections to the pool as there is possibility of resources leakage
     * 
     * @param connection
     */
    public synchronized void returnConnection(Connection connection) {
        logger.debug("returning connection");

        if (connectionPool.offer(connection)) {
            logger.warn("The collection is outside of the pool!");
        }
    }

    /**
     * Releases all resource used by this class. Should be call while program
     * ends(or crashes)!
     **/
    @OnShutdown
    public void shutdown() {
        logger.debug("Shutting down connections pool");
        Connection conn = null;
        int shutDownConn = 0;
        while (!connectionPool.isEmpty()) {
            try {
                conn = connectionPool.take();
            } catch (InterruptedException e) {
                // TODO what here?
                e.printStackTrace();
            }

            try {
                conn.close();
                ++shutDownConn;
            } catch (SQLException e) {
                logger.warn("Cannot close connection. However if application"
                        + "is going to close it's not a critical problem", e);
            }
        }
        if (shutDownConn < createdConnections) {
            logger.warn("There was smaller amount of connections closed than created");
        }
    }
}
