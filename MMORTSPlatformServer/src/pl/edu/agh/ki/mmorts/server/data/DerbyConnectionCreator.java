package pl.edu.agh.ki.mmorts.server.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;


public class DerbyConnectionCreator implements ConnectionCreator {
	private static final Logger logger = Logger
			.getLogger(DerbyConnectionCreator.class);
	
	final static String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	final static String DB_NAME = "test";
	final String connectionURL = "jdbc:derby:" + DB_NAME + ";create=true";
	
	public DerbyConnectionCreator() {
		logger.debug("Creating connection creator");
		try {
			Class.forName(DRIVER);
		} catch (java.lang.ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection createConnection() throws SQLException{
		logger.debug("Creating connection");
		Connection conn = null;
		conn = DriverManager.getConnection(connectionURL);
		return conn;
	}
}
