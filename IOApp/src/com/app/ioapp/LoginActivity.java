package com.app.ioapp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.app.ioapp.init.RegisteringChecker;
import com.app.ioapp.store.Storage;




/**
 * Activity which displays a login screen to the user
 * Creates MainActivity with intent that has two Extras:
 * Properties keyed LoginActivity.PROPERTIES instance with "mail" and "password" fields filled
 * Boolean keyed LoginActivity.FILEEXISTS whether the info was read from existing file or not (true if file existed)
 */
public class LoginActivity extends Activity {
	
	private int test;
	
	
	

	public int getTest() {
		return test;
	}

	public void setTest(int test) {
		this.test = test;
	}
	// Values for email and password at the time of the login attempt.
	private String mEmail;
	private String mPassword;
	public static final String loginFile = "info.properties";
	private final static String ID = "LoginActivity";
	public static final String PROPERTIES = "java.util.Properties";
	public static final String FILEEXISTS = "file_exists";

	// UI references.
	private EditText mEmailView;
	private EditText mPasswordView;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		/*File dir = getFilesDir();
		File f = new File(dir, loginFile);
		f.delete();*/ //uncomment this if you want to see how it goes before first login again
		File file = getFileStreamPath(loginFile);
		if(file.exists())
		{
			loginFromFile();
		}
			
		// so, file does not exist - first start of app
		
		
		
		else {
		// Set up the login form.
			mEmailView = (EditText) findViewById(R.id.email);
			mEmailView.setText(mEmail);
	
			mPasswordView = (EditText) findViewById(R.id.password);
			mPasswordView
					.setOnEditorActionListener(new TextView.OnEditorActionListener() {
						@Override
						public boolean onEditorAction(TextView textView, int id,
								KeyEvent keyEvent) {
							if (id == R.id.login || id == EditorInfo.IME_NULL) {
								attemptLogin();
								return true;
							}
							return false;
						}
					});
	
			findViewById(R.id.sign_in_button).setOnClickListener(
					new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							attemptLogin();
						}
					});
		}
	}
	
	private void loginFromFile(){
		FileInputStream loginconf=null;
		try {
			loginconf = openFileInput(loginFile);
		} catch (FileNotFoundException e) {
			Log.e(ID,"SHOULD NOT HAPPEN",e);
		}
		RegisteringChecker chk = new RegisteringChecker(loginconf);
		Properties p;
		if(chk.checkIfAccountExists()){
			Log.d(ID,"Got valid things from file, gonna start the program");
			p = chk.getProperties();
			Intent i = new Intent(this,InitialActivity.class);
			if(p == null){
				Log.e(ID,"Invalid login file content");
			}
			i.putExtra(PROPERTIES, p);
			i.putExtra(FILEEXISTS, true);
			
			startActivity(i);
			finish();
		}
		else{
			Log.e(ID,"File exists but has no properties? SHOULD NOT HAPPEN");
			endProgram();
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	/**
	 * Attempts to log in with data filled into forms, starts MainActivity with that data
	 * put into intent extra.
	 * If there are form errors (invalid email, missing fields, etc.), the
	 * errors are presented and no actual login attempt is made.
	 */
	public void attemptLogin() {

		// Reset errors.
		mEmailView.setError(null);
		mPasswordView.setError(null);

		// Store values at the time of the login attempt.
		mEmail = mEmailView.getText().toString();
		mPassword = mPasswordView.getText().toString();

		boolean cancel = false;
		View focusView = null;

		// Check for a valid password.
		if (TextUtils.isEmpty(mPassword)) {
			mPasswordView.setError(getString(R.string.error_field_required));
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError(getString(R.string.error_invalid_password));
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError(getString(R.string.error_field_required));
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError(getString(R.string.error_invalid_email));
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			Log.d(ID,"Got valid things in fields, gonna start the program");
			Properties p = new Properties();
			p.put("mail", mEmail);
			p.put("password", mPassword);
			Intent i = new Intent(this,RunningActivity.class);
			i.putExtra(PROPERTIES, p);
			i.putExtra(FILEEXISTS, false);
			startActivity(i);
			finish();
		}
	}
	public void endProgram(){
    	finish();
    	System.exit(0);
    }
}
