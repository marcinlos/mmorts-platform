package protocol.buildingsModule;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * Represents a content of a message sent between {@code MapModulePresenter} and {@code MapModule}
 * If {@code MapModule} gets a message with the content of {@code null} then it's a request for data.
 */
public class BuildingModuleData implements Serializable {
	
	private List<BuildingInstance> buildings = new ArrayList<BuildingInstance>();

	public List<BuildingInstance> getBuildings() {
		return buildings;
	}

	public void addBuilding(BuildingInstance b) {
		buildings.add(b);
	}
	
	public void removeBuilding(BuildingInstance b){
		buildings.remove(b);
	}
	
	
	public void removeBuilding(int x, int y) {
		Iterator<BuildingInstance> i = buildings.iterator();
		while(i.hasNext()){
			BuildingInstance b = i.next();
			if(b.getColumn() == x && b.getRow() == y){
				i.remove();
			}
		}
	}
	

}
