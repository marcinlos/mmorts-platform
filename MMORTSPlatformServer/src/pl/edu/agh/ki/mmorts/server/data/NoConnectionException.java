package pl.edu.agh.ki.mmorts.server.data;

public class NoConnectionException extends Exception {

	public NoConnectionException() {
		super();
	}

	public NoConnectionException(String arg0, Throwable arg1) {
		super(arg0, arg1);
	}

	public NoConnectionException(String arg0) {
		super(arg0);
	}

	public NoConnectionException(Throwable arg0) {
		super(arg0);
	}

}
