package pl.edu.agh.ki.mmorts.server.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.core.InitException;
import pl.edu.agh.ki.mmorts.server.core.ModuleEventsListener;
import pl.edu.agh.ki.mmorts.server.core.ModuleEventsNotifier;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionListener;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.data.utils.QueriesCreator;
import pl.edu.agh.ki.mmorts.server.modules.ConfiguredModule;
import pl.edu.agh.ki.mmorts.server.modules.ModuleDescriptor;

import com.google.gson.Gson;
import com.google.inject.Inject;



/**
 * Simple implementation of {@link Database} which wraps Apache Derby database.
 * Implementation use connection pooling(implemented by {@link SimpleConnectionPool}) 
 * Implementation is fully trasanctional. Contract for transaction and connection is:
 * <ul>
 * <li>One connection per transaction</li>
 * <li>One transaction per one thread</li>
 * <li>Connection is returned to pool after either commit or rollback</li>
 * </ul>
 * Additionally to properly intialize database it is required that database can prepare
 * schema for loaded modules. So, to achieve this, this class implements {@link ModuleEventsListener} also.
 * 
 * @author drew
 * 
 */
public class DerbyDatabase implements Database, ModuleEventsListener{

	private static final Logger logger = Logger.getLogger(DerbyDatabase.class);

	/**
	 * Transaction manager which manages transactions 
	 */
	@Inject
	private TransactionManager tm;

	/**
	 * Mapping name of module to class type of it's data
	 */
	private  HashMap<String, Class<?>> namesObjectMap;

	/**
	 * Underlying utilities class for constructing SQL queries
	 */
	@Inject
	private QueriesCreator queriesCreator;

	/**
	 * Connection pool used by this database
	 */
	@Inject
	private SimpleConnectionPool connectionPool;
	
	@Inject
	private ModuleEventsNotifier notifier;

	/**
	 * As one thread is mapped to one transaction at every moment, this
	 * field is different per thread and represents current transaction connection.
	 * It's initialized(every time someone calls get on this variable, look in {@link ThreadLocal} javadoc
	 * new connection is intialized by {@link DerbyDatabase#initConnection()})
	 * 
	 *  @see ThreadLocal
	 */
	private ThreadLocal<Connection> perThreadConn = new ThreadLocal<Connection>() {
		@Override
		protected Connection initialValue() {
			Connection conn = null;
			try {
				conn = initConnection();
			} catch (NoConnectionException e) {
				logger.fatal("Connection for transaction could not be taken(anyone). "
						+ "Server is shutting down");
				System.exit(1);
			}
			return conn;
		}
	};

	/**
	 * Gson for serializing and deserializing data objects for modules
	 */
	private Gson gson = new Gson();
	
	/**
	 * Initialize correct connection. Done operations:
	 * <ul>
	 * <li>Getting connection from pool</li>
	 * <li>Disabling auto commit for connections</li>
	 * <li>Adding listeners if transaction is either commited or rolled back.
	 * Those listeners returns connection and remove {@link ThreadLocal} variable</li>
	 * </ul>
	 * @return
	 * 		initialized connection
	 * @throws NoConnectionException
	 * 		when can't get connection from pool
	 */
	private Connection initConnection() throws NoConnectionException {
		logger.debug("Initializing connection");
		final Connection conn = connectionPool.getConnection();
		try {
			conn.setAutoCommit(false);
		} catch (SQLException e) {
			logger.fatal("Cannot create transaction at database level");
			e.printStackTrace();// TODO
		}

		tm.getCurrent().addListener(new TransactionListener() {

			@Override
			public void rollback() {
				logger.debug("Database rollback - transaction rolled back");
				connectionReturn();
				perThreadConn.remove();
				try {
					conn.rollback();
				} catch (SQLException e) {
					logger.fatal("Cannot rollback transaction at database level");
					e.printStackTrace(); // TODO
				}
			}

			@Override
			public void commit() {
				logger.debug("Database commiting - transaction commited");
				connectionReturn();
				perThreadConn.remove();
				try {
					conn.commit();
				} catch (SQLException e) {
					logger.fatal("Cannot commit transaction at database level");
					e.printStackTrace(); // TODO
				}
			}
		});
		logger.debug("Connection initialized");
		return conn;
	}

	/**
	 * Just returns connection to pool
	 */
	private void connectionReturn() {
		connectionPool.returnConnection(perThreadConn.get());
	}
	
	
	

	/**
	 * Initializes database. In details:
	 * <ul>
	 * <li>Initializes connection pool</li>
	 * <li>Initializes all uninitialized fields of this class</li>
	 * <li>Initializes(if need) table in database for players</li>
	 * <li>Adds database as listener to the module events
	 * 	(see {@link ModuleEventsNotifier} and {@link ModuleEventsListener} )</li>
	 * </ul>
	 * 
	 * @see pl.edu.agh.ki.mmorts.server.data.Database#init()
	 */
	@Override
	@OnInit
	public void init() {
		logger.debug("Database init started");
		connectionPool.init();
		namesObjectMap = new HashMap<String, Class<?>>();
		tm.begin();
		Statement stm = null;
		try {
			stm = perThreadConn.get().createStatement();
			stm.execute(
					queriesCreator.getCreatePlayersTableQuery());
		} catch (SQLException e) {
			// X0Y32 is the error code of "table already exists"
			if (!e.getSQLState().equals("X0Y32")) {
				tm.rollback();
				throw new InitException(e);
			}
		}finally{
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					// TODO
					e.printStackTrace();
				}
			} 
		}
		tm.commit();
		notifier.addListener(this);
		logger.debug("Database init ended");
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPlayer(PlayerData player) throws IllegalArgumentException {
		logger.debug("Creating player " + player.getName());
		Statement stm = null;
		try {
			String playerName = player.getName();
			String playerLogin = player.getLogin();
			String playerPassword = player.getPasswordHash();
			stm = perThreadConn.get().createStatement();

			stm.execute(queriesCreator.getInsertPlayerQuery(playerName,
					playerLogin, playerPassword));
		} catch (SQLException e) {
			
			//23505 is error number of violating constraints(here PK)
			if (e.getSQLState().equals("23505")) {
				throw new IllegalArgumentException();
			}
			e.printStackTrace();
			//TODO
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					// TODO
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerData receivePlayer(String name) {
		logger.debug("Receiving player by name: " + name);
		PlayerData playerData = null;
		Statement stm = null;
		try {
			stm = perThreadConn.get().createStatement();
			ResultSet resultSet = stm.executeQuery(queriesCreator
					.getSelectPlayerQuery(name));
			while (resultSet.next()) {
				String playerName = resultSet
						.getString(QueriesCreator.PLAYER_NAME_COL);
				String playerLogin = resultSet
						.getString(QueriesCreator.PLAYER_LOGIN_COL);
				String playerPassword = resultSet
						.getString(QueriesCreator.PLAYER_PASS_COL);
				playerData = new PlayerDataImpl(playerName, playerPassword,
						playerLogin);
			}

		} catch (SQLException e) {
			logger.warn("Cannot receive player");
			e.printStackTrace();
			throw new IllegalArgumentException();
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					// TODO
					e.printStackTrace();
				}
			}
		}
		return playerData;
	}
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePlayer(String name, PlayerData player)
			throws IllegalArgumentException {
		logger.debug("Updating player with name: " + name);
		Statement stm = null;
		try {
			String playerLogin = player.getLogin();
			String playerPassword = player.getPasswordHash();
			stm = perThreadConn.get().createStatement();
			stm.executeUpdate(queriesCreator.getUpdatePlayerQuery(name,
					playerLogin, playerPassword));
			if (stm.getUpdateCount() == 0) {
				throw new IllegalArgumentException();
			}
		} catch (SQLException e) {
			logger.warn("Cannot update player");
			e.printStackTrace();
			//TODO
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					// TODO
					e.printStackTrace();
				}
			}
		}

	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deletePlayer(String name) throws IllegalArgumentException {
		logger.debug("Deleting player by name: " + name);
		Statement stm = null;
		try {
			stm = perThreadConn.get().createStatement();
			stm.executeUpdate(queriesCreator.getDeletePlayerQuery(name));
			if (stm.getUpdateCount() == 0) {
				throw new IllegalArgumentException();
			}
		} catch (SQLException e) {
			logger.warn("Cannot delete player");
			e.printStackTrace();
			throw new IllegalArgumentException();
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					// TODO
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createBinding(String moduleName, String playerName, Object o)
			throws IllegalArgumentException {
		logger.debug(String.format("Creating to player %s in module %s", playerName, moduleName));
		Statement stm = null;
		try{
			String sqlString = queriesCreator.getInsertCustomDataQuery(playerName, moduleName, serialize(o));
			stm = perThreadConn.get().createStatement();
			stm.execute(sqlString);
		} catch (SQLException e) {
			logger.warn("Cannot create custom binding");
			if (e.getSQLState().equals("23505")) {
				throw new IllegalArgumentException();
			}
			e.printStackTrace();
			//TODO
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					// TODO
					e.printStackTrace();
				}
			}
		}
		
		
	}

	
	
	/**
	 * Deserializes object from JSON representation
	 * to plain java Object. Gson needs type of object to
	 * which string should be deserialized. This type is provided
	 * by map {@link DerbyDatabase#namesObjectMap}.
	 * 
	 * @param moduleName
	 * 			module name which leads to proper type of deserializng
	 * @param stringData
	 * 			gson serialized data
	 * @return
	 * 			java {@link Object} which on runtime is needed custom object data
	 * 				declared by config of module
	 */
	private Object deserialize(String moduleName, String stringData) {
		return gson.fromJson(stringData, namesObjectMap.get(moduleName));
	}
	
	
	/**
	 * Serialize given object to JSON using gson
	 * 
	 * @param o
	 * 			object to serialize
	 * @return
	 * 			JSON string
	 */
	private String serialize(Object o) {
		return gson.toJson(o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Object receiveBinding(String moduleName, String playerName)
			throws IllegalArgumentException {
		logger.debug(String.format("Receiving data of player %s in module %s", playerName, moduleName));
		Statement stm = null;
		Object deserialized = null;
		try{
			String sqlString = queriesCreator.getSelectCustomDataQuery(moduleName,playerName);
			stm = perThreadConn.get().createStatement();
			ResultSet rs = stm.executeQuery(sqlString);
			if(rs.next()){
				String stringData = rs.getString(QueriesCreator.PLAYER_CUST_DATA_COL);
				deserialized = deserialize(moduleName, stringData);
			}else{
				throw new IllegalArgumentException();
			}
		} catch (SQLException e) {
			logger.warn("Cannot receive data");
			e.printStackTrace();
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					// TODO
					e.printStackTrace();
				}
			}
		}
		return deserialized;
	}


	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinding(String moduleName, String playerName, Object o)
			throws IllegalArgumentException {
		logger.debug(String.format("Updating data of player %s in module %s", playerName, moduleName));
		Statement stm = null;
		try {
			stm = perThreadConn.get().createStatement();
			String query = queriesCreator.getUpdateCustomDataQuery(playerName, moduleName, serialize(o));
			stm.executeUpdate(query);
			if (stm.getUpdateCount() == 0) {
				throw new IllegalArgumentException();
			}
		} catch (SQLException e) {
			logger.warn("Cannot update data!");
			e.printStackTrace();
			//TODO
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					// TODO
					e.printStackTrace();
				}
			}
		}
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteBinding(String moduleName, String playerName)
			throws IllegalArgumentException {
		logger.debug(String.format("Deleting binding of player %s in module %s", playerName, moduleName));
		Statement stm = null;
		try {
			stm = perThreadConn.get().createStatement();
			stm.executeUpdate(queriesCreator.getDeleteCustomDataQuery(playerName, moduleName));
			if (stm.getUpdateCount() == 0) {
				throw new IllegalArgumentException();
			}
		} catch (SQLException e) {
			logger.warn("Cannot delete binding!");
			e.printStackTrace();
			//TODO
		} finally {
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					// TODO
					e.printStackTrace();
				}
			}
		}

	}

	/**
	 * Removes database from module events listeners. Logs that database is closing
	 * 
	 * @see Database
	 */
	@Override
	@OnShutdown
	public void shutdown() {
		logger.debug("Closing database");
		notifier.removeListener(this);
	}

	
	
	/**
	 * Implementation is empty
	 * 
	 * @see pl.edu.agh.ki.mmorts.server.core.ModuleEventsListener#loadingModule(pl.edu.agh.ki.mmorts.server.modules.ModuleDescriptor)
	 */
	@Override
	public void loadingModule(ModuleDescriptor descriptor) {
		//empty
	}

	/**
	 * Database infrastructure(e. g. table for loaded module) is created here
	 * if it didn't exist. Nothing is done if table to given module exists.
	 * This means, that if someone wants to update module and change a data storing
	 * by this module, should delete table manually! Additionally class of module
	 * data is put into internal map ( {@link DerbyDatabase#namesObjectMap} ) 
	 * 
	 * @see pl.edu.agh.ki.mmorts.server.core.ModuleEventsListener#moduleLoaded(pl.edu.agh.ki.mmorts.server.modules.ConfiguredModule)
	 */
	@Override
	public void moduleLoaded(ConfiguredModule module) {
		logger.debug("Module "+ module.descriptor.name + " initialization at database side");
		tm.begin();
		Statement stm = null;
		try {
			stm = perThreadConn.get().createStatement();
			stm.execute(
					queriesCreator.getCreateCustomTableQuery(module.descriptor.name));
		} catch (SQLException e) {
			// X0Y32 is the error code of "table already exists"
			if (!e.getSQLState().equals("X0Y32")) {
				logger.warn("Cannot intialize module!");
				tm.rollback();
				throw new InitException(e);
			}
		}finally{
			if (stm != null) {
				try {
					stm.close();
				} catch (SQLException e) {
					// TODO
					e.printStackTrace();
				}
			} 
		}
		tm.commit();
		namesObjectMap.put(module.descriptor.name, getModuleDataClass(module.descriptor));
	}

	/**
     * @param desc
     * @return
     */
    private Class<?> getModuleDataClass(ModuleDescriptor desc){
            return desc.config.get("datatype", Class.class);
    }
	
	/**
	 * Implementation ommited
	 * 
	 * @see pl.edu.agh.ki.mmorts.server.core.ModuleEventsListener#unloadingModule(pl.edu.agh.ki.mmorts.server.modules.ConfiguredModule)
	 */
	@Override
	public void unloadingModule(ConfiguredModule module) {
		//ommited
	}

	/**
	 * Class of module is removed from internal map ( {@link DerbyDatabase#namesObjectMap} ). 
	 * <p>
	 * Disclaimer:
	 * Any of tables are deleted
	 * </p>
	 * @see pl.edu.agh.ki.mmorts.server.core.ModuleEventsListener#moduleUnloaded(pl.edu.agh.ki.mmorts.server.modules.ConfiguredModule)
	 */
	@Override
	public void moduleUnloaded(ConfiguredModule module) {
		logger.debug("Unloading from database module: " + module.descriptor.name);
		namesObjectMap.remove(module.descriptor.name);
	}

}
