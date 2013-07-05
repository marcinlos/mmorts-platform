package pl.edu.agh.ki.mmorts.client.data;

import com.google.inject.Inject;

/**
 * Implementation of {@link PlayersPersistor} which generally wraps raw {@link Database}
 * to achieve typing. There is nothing special here, except for checking for
 * some additional logic correctness which is not done at {@link Database} level
 * and delegating operations to {@link Database} implementation.
 * 
 * Compare javadocs for {@link Database} and {@link PlayersPersistor} for see what 
 * additional checks are done on concrete methods.
 * 
 *
 */
public class PlayersPersistorImpl implements PlayersPersistor {

	/**
	 * Underlying {@link Database} implementation
	 */
	@Inject
	private Database db;

	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createPlayer(PlayerData player) throws IllegalArgumentException {
		db.createPlayer(player);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public PlayerData receivePlayer(String name)
			throws IllegalArgumentException {
		return db.receivePlayer(name);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updatePlayer(String name, PlayerData player)
			throws IllegalArgumentException {
		if (name.equals(player.getName())) {
			throw new IllegalArgumentException();
		}
		updatePlayer(name, player);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deletePlayer(String name) throws IllegalArgumentException {
		db.deletePlayer(name);
	}

}
