package pl.edu.agh.ki.mmorts.server.data;



/**
 * Concrete simple implementation of {@link PlayerData}. It's immutable!
 * 
 * @author drew
 *
 */
public class PlayerDataImpl implements PlayerData {
	private String name;
	private String passwordHash;
	private String login;
	
		
	public PlayerDataImpl(String name, String passwordHash, String login) {
		super();
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
