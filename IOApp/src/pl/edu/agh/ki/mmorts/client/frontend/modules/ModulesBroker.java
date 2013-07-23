package pl.edu.agh.ki.mmorts.client.frontend.modules;

import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModulePresenter;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;

public interface ModulesBroker {
	
	public void registerPresenter(String moduleName);
	
	public void unregisterPresenter(ModulePresenter p);
	
	public void tellModule(ModuleDataMessage m, String moduleName);
	
	//TODO? Dunno what more

}
