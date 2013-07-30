package pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod;

import java.util.List;
import java.util.Map;

/**
 * Represents a content of a message sent between {@code MapModulePresenter} and {@code MapModule}
 * If {@code MapModule} gets a message with the content of {@code null} then it's a request for data.
 */
public class BuildingModuleData {
	
	 private List<BuildingInstance> l;

	public List<BuildingInstance> getL() {
		return l;
	}

	public void addBuilding(BuildingInstance b) {
		l.add(b);
	}
	
	public void removeBuilding(BuildingInstance b){
		l.remove(b);
	}
	
	

}
