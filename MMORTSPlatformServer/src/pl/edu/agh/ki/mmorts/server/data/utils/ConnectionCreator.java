package pl.edu.agh.ki.mmorts.server.data.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.google.inject.Inject;
import com.google.inject.name.Named;


//TODO - extract to interface, replace to server.data package?
public class ConnectionCreator {
	private static final Logger logger = Logger
			.getLogger(ConnectionCreator.class);
	
	@Inject
	@Named("db.driver")
	String DRIVER = "org.apache.derby.jdbc.EmbeddedDriver";
	final static String DB_NAME = "test";
	final String connectionURL = "jdbc:derby:" + DB_NAME + ";create=true";
	
	public ConnectionCreator() {
		logger.debug("Creating connection creator");
		try {
			Class.forName(DRIVER);
		} catch (java.lang.ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	public Connection createConnection() throws SQLException{
		logger.debug("Creating connection");
		Connection conn = null;
		conn = DriverManager.getConnection(connectionURL);
		return conn;
	}
}
