package pl.edu.agh.ki.mmorts.server.data;



import java.sql.Connection;
import java.sql.SQLException;

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
	
	private static final Logger logger = Logger
            .getLogger(DerbyDatabase.class);
	
	@Inject
	TransactionManager tm;
	
	@Inject
	ModuleTable moduleTable;

	@Inject
	QueriesCreator queriesCreator;
	
	@Inject
	SimpleConnectionPool connectionPool;
	
	ThreadLocal<Connection> perThreadConn =new ThreadLocal<Connection>() {
		@Override
		protected Connection initialValue() {
			Connection conn = null;
			try {
				conn = initConnection();
			} catch (NoConnectionException e) {
				logger.fatal("Connection for transaction could not be taken(anyone). " +
						"Server is shutting down");
				System.exit(1);
			}
			return conn;
		}
	};
	
	private Connection initConnection() throws NoConnectionException{
		Connection conn = connectionPool.getConnection();
		tm.getCurrent().addListener(new TransactionListener() {
			
			@Override
			public void rollback() {
				connectionReturn();
				
			}
			
			@Override
			public void commit() {
				connectionReturn();
				
			}
		});
		return conn;
	}
	
	private void connectionReturn(){
		connectionPool.returnConnection(perThreadConn.get());
	}
	
	
	/**
	 * Non transactional! Connection handled without transaction!
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
				conn.createStatement().execute(queriesCreator.getCreatePlayersTableQuery());
				for(ModuleDescriptor desc : moduleTable.getModuleDescriptors()){
					conn.createStatement().execute(queriesCreator.getCreateCustomTableQuery(desc.name));
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
		// TODO Auto-generated method stub
		
	}

	@Override
	public PlayerData receivePlayer(String name) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePlayer(String name, PlayerData player)
			throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void deletePlayer(String name) throws IllegalArgumentException {
		// TODO Auto-generated method stub
		
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
