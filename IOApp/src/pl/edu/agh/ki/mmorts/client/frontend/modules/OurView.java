package pl.edu.agh.ki.mmorts.client.frontend.modules;


/**
 * created just to have one place to store javadocs about these functions which should be pretty universal
 *
 */
public interface OurView {
	
	/**
	 * Every view that wants to draw or receive events or something like that needs to call this
	 * and pass himself.
	 * @param listener
	 */
	public void addListener(ViewListener listener);
	
	/**
	 * adds listeners to every action worth listening to, and sends info about those actions happening
	 * to every registered listener.
	 */
	public void createListeners();
	
	/**
	 * method called from presenter tell the view that action is finished, and how it ended.
	 * Usefull only when view needs to do some cleaning up after an action (show message
	 * about failure, close some open menus, ask modules for another sets of what to draw...).
	 * Note, that this may not be needed in most cases (it's not used in example), but is here just in case.
	 * @param result information how the action ended in case what view needs to do depends on it
	 */
	public void actionFinished(boolean result);

}
