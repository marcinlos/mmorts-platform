package pl.edu.agh.ki.mmorts.client.data;

/**
 * Represents set of data associated with particular player. 
 * Temporarily it's static but preferred way to implement in future
 * is dynamic kind of map of property name and {@code Object}. Unfortunately
 * there is no way to provide kind style(easily configurable, flexible and type-checked) of creating such an interface even
 * with an ORM.
 * 
 *  @author drew
 */
public interface PlayerData {

	
	/**
	 * Returns the name of represented player
	 * 
	 * @return name of representing player
	 */
	String getName();

	/**
	 * Returns the password hash of represented player's account
	 * 
	 * @return password hash of represented player's account
	 */
	String getPasswordHash();

	/**
	 * Returns the login of represented player's account. Login shouldn't be the same
	 * as player name(for example one could have more players on one login)
	 * 
	 * @return login of represented player's account
	 */
	String getLogin();
	
}
