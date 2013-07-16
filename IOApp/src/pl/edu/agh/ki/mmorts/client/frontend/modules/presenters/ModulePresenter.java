package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//could be annotation specified if has button, or pointer interface
public interface ModulePresenter {
	//Might need context for Button and View creation
	
	/**
	 * 
	 * @return whether this presenter has a button to show in Menu
	 */
	public boolean hasMenuButton();
	/**
	 * 
	 * @return configured button to use in menu
	 */
	public Button getMenuButton();
	/**
	 * method used as a listener in module - receives concrete object with new data.
	 */
	public void dataChanged(); //TODO
}
