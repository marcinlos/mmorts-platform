package pl.edu.agh.ki.mmorts.server.data;

/**
 * Exceptions signals that connection to db can't be obtained
 * 
 * @author drew
 * 
 */
public class NoConnectionException extends Exception {

	/**
	 * {@inheritDoc}
	 */
	public NoConnectionException() {
		super();
	}

	/**
	 * {@inheritDoc}
	 */
	public NoConnectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	/**
	 * {@inheritDoc}
	 */
	public NoConnectionException(String arg0) {
		super(arg0);
	}

	/**
	 * {@inheritDoc}
	 */
	public NoConnectionException(Throwable arg0) {
		super(arg0);
	}

}
