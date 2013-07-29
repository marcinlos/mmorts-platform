package pl.edu.agh.ki.mmorts.client.frontend.activities;

import javax.inject.Inject;

import pl.edu.agh.ki.mmorts.client.backend.init.InitException;
import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import roboguice.activity.RoboActivity;
import roboguice.inject.ContextSingleton;
import roboguice.inject.InjectResource;
import roboguice.inject.InjectView;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.ioapp.init.Initializer;
import com.google.inject.spi.HasDependencies;

@ContextSingleton
public class InitialActivity extends RoboActivity {

	private static String ID = InitialActivity.class.getSimpleName();
	@Inject
	private Initializer initializer;

	@InjectResource(R.drawable.gummi)
	private Drawable gummiBearsPicture;

	ViewGroup mainLayout;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(ID, "Started initial activity");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_initial);
		mainLayout = (ViewGroup) findViewById(R.id.firstLayoutEver);
		displayPicture();
	}

	@Override
	protected void onResume() {
		super.onResume();
		initialize();
		Intent intentRun = new Intent(this, RunningActivity.class);
		startActivity(intentRun);
		finish();
	}

	private void initialize() {

		try {
			initializer.initialize();
		} catch (InitException e) {
			// initializing didn't work, it's sad. Tell the user to go to the
			// corner and cry.
			Log.e(ID, "Initializer can't initialize", e);
			endProgram();
		}

	}

	private void displayPicture() {
		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setImageDrawable(gummiBearsPicture);
		mainLayout.addView(imageView);
		mainLayout.invalidate();
	}

	public void endProgram() {
		finish();
		System.exit(0);
	}

}
