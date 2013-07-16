package pl.edu.agh.ki.mmorts.client.backend.data;

import android.util.Log;

import com.google.inject.Inject;



/**
 * Implementation of {@link CustomPersistor} which generally wraps raw {@link Database}
 * to achieve typing. There is nothing special here, except for checking for
 * some additional logic correctness which is not done at {@link Database} level.
 * 
 * Compare javadocs for {@link Database} and {@link CustomPersistor} for see what 
 * additional checks are done on concrete methods.
 * 
 *
 */
public class CustomPersistorImpl implements CustomPersistor {
	
	private static final String ID = "CustomPersistorImpl";
	/**
	 *	Represents underlying database to persist the data in
	 */
	@Inject
	private Database db;
	
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void createBinding(String moduleName, String playerName, Object o)
			throws IllegalArgumentException {
		Log.d(ID, "create binding: " + moduleName + ", " + playerName);
		db.createBinding(moduleName, playerName, o);

	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public <T> T receiveBinding(String moduleName, String playerName, Class<T> clazz)
			throws IllegalArgumentException {
		Log.d(ID, "receive binding: " + moduleName + ", " + playerName);
		return clazz.cast(db.receiveBinding(moduleName, playerName));
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void updateBinding(String moduleName, String playerName, Object o)
			throws IllegalArgumentException {
		Log.d(ID, "update binding: " + moduleName + ", " + playerName);
		db.updateBinding(moduleName, playerName, o);
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void deleteBinding(String moduleName, String playerName)
			throws IllegalArgumentException {
		Log.d(ID, "delete binding: " + moduleName + ", " + playerName);
		db.deleteBinding(moduleName, playerName);
	}

}
