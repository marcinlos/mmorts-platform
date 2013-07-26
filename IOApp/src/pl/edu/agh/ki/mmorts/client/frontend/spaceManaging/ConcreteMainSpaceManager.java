package pl.edu.agh.ki.mmorts.client.frontend.spaceManaging;

import java.util.HashMap;
import java.util.Map;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ViewAlreadyRegisteredException;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ViewNotRegisteredException;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

import com.app.ioapp.init.Initializer;
import com.google.inject.Inject;

public class ConcreteMainSpaceManager implements MainSpaceManager {
	
	private static final String ID = ConcreteMainSpaceManager.class.getSimpleName();

	@Inject(optional=true) private  Initializer initializer;
	
	private Map<String, View> viewMap = new HashMap<String, View>();
	
	private View topView;
	
	private LinearLayout mainSpace;
	
	@OnInit
	public void onInit(){
		Log.d(ID, "OnInit fired");
		Log.d(ID,String.format("%s", initializer.getMainModulesView()));
		mainSpace = (LinearLayout) initializer.getMainModulesView();
	}

	/**
	 * only register views with visibility.GONE
	 */
	@Override
	public void register(String id, View view) throws ViewAlreadyRegisteredException{
		Log.d(ID, String.format("Registered view: %s", view));
		if (viewMap.containsKey(id)) {
			throw new ViewAlreadyRegisteredException("Your ID is not as unique as you think");
		}
		viewMap.put(id, view);
		view.setVisibility(View.GONE);
		mainSpace.addView(view);
		
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
		Log.d(ID, String.format("Pushing to top %s", id));
		if(topView != null){
			topView.setVisibility(View.GONE);
		}
		topView = viewMap.get(id);
		topView.setVisibility(View.VISIBLE);
		topView.postInvalidate();
		topView.invalidate();
	}

	@Override
	public View getTop() {
		return topView;
	}

	@Override
	public View getViewById(String id) {
		return viewMap.get(id);
	}

}
