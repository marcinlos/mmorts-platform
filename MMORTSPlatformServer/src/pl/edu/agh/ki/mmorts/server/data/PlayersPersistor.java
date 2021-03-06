package pl.edu.agh.ki.mmorts.server.data;

/**
 * Representing access to players information represented by {@link PlayerData}.
 * Provides simple CRUD access to records about player. The key by which players
 * are being retrieved from storage is it's name.
 * 
 * This interface should be used to implement effective wrapper, cache, on the
 * real implementation providing connectivity to DB.
 */
public interface PlayersPersistor {

	/**
	 * Adds player to database. Player name cannot be in database earlier
	 * 
	 * @param player - {@link PlayerData} object represents player to add
	 * @throws IllegalArgumentException - when player with this name exists in database
	 */
	void createPlayer(PlayerData player) throws IllegalArgumentException;
	
	/**
	 * Receives player with given name as {@link PlayerData}
	 * 
	 * @param name - name of requested player's data
	 * @return {@link PlayerData} representing information about player
	 */
	PlayerData receivePlayer(String name);
	/**
	 * 
	 * Updates player with given name with given information.
	 * The name in given parameter name and name in {@link PlayerData}
	 * must be the same! 
	 * 
	 * @param name - name of player whose data should be updated
	 * @param player - new player's data
	 * @throws IllegalArgumentException - when player with this name doesn't exist or
	 * name provided in parameter and {@link PlayerData} are not the same
	 */
	void updatePlayer(String name, PlayerData player) throws IllegalArgumentException;
	/**
	 * 
	 * Deletes player with given name
	 * 
	 * @param name - name to be deleted
	 * @throws IllegalArgumentException - when player with this name doesn't exist
	 */
	void deletePlayer(String name) throws IllegalArgumentException;
	
    
}
