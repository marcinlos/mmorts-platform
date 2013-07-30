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

}
