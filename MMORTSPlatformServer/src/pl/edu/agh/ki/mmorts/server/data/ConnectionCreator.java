package pl.edu.agh.ki.mmorts.server.data;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * It's creator of connection. This should be "stupid" creator, without pooling
 * so it could be used to implement pool.
 * 
 * @author drew
 *
 */
public interface ConnectionCreator {
	/**
	 * Creates connection to database
	 * @throws SQLException
	 * 			thrown when can't create connection
	 */
	public Connection createConnection() throws SQLException;
}