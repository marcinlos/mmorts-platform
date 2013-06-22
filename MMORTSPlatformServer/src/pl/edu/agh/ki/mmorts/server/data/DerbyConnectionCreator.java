package pl.edu.agh.ki.mmorts.server.data;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import pl.edu.agh.ki.mmorts.server.core.annotations.OnInit;

import com.google.inject.Inject;
import com.google.inject.name.Named;

/**
 * Creates connections to Apache Derby database.
 * 
 * @author drew
 * 
 */
public class DerbyConnectionCreator implements ConnectionCreator {
	private static final Logger logger = Logger
			.getLogger(DerbyConnectionCreator.class);

	/**
	 * URL which enables JDBC to connect to database
	 */
	@Inject
	@Named("db.url")
	private String connectionURL;

	/**
	 * It's needed to load driver(class). It's unnecessary to have this
	 * association here, but for clarity it's staying here.
	 */
	@Inject
	private Driver driver;

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Connection createConnection() throws SQLException {
		logger.debug("Creating connection");
		Connection conn = null;
		conn = DriverManager.getConnection(connectionURL);
		return conn;
	}
}
