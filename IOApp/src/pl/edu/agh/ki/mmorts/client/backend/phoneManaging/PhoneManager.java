package pl.edu.agh.ki.mmorts.client.backend.phoneManaging;

/**
 * Class enabling to move players account to different phone
 *
 */
public interface PhoneManager {
	
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
