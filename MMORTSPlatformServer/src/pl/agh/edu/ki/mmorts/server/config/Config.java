package pl.agh.edu.ki.mmorts.server.config;

import java.util.Map;

import pl.edu.agh.ki.mmorts.server.core.Dispatcher;
import pl.edu.agh.ki.mmorts.server.data.CustomPersistor;
import pl.edu.agh.ki.mmorts.server.data.PlayersDAO;
import pl.edu.agh.ki.mmorts.server.data.PlayersManager;

/**
 * Allows retrieval of configuration properties
 */
public interface Config {

    /** Used implementation of dispatcher */
    public static final String DISPATCHER_CLASS = "sv.dispatcher.class";

    /** Implementation of the custom persistor */
    public static final String CUSTOM_PERSISTOR_CLASS = "sv.persistor.class";

    /** Implementation of PlayersDAO */
    public static final String PLAYERS_DAO_CLASS = "sv.dao.class";

    /** Implementation of ordinary persistence */
    public static final String PLAYERS_MANAGER_CLASS = "sv.persistence.class";

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
     * @return Dispatcher implementation class specified in the configuration
     */
    Class<? extends Dispatcher> getDispatcherClass();

    /**
     * @return Implementation of custom persistor specified in the configuration
     */
    Class<? extends CustomPersistor> getCustomPersistorClass();

    /**
     * @return Implementation of players database access object specified in the
     *         configuration
     */
    Class<? extends PlayersDAO> getPlayersDaoClass();

    /**
     * @return Implementation of players manager specified in the configuration
     */
    Class<? extends PlayersManager> getPlayerManagerClass();

    /**
     * @return Read-only map of all the available properties
     */
    Map<String, String> getProperties();

}
