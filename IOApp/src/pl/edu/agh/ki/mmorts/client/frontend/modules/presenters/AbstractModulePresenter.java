package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.MainSpaceManager;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.TopSpaceManager;
import android.content.Context;
import android.widget.Button;

import com.google.inject.Inject;

public abstract class AbstractModulePresenter implements ModulePresenter, ModuleDataChangedListener, BusListener{
	
	@Inject(optional = true)
	protected Context context;
	//@Inject(optional = true)
	//DEBUUUUUUUUUUUUUUUUUUUUUUUUUG TODO
	protected ModulesBroker modulesBroker = new ModulesBrokerDummy();
	//DEGUUUUUUUUUUUUUUUUUUUUUUUUUG
	@Inject(optional = true)
	protected MainSpaceManager mainSpaceManager;
	@Inject(optional = true)
	protected TopSpaceManager topSpaceManager;
	@Inject(optional = true)
	protected Bus bus;
	protected String presenterId; 

	@Override
	public boolean hasMenuButton() {
		return false;
	}

	@Override
	public Button getMenuButton() {
		return null;
	}
	
	public abstract void init();

}
