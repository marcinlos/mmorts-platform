package pl.edu.agh.ki.mmorts.server.data;


/**
 * Database wrapper of some storage mechanisms - it's like DAO
 * Its operations are really similar to {@link CustomPersistor} and {@link PlayersPersistor}
 * It shouldn't be an surprise cause this interface only perform raw operations
 * on storage(but gently wrapped) and is developed to easily maintain
 * a process of changing underlying storage.
 * 
 * <p>
 * Considered situation deal with storing only currently playing avatar data and this interface is
 * generally used to preserve server's architecture so both architectures could be easier to understand.
 * Of course, here could be used
 * easier hierarchy but we wanted to avoid inconsistency in architecture
 * </p> 
 * 
 * Additionally DAO shouldn't do any additional validation or processing of data except for this
 * which will be reported by underlying storage.
 * 
 * Moreover this interface is very low-level interface. There is no place for magic.
 * 
 * Later in this doc, storage is named as database. However, on this side this storage could be made
 * of java Maps
 * 
 * @see CustomPersistor
 * 
 */
public interface Database {

	
	/**
	 * Performs all initialization processes which should be done.
	 * Usually everything connected with schema validation and connections
	 * management.
	 * 
	 * 
	 * @see OnInit
	 */
	void init();
	
	/**
	 * Adds player to database. Player name cannot be in database earlier
	 * cause it raises database error.
	 * 
	 * @param player 
	 * 			object represents player to add
	 * @throws IllegalArgumentException 
	 * 			thrown when player with this name exists in database.
	 * 			Consequence of situation that raises DB error.
	 * @see PlayersPersistor
	 */
	void createPlayer(PlayerData player) throws IllegalArgumentException;
	
	/**
	 * Receives player with given name as {@link PlayerData}. Doesn't throw
	 * any exception even, if player doesn't exist because it's not DB error
	 * and returns null then
	 * 
	 * @param name
	 * 			name of requested player's data
	 * @return 
	 * 			information about player
	 * @see PlayersPersistor
	 */
	PlayerData receivePlayer(String name);
	/**
	 * Updates player with given name with given information.
	 * The name in given parameter name and name in {@link PlayerData}
	 * are considered to be the same {@link PlayersPersistor}
	 * 
	 * @param name	
	 * 			name of player whose data should be updated
	 * @param player
	 * 			new player's data
	 * @throws IllegalArgumentException
	 * 			when player with this name doesn't exit.
	 * 			Consequence of situation that raises DB error.
	 * @see PlayersPersistor
	 */
	void updatePlayer(String name, PlayerData player) throws IllegalArgumentException;
	/**
	 * Deletes player with given name
	 * 
	 * @param name
	 * 			name to be deleted
	 * @throws IllegalArgumentException
	 * 			player with this name doesn't exist.
	 * 			Consequence of situation that raises DB error.
	 * @see PlayersPersistor
	 */
	void deletePlayer(String name) throws IllegalArgumentException;
	
	
	/**
	 * Creates binding between object and Player name for given module.
	 * Player name cannot be bound in this module in database earlier.
	 * 
	 * <p>
	 * Yes, it <b>is</b> the same as {@link CustomPersistor} now! Read {@link CustomPersistor}
	 * javadoc to find out why!
	 * </p>
	 * 
	 * @param moduleName
	 * 			indicates for which module binding occurs
	 * @param playerName
	 * 			object is going to be bound to this player name
	 * @param o
	 * 			object bound
	 * @throws IllegalArgumentException
	 * 			thrown when binding with this player name exists in this module in database
	 * 
	 */
	void createBinding(String moduleName, String playerName, Object o) throws IllegalArgumentException;
	
	/**
	 * Receives object bound to player name for given module.
	 * If player name wasnt't bound in this module in database earlier return null.
	 * 
	 * <p>
	 * Yes, it <b>is</b> the same as {@link CustomPersistor} now! Read {@link CustomPersistor}
	 * javadoc to find out why!
	 * </p>
	 * 
	 *
	 * @param moduleName
	 * 			indicates for which module binding is looked
	 * @param playerName
	 * 			indicates player to which object is bound
	 * @return
	 * 			Valid Java object bound with player name in given module or null
	 */
	Object receiveBinding(String moduleName, String playerName);
	
	/**
	 * Updates object bound to player name for given module.
	 * Player name must be bound in this module in database.
	 *
	 * 
	 * 
	 * <p>
	 * Yes, it <b>is</b> the same as {@link CustomPersistor} now! Read {@link CustomPersistor}
	 * javadoc to find out why!
	 * </p>
	 *
	 * @param moduleName
	 * 			indicates for which module binding is processed
	 * @param playerName
	 * 			indicates player to which updated object is bound
	 * @param o
	 * 			custom Object of custom module data
	 * @throws IllegalArgumentException
	 * 			thrown when player name is not bound in this module 
	 */
	void updateBinding(String moduleName, String playerName, Object o) throws IllegalArgumentException;
	/**
	 * Deletes binding in given module.
	 * Binding must occur in this module in database earlier.
	 * 
	 * <p>
	 * Yes, it <b>is</b> the same as {@link CustomPersistor} now! Read {@link CustomPersistor}
	 * javadoc to find out why!
	 * </p>
	 *
	 * 
	 * @param moduleName
	 * 		indicates for which module binding is processed
	 * @param playerName
	 * 		indicates player whose binding is being deleted
	 * @throws IllegalArgumentException
	 * 		thrown when player name is not bound in this module
	 */
	void deleteBinding(String moduleName, String playerName) throws IllegalArgumentException;
	
	
	/**
	 * Releases all used resources, usually connected with connection or dbcache
	 * 
	 */
	void shutdown();
	
}
