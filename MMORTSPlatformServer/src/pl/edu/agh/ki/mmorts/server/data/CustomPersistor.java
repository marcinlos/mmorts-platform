package pl.edu.agh.ki.mmorts.server.data;


/**
 * Representing CRUD operations on custom data from custom modules. General
 * interface map player's name to custom data object. Now it's just simple
 * interface which provides returning of valid java Object from database and saving
 * these objects. Now there is no checking of validity. Read below what could be done!
 * 
 * <p>
 * Generally, now this interface is created for preserving the symmetry with PlayersPersistor
 * Its created to add flexibility to class hierarchy too
 * </p>
 * 
 * Here there could be some magic in the future. By using standarised custom modules data classes
 * (a'la java beans), some reflection and a bit of something like visitor pattern there could be used
 * a scenario: CustomPersistor has some special methods such as increase(), decrease(), divide(), clear()
 * etc. which takes object and do some manipulations on their initial state.(reflection is needed,
 * because we can't call object method from string) 
 */


//TODO add init!!!

public interface CustomPersistor {

	/**
	 * Creates binding between object and Player name for given module.
	 * Player name cannot be bound in this module in database earlier.
	 * 
	 * @param moduleName
	 * 			indicates for which module binding occurs
	 * @param playerName
	 * 			object is going to be bound to this player name
	 * @param data
	 * 			data object bound
	 * @throws IllegalArgumentException
	 * 			thrown when binding with this player name exists in this module in database
	 */
	<T> void createBinding(String moduleName, String playerName, T data) throws IllegalArgumentException;
	
	/**
	 * Receives object bound to player name for given module.
	 * Player name must be bound in this module in database.
	 * @param moduleName
	 * 			indicates for which module binding is looked
	 * @param playerName
	 * 			indicates player to which object is bound
	 * @return
	 * 			Valid Java object(connected with module) bound with player name in given module
	 * @throws IllegalArgumentException
	 * 			thrown when player name is not bound in this module
	 */
	<T> T receiveBinding(String moduleName, String playerName, Class<T> type) throws IllegalArgumentException;
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
	<T> void updateBinding(String modulename, String playerName, T data) throws IllegalArgumentException;
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
