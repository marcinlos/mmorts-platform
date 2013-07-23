package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

import com.google.inject.Inject;

import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.MainSpaceManager;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.TopSpaceManager;

public abstract class AbstractModulePresenter implements ModulePresenter, ModuleDataChangedListener, BusListener{
	
	@Inject
	protected ModulesBroker modulesBroker;
	@Inject
	protected MainSpaceManager mainSpaceManager;
	@Inject
	protected TopSpaceManager topSpaceManager;
	protected String presenterId; 

}
