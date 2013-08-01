package pl.edu.agh.ki.mmorts.client.backend.modules.mapModule;

import pl.edu.agh.ki.mmorts.client.backend.modules.ModuleBase;
import pl.edu.agh.ki.mmorts.client.frontend.modules.GUICommModule;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;

import com.google.inject.Inject;

public class MapModule extends ModuleBase implements GUICommModule{
	
	private static final String ID = "MapModule";
	
	@Inject(optional= true)
	ModulesBroker modulesBroker;

	@Override
	public void dataChanged(ModuleDataMessage data) {
		// jak w BuildingModule
		
	}
	

}
