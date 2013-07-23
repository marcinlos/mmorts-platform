package pl.edu.agh.ki.mmorts.client.frontend.spaceManaging;

import com.app.ioapp.init.Initializer;
import com.google.inject.Inject;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import roboguice.inject.InjectView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class ConcreteTopSpaceManager implements TopSpaceManager{

	
	
	private static final String ID = ConcreteTopSpaceManager.class.getSimpleName();

//	@InjectView(R.id.topSpace) ViewGroup managedTopSpace;
//	@Inject(optional = true) LayoutInflater inflater;

	@Inject(optional=true) private  Initializer initializer;

	
	
	@OnInit
	public void onInit(){
		Log.d(ID, "OnInit fired");
		View view  = initializer.getTopView();
		Log.d(ID,String.format("%s", view));
		Log.d(ID, String.format("Main is: %s", initializer.getMainScreenView()));

	}
	
	@Override
	public View getIconView() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public View getStatView() {
		// TODO Auto-generated method stub
		return null;
	}

}
