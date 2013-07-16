package pl.edu.agh.ki.mmorts.client.frontend.activities;

public interface UIListener {
	
	/**
	 * It should probably be a series of methods, each taking care of specific event
	 * that UI is interested in
	 * @param whathappend a way to describe what happened? could be more arguments , could not be Object
	 */
	public void stuffHappened(Object whathappend);

}
