package com.app.ioapp;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Properties;

import com.app.ioapp.init.InitException;
import com.app.ioapp.init.Initializer;
import com.app.ioapp.login.LogInException;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity{
	
	private static final String ID = "StartActivity";
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		initialize();
		
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
		
		Intent intentRun = new Intent(this, RunActivity.class);
		startActivity(intentRun);
		finish();
		
	}
	
	public void endProgram(){
    	finish();
    	System.exit(0);
    }
	
	


}
