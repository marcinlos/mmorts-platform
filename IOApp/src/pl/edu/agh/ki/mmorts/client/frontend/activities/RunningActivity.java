package pl.edu.agh.ki.mmorts.client.frontend.activities;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContextSingleton;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.app.ioapp.init.Initializer;
import com.google.inject.Inject;

@ContextSingleton
public class RunningActivity extends RoboActivity {

	private static final String ID = RunningActivity.class.getSimpleName();
	
	@Inject(optional= true)
	Initializer initializer;
	
	private View view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(ID, String.format("Creating running activity"));
		super.onCreate(savedInstanceState);
		view  = initializer.getMainScreenView();
		setContentView(view);
		view.invalidate();
		Log.d(ID, String.format("Running activity started"));
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		view.invalidate();
	}

}
