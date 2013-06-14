package pl.edu.agh.ki.mmorts.server.data;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionCreator {
	/**
	 * 
	 * @throws SQLException
	 * 			thrown when can't create connection
	 */
	public Connection createConnection() throws SQLException;
}