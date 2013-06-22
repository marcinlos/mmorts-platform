package pl.edu.agh.ki.mmorts.server.data;

import java.sql.Blob;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.core.InitException;
import pl.edu.agh.ki.mmorts.server.core.ModuleTable;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionListener;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.data.utils.QueriesCreator;
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
 * 
 * @author drew
 * 
 */
public class DerbyDatabase implements Database {

	private static final Logger logger = Logger.getLogger(DerbyDatabase.class);

	/**
	 * Transaction manager which manages transactions 
	 */
	@Inject
	private TransactionManager tm;

	/**
	 * Contains informations about loaded moduels
	 */
	@Inject
	private ModuleTable moduleTable;
	
	private  HashMap<String, Class<?>> namesObjectMap;

	@Inject
	private QueriesCreator queriesCreator;

	@Inject
	private SimpleConnectionPool connectionPool;

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

	private Gson gson = new Gson();
	
	/**
	 * Initialize correct connection. Done operations:
	 * <ul>
	 * <li>Getting connection from pool</li>
	 * <li>Disabling auto commit for connections</li>
	 * <li>Adding listeners if transaction is either commited or rolled back.
	 * Those listeners returns connection</li>
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
	 * @param desc
	 * @return
	 */
	private Class<?> getModuleDataClass(ModuleDescriptor desc){
		return desc.config.get("datatype", Class.class);
	}
	
	
	

	/**
	 * Non transactional! Connection handled without transaction!
	 * 
	 * @see pl.edu.agh.ki.mmorts.server.data.Database#init()
	 */
	@Override
	@OnInit
	public void init() {
		logger.debug("Database init started");
		
		namesObjectMap = new HashMap<String, Class<?>>();
		for(ModuleDescriptor desc : moduleTable.getModuleDescriptors()){
			Class<?> dataTypeClass = getModuleDataClass(desc);
			if(dataTypeClass!=null){
				namesObjectMap.put(desc.name, dataTypeClass);
			}
		}
		
		connectionPool.init();
		Connection conn = null;
		try {
			conn = connectionPool.getConnection();
			conn.createStatement().execute(
					queriesCreator.getCreatePlayersTableQuery());
			for (ModuleDescriptor desc : moduleTable.getModuleDescriptors()) {
				conn.createStatement().execute(
						queriesCreator.getCreateCustomTableQuery(desc.name));
			}
		} catch (NoConnectionException e) {
			logger.fatal("Cannot create any, even starting connection to DB!");
			throw new InitException(e);
		} catch (SQLException e) {

			// X0Y32 is the error code of "table already exists"
			if (!e.getSQLState().equals("X0Y32")) {
				throw new InitException(e);
			}

		} finally {
			connectionPool.returnConnection(conn);
		}
		logger.debug("Database init ended");
	}

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

	

	private Object deserialize(String moduleName, String stringData) {
		return gson.fromJson(stringData, namesObjectMap.get(moduleName));
	}
	
	private String serialize(Object o) {
		return gson.toJson(o);
	}

	@Override
	public Object receiveBinding(String moduleName, String playerName)
			throws IllegalArgumentException {
		logger.debug(String.format("Receving data of player %s in module %s", playerName, moduleName));
		Statement stm = null;
		Object deserialized = null;
		try{
			String sqlString = queriesCreator.getSelectCustomDataQuery(moduleName,playerName);
			stm = perThreadConn.get().createStatement();
			ResultSet rs = stm.executeQuery(sqlString);
			while(rs.next()){
				String stringData = rs.getString(QueriesCreator.PLAYER_CUST_DATA_COL);
				deserialized = deserialize(moduleName, stringData);
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

	@Override
	@OnShutdown
	public void shutdown() {
		logger.debug("Closing database");
		logger.debug("Closing database ended");

	}

}
