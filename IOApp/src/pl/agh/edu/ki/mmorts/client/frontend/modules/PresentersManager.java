package pl.agh.edu.ki.mmorts.client.frontend.modules;

import java.util.HashMap;
import java.util.Map;

public class PresentersManager {
	
	private Map<String, ModulePresenter> presentersMap = new HashMap<String, ModulePresenter>();
	
	
	
	public PresentersManager(Map<String, ModulePresenter> presentersMap) {
		this.presentersMap = presentersMap;
	}



	public ModulePresenter get(String name){
		return presentersMap.get(name);
	}
	
}
