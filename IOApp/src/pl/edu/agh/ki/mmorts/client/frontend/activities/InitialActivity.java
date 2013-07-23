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
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.app.ioapp.init.Initializer;

@ContextSingleton
public class InitialActivity extends RoboActivity{
	
	private static String ID = InitialActivity.class.getSimpleName();
	@Inject
	private Initializer initializer;
	
	
	@InjectResource(R.drawable.gummi) private Drawable gummiBearsPicture;
	@InjectView(R.id.firstLayoutEver) private LinearLayout intialScreenLayout;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(ID, "Started initial activity");
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_initial);
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
			// initializing didn't work, it's sad. Tell the user to go to the corner and cry.
			Log.e(ID,"Initializer can't initialize",e);
			endProgram();
		}
		/*
		Intent intentLogin = getIntent();
		Properties p = null;
		boolean loginSucceeded = true;   //tylko do testow -> powinno byc false
		while (!loginSucceeded) {
			try{
				Serializable o = intentLogin.getSerializableExtra(LoginActivity.PROPERTIES);
				@SuppressWarnings("rawtypes")
				HashMap map = (HashMap)o; //somehow Properties serialize into HashMap O_o
				p = new Properties();
				p.put("mail", (String)map.get("mail"));
				p.put("password", (String)map.get("password"));
			}
			catch(Exception e){
				Log.e(ID,"Properties from intent can't be loaded :(",e);
				endProgram();
			}
			String mail = p.getProperty("mail");
			String pass = p.getProperty("password");
			boolean fileExists = intentLogin.getBooleanExtra(LoginActivity.FILEEXISTS,false);
			try{
				initializer.logIn(mail, pass, fileExists);
				loginSucceeded = true;
			} catch(LogInException e){
				Log.e(ID,"Login failure, dunno: " + e.getCause());
				
			}
		}
		*/
		
		
	}
	
	private void displayPicture(){
		ImageView imageView = new ImageView(getApplicationContext());
		imageView.setImageDrawable(gummiBearsPicture);
		intialScreenLayout.addView(imageView);
		
	}
	
	public void endProgram(){
    	finish();
    	System.exit(0);
    }
	
	


}
