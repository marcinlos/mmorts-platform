package pl.edu.agh.ki.mmorts.client.frontend.modules.presenters;

import android.content.Context;
import android.view.LayoutInflater;

import com.google.inject.Inject;

import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.MainSpaceManager;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.TopSpaceManager;

public abstract class AbstractModulePresenter implements ModulePresenter, ModuleDataChangedListener, BusListener{
	
	@Inject
	private Context context;
	@Inject
	protected ModulesBroker modulesBroker;
	@Inject
	protected MainSpaceManager mainSpaceManager;
	@Inject
	protected TopSpaceManager topSpaceManager;
	@Inject
	protected Bus bus;
	@Inject
	protected LayoutInflater inflater;
	protected String presenterId; 
	
	public abstract void init();

}
