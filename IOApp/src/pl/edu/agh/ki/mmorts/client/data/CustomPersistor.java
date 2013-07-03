package pl.edu.agh.ki.mmorts.client.data;


/**
 * Representing CRUD operations on custom data from custom modules. General
 * interface map player's name to custom data object. Now it's just simple
 * interface which provides returning of valid java objects(as passed to method) from database and saving
 * these objects. Now there is no checking of validity(except for parametrized method)
 * 
 * <p>
 * Interface is generally used to preserve server's architecture so both
 * architectures could be easier to understand. Of course, here could be used
 * easier hierarchy and methods but we wanted to avoid inconsistency in architecture
 * </p>
 * 
 */

public interface CustomPersistor {

	/**
	 * Creates binding between object and Player name for given module.
	 * Player name cannot be bound in this module in database earlier.
	 * 
	 * @param moduleName
	 * 			indicates for which module binding occurs
	 * @param playerName
	 * 			object is going to be bound to this player name
	 * @param o
	 * 			object bound
	 * @throws IllegalArgumentException
	 * 			thrown when binding with this player name exists in this module in database
	 */
	void createBinding(String moduleName, String playerName, Object o) throws IllegalArgumentException;
	
	/**
	 * Receives object bound to player name for given module.
	 * Player name must be bound in this module in database.
	 * Parameter T indicates the class type which returned object should be of.
	 * @param moduleName
	 * 			indicates for which module binding is looked
	 * @param playerName
	 * 			indicates player to which object is bound
	 * @param clazz
	 * 			indicates what class should be returned instead of plain Object
	 * @return
	 * 			Valid Java object bound with player name in given module
	 * @throws IllegalArgumentException
	 * 			thrown when player name is not bound in this module
	 */
	<T> T receiveBinding(String moduleName, String playerName, Class<T> clazz) throws IllegalArgumentException;
	/**
	 * Updates object bound to player name for given module.
	 * Player name must be bound in this module in database earlier.
	 * 
	 * @param modulename
	 * 			indicates for which module binding is processed
	 * @param playerName
	 * 			indicates player to which updated object is bound
	 * @param o
	 * 			custom Object of custom module data
	 * @throws IllegalArgumentException
	 * 			thrown when player name is not bound in this module 
	 */
	void updateBinding(String modulename, String playerName, Object o) throws IllegalArgumentException;
	/**
	 * Deletes binding in given module.
	 * Binding must occur in this module in database earlier.
	 * 
	 * 
	 * @param modulename
	 * 		indicates for which module binding is processed
	 * @param playerName
	 * 		indicates player whose binding is being deleted
	 * @throws IllegalArgumentException
	 * 		thrown when player name is not bound in this module
	 */
	void deleteBinding(String modulename, String playerName) throws IllegalArgumentException;
}
