package pl.edu.agh.ki.mmorts.server.data.utils;


/**
 * Utility class for creating SQL strings which are valid SQL queries
 * 
 * @author drew
 * 
 */
public class QueriesCreator {

	/**
	 * Table containing players and their main data(login, name, password hash)
	 */
	public static final String PLAYER_MAIN_TAB = "players";
	/**
	 * Column name of column in which player name is stored
	 */
	public static final String PLAYER_NAME_COL = "player_name";
	/**
	 * Column name of column in which custom data player of is stored(in tables specified to modules)
	 */
	public static final String PLAYER_CUST_DATA_COL = "player_data";
	/**
	 * Column name of column in which player login is stored
	 */
	public static final String PLAYER_LOGIN_COL = "player_login";
	/**
	 * Column name of column in which player password hash is stored
	 */
	public static final String PLAYER_PASS_COL = "player_password";

	/**
	 * Constructs query which creates table to store player data for given module.
	 * Name of the module is normalized - all spaces changed to __(two underscores).
	 * Normalized name is then table name 
	 * 
	 * @param moduleName
	 * 			indicates module name for which the table is created
	 * @return
	 * 			valid SQL query to create table for module 
	 */
	public String getCreateCustomTableQuery(String moduleName) {
		moduleName = prepareModuleName(moduleName);
		return String.format("CREATE TABLE %s " + " (" + PLAYER_NAME_COL
				+ " VARCHAR(255) NOT NULL, " + PLAYER_CUST_DATA_COL
				+ " VARCHAR(4096), PRIMARY KEY(player_name))", moduleName);
	}

	
	/**
	 * Constructs query which creates table to store players and their main data

	 * @return
	 * 			valid SQL query to create table for players
	 */
	public String getCreatePlayersTableQuery() {
		return String.format("CREATE TABLE " + PLAYER_MAIN_TAB + " ("
				+ PLAYER_NAME_COL + " VARCHAR(255), " + PLAYER_LOGIN_COL
				+ " VARCHAR(255), " + PLAYER_PASS_COL
				+ " VARCHAR(255), PRIMARY KEY(player_name))");
	}

	/**
	 * Constructs valid select query of all data connected with given player
	 * 
	 * @param playerName
	 * 		indicates which player's data is to be selected
	 * @return
	 * 		valid SQL query to select all data of player with given name
	 */
	public String getSelectPlayerQuery(String playerName) {
		String selectQuery = String.format("SELECT * FROM " + PLAYER_MAIN_TAB
				+ " WHERE " + PLAYER_NAME_COL + " = '" + playerName + "'");
		return selectQuery;
	}

	
	/**
	 * Constructs valid update query to update data connected with given player
	 * by given new data
	 * 
	 * @param playerName
	 * 			player name to update the data of
	 * @param playerLogin
	 * 			new account's login
	 * @param playerPassword
	 * 			new account's password
	 * @return
	 * 			valid SQL query updating data of given player
	 */
	public String getUpdatePlayerQuery(String playerName, String playerLogin,
			String playerPassword) {

		String updateString = "UPDATE " + PLAYER_MAIN_TAB + " SET "
				+ PLAYER_LOGIN_COL + " = '" + playerLogin + "', "
				+ PLAYER_PASS_COL + " = '" + playerPassword + "' WHERE "
				+ PLAYER_NAME_COL + " = '" + playerName + "'";
		return String.format(updateString);
	}

	/**
	 * Constructs valid insert query to insert new player an it's data
	 * 
	 * @param playerName
	 * 			new player's name
	 * @param playerLogin
	 * 			new account's login
	 * @param playerPassword
	 * 			new account's password
	 * @return
	 * 			valid SQL query inserting given player with data
	 */
	public String getInsertPlayerQuery(String playerName, String playerLogin,
			String playerPassword) {
		String insertQuery = String.format("INSERT INTO " + PLAYER_MAIN_TAB
				+ " VALUES" + " ('" + playerName + "', '" + playerLogin
				+ "', '" + playerPassword + "')");
		return insertQuery;
	}

	/**
	 * Constructs valid SQL delete query to remove given player and it's data from db 
	 * @param playerName
	 * 			player name to be removed
	 * @return
	 * 			valid SQL delete query deleting given player and it's data
	 */
	public String getDeletePlayerQuery(String playerName) {
		String deleteString = "DELETE FROM " + PLAYER_MAIN_TAB + " WHERE "
				+ PLAYER_NAME_COL + " = '" + playerName + "'";
		return String.format(deleteString);
	}

	/**
	 * Constructs valid select query of all data connected with given player in given module
	 * Name of the module is normalized - all spaces changed to __(two underscores).
	 * Normalized name is then table name
	 * 
	 * @param moduleName
	 * 			indicates name of concerned module
	 * @param playerName
	 * 			indicates name of concerned player
	 * @return
	 * 			valid SQL select query getting all data in given module for given player
	 */
	public String getSelectCustomDataQuery(String moduleName, String playerName) {
		moduleName = prepareModuleName(moduleName);
		return String.format("SELECT * FROM " + moduleName + " WHERE "
				+ PLAYER_NAME_COL + " = '" + playerName + "'");
	}

	/**
	 * Constructs valid update query to update data connected with given player in given module 
	 * by given new data
	 * Name of the module is normalized - all spaces changed to __(two underscores).
	 * Normalized name is then table name
	 * 
	 * @param playerName
	 * 		player name to update the data of
	 * @param moduleName
	 * 		indicates name of concerned module
	 * @param data
	 * 		new data to be bound with player
	 * @return
	 * 		valid SQL update query updating necessary information
	 */
	public String getUpdateCustomDataQuery(String playerName,
			String moduleName, String data) {
		moduleName = prepareModuleName(moduleName);
		return String.format("UPDATE " + moduleName + " SET "
				+ PLAYER_CUST_DATA_COL + " ='" + data + "' WHERE "
				+ PLAYER_NAME_COL + " = '" + playerName + "'");
	}

	/**
	 * Constructs valid insert query to insert new player  an it's data in given module
	 * Name of the module is normalized - all spaces changed to __(two underscores).
	 * Normalized name is then table name
	 * 
	 * @param playerName
	 * 			player name to whom data will be bound
	 * @param moduleName
	 * 			indicates name of concerned module
	 * @param data
	 * 			data to be bound with player
	 * @return
	 * 			valid SQL insert query inserting binding between player and data in given module
	 */
	public String getInsertCustomDataQuery(String playerName,
			String moduleName, String data) {
		moduleName = prepareModuleName(moduleName);
		return String.format("INSERT INTO " + moduleName + " VALUES (" + "'"
				+ playerName + "', " + " '" + data + "')");
	}

	/**
	 * Constructs valid SQL delete query to remove given player bindings in the given module
	 * Name of the module is normalized - all spaces changed to __(two underscores).
	 * Normalized name is then table name
	 * 
	 * @param playerName
	 * 			player name whose binding will be deleted
	 * @param moduleName
	 * 			indicates name of concerned module
	 * @return
	 * 			valid SQL delete query deleting given binding in given module
	 */
	public String getDeleteCustomDataQuery(String playerName, String moduleName) {
		moduleName = prepareModuleName(moduleName);
		return String.format("DELETE FROM " + moduleName + " WHERE "
				+ PLAYER_NAME_COL + " = '" + playerName + "'");
	}

	/**
	 * Normalizes module name string. Changes spaces to two underscores.
	 * <p>
	 * Example: module example_name -> module__example_name
	 * </p>
	 * 
	 * @param moduleName
	 * @return
	 */
	private String prepareModuleName(String moduleName) {
		moduleName = moduleName.replace(" ", "__");
		return moduleName;
	}
}
