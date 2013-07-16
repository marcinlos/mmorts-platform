 package pl.edu.agh.ki.mmorts.client.frontend.activities;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

import pl.edu.agh.ki.mmorts.client.backend.init.InitException;
import pl.edu.agh.ki.mmorts.client.backend.loginMod.LogInException;

import com.app.ioapp.R;
import com.app.ioapp.init.Initializer;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class InitialActivity extends Activity{
	
	private static final String ID = InitialActivity.class.getName();
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.activity_initial);
		displayPicture();
		
		initialize();

		Intent intentRun = new Intent(this, RunningActivity.class);
		startActivity(intentRun);
		finish();
		
	}

	private void initialize() {
		
		Initializer initializer = new Initializer(this);
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
		Resources res = getResources();
		Drawable drawable = res.getDrawable(R.drawable.gummi);
		imageView.setImageDrawable(drawable);
		LinearLayout l = (LinearLayout) findViewById(R.id.firstLayoutEver);
		l.addView(imageView);
		
	}
	
	public void endProgram(){
    	finish();
    	System.exit(0);
    }
	
	


}
