package pl.edu.agh.ki.mmorts.client.frontend.modules;

import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModulePresenter;

public interface ModulesBroker {
	
	public void registerPresenter(String moduleName);
	
	public void unregisterPresenter(ModulePresenter p);
	
	//TODO? Dunno what more

}
