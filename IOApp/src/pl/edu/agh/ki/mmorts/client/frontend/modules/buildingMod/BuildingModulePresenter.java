package pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod.MapModuleView;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.content.Context;
import android.widget.Button;

import com.google.inject.Inject;
/**
 * Presenter for building module. It is not the main presenter for any view.
 * It does not have menu button
 *
 */
public class BuildingModulePresenter extends AbstractModulePresenter{
	
	@Inject
	private Context context;
	/**
	 * This presenter is not the main presenter for this view so 
	 * it does not create it
	 */
	private MapModuleView mapModuleView;
	
	@Override
	@OnInit
	public void init() {
		
	}

	/**
	 * Does not have 
	 */
	@Override
	public boolean hasMenuButton() {
		return false;
	}

	/**
	 * Does not have
	 */
	@Override
	public Button getMenuButton() {
		return null;
	}

	@Override
	public void dataChanged(ModuleDataMessage data) {
		// TODO Auto-generated method stub
		
	}


	@Override
	public void gotMessage(PresentersMessage m) {
		// TODO Auto-generated method stub
		
	}

}
