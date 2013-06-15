package pl.edu.agh.ki.mmorts.server.data.utils;

import java.sql.Blob;

/**
 * @author drew
 * 
 */
public class QueriesCreator {

	public static final String PLAYER_MAIN_TAB = "players";
	public static final String PLAYER_NAME_COL = "player_name";
	public static final String PLAYER_CUST_DATA_COL = "player_data";
	public static final String PLAYER_LOGIN_COL = "player_login";
	public static final String PLAYER_PASS_COL = "player_password";

	/**
	 * 
	 * 
	 * @param moduleName
	 * @return
	 */
	public String getCreateCustomTableQuery(String moduleName) {
		moduleName = prepareModuleName(moduleName);
		return String.format("CREATE TABLE %s " + " (" + PLAYER_NAME_COL
				+ " VARCHAR(255) NOT NULL, " + PLAYER_CUST_DATA_COL
				+ " BLOB(1M), PRIMARY KEY(player_name))", moduleName);
	}

	public String getCreatePlayersTableQuery() {
		return String.format("CREATE TABLE " + PLAYER_MAIN_TAB + " ("
				+ PLAYER_NAME_COL + " VARCHAR(255), " + PLAYER_LOGIN_COL
				+ " VARCHAR(255), " + PLAYER_PASS_COL
				+ " VARCHAR(255), PRIMARY KEY(player_name))");
	}

	public String getSelectPlayerQuery(String playerName) {
		String selectQuery = String.format("SELECT * FROM " + PLAYER_MAIN_TAB + " WHERE "
				+ PLAYER_NAME_COL + " = '" + playerName + "'");
		return selectQuery;
	}

	public String getUpdatePlayerQuery(String playerName, String playerLogin,
			String playerPassword) {
		
		String updateString = "UPDATE " + PLAYER_MAIN_TAB + " SET "
						+ PLAYER_LOGIN_COL + " = '" + playerLogin + "', "
						+ PLAYER_PASS_COL + " = '" + playerPassword + "' WHERE "
						+ PLAYER_NAME_COL + " = '" + playerName + "'";
		return String.format(updateString);
	}

	public String getInsertPlayerQuery(String playerName, String playerLogin,
			String playerPassword) {
		String insertQuery = String.format("INSERT INTO " + PLAYER_MAIN_TAB + " VALUES"
						+ " ('" + playerName + "', '" 
						+ playerLogin + "', '"
						+ playerPassword + "')");
		return  insertQuery;
	}

	public String getDeletePlayerQuery(String playerName) {
		String deleteString = "DELETE FROM " + PLAYER_MAIN_TAB + " WHERE "
				+ PLAYER_NAME_COL + " = '" + playerName + "'";
		return String.format(deleteString);
	}

	public String getSelectCustomDataQuery(String moduleName, String playerName) {
		moduleName = prepareModuleName(moduleName);
		return String.format("SELECT * FROM " + moduleName + " WHERE "
				+ PLAYER_NAME_COL + " = " + playerName);
	}

	/**
	 * BLOB:/
	 * 
	 * @param playerName
	 * @param moduleName
	 * @return
	 */
	public String getUpdateCustomDataQuery(String playerName, String moduleName) {
		moduleName = prepareModuleName(moduleName);
		return String.format("UPDATE " + moduleName + " SET "
				+ PLAYER_CUST_DATA_COL + " = ? WHERE " + PLAYER_NAME_COL
				+ " = " + playerName);
	}

	/**
	 * BLOB:/
	 * 
	 * @param playerName
	 * @param moduleName
	 * @return
	 */
	public String getInsertCustomDataQuery(String playerName, String moduleName) {
		moduleName = prepareModuleName(moduleName);
		return String.format("INSERT INTO " + moduleName + " VALUES ("
				+ PLAYER_NAME_COL + " = " + playerName + PLAYER_CUST_DATA_COL
				+ " = ?");
	}

	public String getDeleteCustomDataQuery(String playerName, String moduleName) {
		return String.format("DELETE FROM " + moduleName + " WHERE "
				+ PLAYER_NAME_COL + " = " + playerName);
	}

	private String prepareModuleName(String moduleName) {
		moduleName = moduleName.replace(" ", "__");
		return moduleName;
	}
}
