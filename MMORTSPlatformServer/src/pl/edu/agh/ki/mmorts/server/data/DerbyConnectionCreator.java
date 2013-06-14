package pl.edu.agh.ki.mmorts.server.data;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;

import com.google.inject.Inject;
import com.google.inject.name.Named;


public class DerbyConnectionCreator implements ConnectionCreator {
	private static final Logger logger = Logger
			.getLogger(DerbyConnectionCreator.class);
	
	@Inject
	@Named("db.url")
	private String connectionURL;
	
	@Inject
	Driver driver;
	
	
	@OnInit
	public void init(){
		logger.debug("Connection creator intialized");
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection createConnection() throws SQLException{
		logger.debug("Creating connection");
		Connection conn = null;
		conn = DriverManager.getConnection(connectionURL);
		logger.debug("Connection created");
		return conn;
	}
}
