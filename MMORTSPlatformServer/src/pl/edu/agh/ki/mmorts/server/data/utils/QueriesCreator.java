package pl.edu.agh.ki.mmorts.server.data.utils;

/**
 * @author drew
 *
 */
public class QueriesCreator {

	
	/**
	 * TO lowwercase
	 * @param moduleName
	 * @return
	 */
	public String getCreateCustomTableQuery(String moduleName){
		moduleName=moduleName.toLowerCase();
		return String.format("CREATE TABLE %s " +
				"(player_name VARCHAR(255) NOT NULL AS IDENTITY," +
				"player_data BLOB(1M))", moduleName);
	}
	
	public String getCreatePlayersTableQuery(){
		return String.format("CREATE TABLE players " +
				"(id NOT NULL GENERATED ALWAYS AS IDENTITY," +
				"player_name VARCHAR(255)," +
				"player_login VARCHAR(255)," +
				"player_password VARCHAR(255)");
	}
	
}
