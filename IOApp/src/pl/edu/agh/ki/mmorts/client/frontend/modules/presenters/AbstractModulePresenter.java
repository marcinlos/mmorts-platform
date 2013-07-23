package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

import com.google.inject.Inject;

import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;

public abstract class AbstractModulePresenter implements ModulePresenter{
	
	@Inject
	protected ModulesBroker modulesBroker;
	protected String presenterId; 

}
