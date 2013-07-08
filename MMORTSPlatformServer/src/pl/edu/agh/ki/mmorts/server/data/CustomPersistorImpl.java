package pl.edu.agh.ki.mmorts.server.data;

import javax.inject.Inject;



/**
 * Implementation of {@link CustomPersistor} which generally wraps raw {@link Database}
 * to achieve typing. There is nothing special here, except for checking for
 * some additional logic correctness which is not done at {@link Database} level.
 * 
 * 
 * @author drew
 *
 */
public class CustomPersistorImpl implements CustomPersistor {
	
	
	/**
	 * Represents underlying database to persist the data in
	 * Annotated with {@link Inject}
	 */
	@Inject
	private Database db;
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createBinding(String moduleName, String playerName, Object o)
			throws IllegalArgumentException {
		db.createBinding(moduleName, playerName, o);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T receiveBinding(String moduleName, String playerName, Class<T> clazz)
			throws IllegalArgumentException {
		return clazz.cast(db.receiveBinding(moduleName, playerName));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinding(String moduleName, String playerName, Object o)
			throws IllegalArgumentException {
		db.updateBinding(moduleName, playerName, o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteBinding(String moduleName, String playerName)
			throws IllegalArgumentException {
		db.deleteBinding(moduleName, playerName);
	}

}
