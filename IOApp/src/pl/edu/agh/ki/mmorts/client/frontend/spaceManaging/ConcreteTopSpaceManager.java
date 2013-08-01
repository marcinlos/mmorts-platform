package pl.edu.agh.ki.mmorts.client.frontend.spaceManaging;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import android.util.Log;
import android.view.View;

import com.app.ioapp.init.Initializer;
import com.google.inject.Inject;

public class ConcreteTopSpaceManager implements TopSpaceManager{

	
	
	private static final String ID = ConcreteTopSpaceManager.class.getSimpleName();

//	@InjectView(R.id.topSpace) ViewGroup managedTopSpace;
//	@Inject(optional = true) LayoutInflater inflater;

	@Inject(optional=true) private  Initializer initializer;
	
	//private Map<String, View> icons = new HashMap<String,View>();
	//private Map<String, View> stats = new HashMap<String,View>();
	
	private View topView;

	
	
	@OnInit
	public void onInit(){
		Log.d(ID, "OnInit fired");
		topView  = initializer.getTopView();
		Log.d(ID,String.format("%s", topView));
		Log.d(ID, String.format("Main is: %s", initializer.getMainScreenView()));

	}
	
	@Override
	public View getIconView() {
		return topView.findViewById(R.id.iconSpace);
	}

	@Override
	public View getStatView() {
		return topView.findViewById(R.id.statSpace);
	}

}
