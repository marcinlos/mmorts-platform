package pl.edu.agh.ki.mmorts.client.frontend.spaceManaging;

import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ViewAlreadyRegisteredException;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ViewNotRegisteredException;
import android.util.Log;
import android.view.View;

import com.app.ioapp.init.Initializer;
import com.google.inject.Inject;

public class ConcreteMainSpaceManager implements MainSpaceManager {
	
	private static final String ID = ConcreteMainSpaceManager.class.getSimpleName();

	@Inject(optional=true) private  Initializer initializer;
	
	private Map<String, View> viewMap = new HashMap<String, View>();
	
	private View topView;
	
	@OnInit
	public void onInit(){
		Log.d(ID, "OnInit fired");
		Log.d(ID,String.format("%s", initializer.getMainScreenView()));
	}

	/**
	 * only register views with visibility.GONE
	 */
	@Override
	public void register(String id, View view) throws ViewAlreadyRegisteredException{
		if (viewMap.containsKey(id)) {
			throw new ViewAlreadyRegisteredException("Your ID is not as unique as you think");
		}
		viewMap.put(id, view);
		
	}

	@Override
	public void unregister(String id) throws ViewNotRegisteredException{
		if (!viewMap.containsKey(id)) {
			throw new ViewNotRegisteredException();
		}
		viewMap.remove(id);
		
	}

	@Override
	public void toTop(String id) {
		if(topView != null){
			topView.setVisibility(View.GONE);
		}
		topView = viewMap.get(id);
		topView.setVisibility(View.VISIBLE);
	}

	@Override
	public View getTop() {
		return topView;
	}

}
