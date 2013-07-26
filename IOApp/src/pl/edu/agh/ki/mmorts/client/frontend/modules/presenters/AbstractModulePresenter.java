package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

import com.google.inject.Inject;

import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.MainSpaceManager;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.TopSpaceManager;

public abstract class AbstractModulePresenter implements ModulePresenter, ModuleDataChangedListener, BusListener{
	
	@Inject(optional = true)
	protected ModulesBroker modulesBroker;
	@Inject(optional = true)
	protected MainSpaceManager mainSpaceManager;
	@Inject(optional = true)
	protected TopSpaceManager topSpaceManager;
	@Inject(optional = true)
	protected Bus bus;
	
	
	protected String presenterId; 
	
	public abstract void init();

}
