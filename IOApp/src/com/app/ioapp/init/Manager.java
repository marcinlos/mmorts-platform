package com.app.ioapp.init;

/**
 * Class enabling to move players account to different phone
 *
 */
public interface Manager {
	
	/**
	 * Moves account to different phone
	 * @param mail
	 * @param password
	 */
	public void changePhone(String mail, String password);
	
	/**
	 * Adds player to phone
	 * @param mail
	 */
	public void addPlayerToPhone(String mail);

	/**
	 * Removes player from phone
	 * @param mail
	 */
	public void removePlayerFromPhone(String mail); 

}
