package pl.edu.agh.ki.mmorts.server.data;



/**
 * Concrete simple implementation of {@link PlayerData}. It's immutable!
 * 
 * @author drew
 *
 */
public class PlayerDataImpl implements PlayerData {
	
	/**
	 * Name of player this object represents
	 */
	private String name;
	
	/**
	 * Hash of password to login to this account
	 */
	private String passwordHash;
	
	/**
	 * Name of account this player belongs to
	 */
	private String login;
	
		
	/**
	 * Constructs <b>immutable</b> {@link PlayersPersistorImpl} object with given attributes
	 * @param name
	 * 			name of player this object represents
	 * @param passwordHash
	 * 			hash of password to login to this account
	 * @param login
	 * 			name of account this player belongs to
	 */
	public PlayerDataImpl(String name, String passwordHash, String login) {
		this.name = name;
		this.passwordHash = passwordHash;
		this.login = login;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getName() {
		return name;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getPasswordHash() {
		return passwordHash;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLogin() {
		return login;
	}
	
	
}
