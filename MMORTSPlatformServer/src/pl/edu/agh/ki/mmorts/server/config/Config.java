package pl.edu.agh.ki.mmorts.server.config;

import java.sql.Driver;
import java.util.Map;

import pl.edu.agh.ki.mmorts.server.communication.MessageInputChannel;
import pl.edu.agh.ki.mmorts.server.core.Dispatcher;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.data.ConnectionCreator;
import pl.edu.agh.ki.mmorts.server.data.Database;
import pl.edu.agh.ki.mmorts.server.data.PlayersPersistor;

/**
 * Allows retrieval of configuration properties.
 * 
 * @author los
 */
public interface Config {

    /** Used implementation of transaction manager */
    public static final String TM_CLASS = "sv.tm.class";

    /** Used implementation of dispatcher */
    public static final String DISPATCHER_CLASS = "sv.dispatcher.class";

    /** Interface of the custom persistor */
    public static final String CUSTOM_PERSISTOR_INTERFACE = "sv.persistor.interface";

    /** Implementation of the custom persistor */
    public static final String CUSTOM_PERSISTOR_CLASS = "sv.persistor.class";
    
    /** Implementation of Database */
    public static final String CONNECTION_CREATOR_CLASS = "sv.database.creator.class";

    /** Implementation of Database */
    public static final String DATABASE_CLASS = "sv.database.class";

    /** Implementation of ordinary persistence */
    public static final String PLAYERS_MANAGER_CLASS = "sv.persistence.class";

    /** Implementation of message delivery mechanism */
    public static final String CHANNEL_CLASS = "sv.message.channel.class";

    /** Path of the module configuration file */
    public static final String MODULE_CONFIG_FILE = "sv.modules.config";
    
    /** Path of the module configuration file */
    public static final String DATABASE_DRIVER_CLASS = "db.driver";
    

    /**
     * Retrieves a string value of a property
     * 
     * @param key
     *            Name of the property
     * @return Value of the property or {@code null} if it is not present in the
     *         configuration.
     */
    String getString(String key);

    /**
     * @return TransactionManager implementation class specified in the
     *         configuration
     */
    Class<? extends TransactionManager> getTransactionManagerClass();

    /**
     * @return MessageInputChannel implementation class specified in the
     *         configuration
     */
    Class<? extends MessageInputChannel> getChannelClass();

    /**
     * @return Dispatcher implementation class specified in the configuration
     */
    Class<? extends Dispatcher> getDispatcherClass();

    /**
     * @return Implementation of custom persistor specified in the configuration
     */
    Class<?> getCustomPersistorInterface();

    /**
     * @return Implementation of custom persistor specified in the configuration
     */
    Class<?> getCustomPersistorClass();
    
    /**
     * @return Implementation of the connection creator
     */
    Class<? extends ConnectionCreator> getConnectionCreatorClass();

    /**
     * @return Implementation of players database access object specified in the
     *         configuration
     */
    Class<? extends Database> getDatabaseClass();

    /**
     * @return Implementation of players manager specified in the configuration
     */
    Class<? extends PlayersPersistor> getPlayerManagerClass();
    
    
    /**
     * @return Implementation of jdbc driver specified in the configuration
     */
    Class<? extends Driver> getJdbcDriverClass();

    /**
     * @return Read-only map of all the available properties
     */
    Map<String, String> getProperties();

}
