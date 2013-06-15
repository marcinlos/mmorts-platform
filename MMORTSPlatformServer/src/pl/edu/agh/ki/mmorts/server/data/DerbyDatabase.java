package pl.edu.agh.ki.mmorts.server.data;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.core.InitException;
import pl.edu.agh.ki.mmorts.server.core.ModuleTable;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.server.core.annotations.OnShutdown;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionListener;
import pl.edu.agh.ki.mmorts.server.core.transaction.TransactionManager;
import pl.edu.agh.ki.mmorts.server.data.utils.QueriesCreator;
import pl.edu.agh.ki.mmorts.server.modules.ModuleDescriptor;

import com.google.inject.Inject;

/**
 * @author drew
 * 
 */
public class DerbyDatabase implements Database {

	private static final Logger logger = Logger.getLogger(DerbyDatabase.class);

	@Inject
	private TransactionManager tm;

	@Inject
	private ModuleTable moduleTable;

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

	private void connectionReturn() {
		connectionPool.returnConnection(perThreadConn.get());
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
			if (e.getSQLState().equals("23505")) {
				throw new IllegalArgumentException();
			}
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
			String playerName = player.getName();
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
		// TODO Auto-generated method stub

	}

	@Override
	public Object receiveBinding(String moduleName, String playerName)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updateBinding(String modulename, String playerName, Object o)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteBinding(String modulename, String playerName)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub

	}

	@Override
	@OnShutdown
	public void shutdown() {
		logger.debug("Closing database");
		logger.debug("Closing database ended");

	}

}
