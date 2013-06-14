package pl.edu.agh.ki.mmorts.server.data;

import java.sql.Connection;
import java.sql.SQLException;

public interface ConnectionCreator {
	public Connection createConnection() throws SQLException;
}