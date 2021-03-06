package pl.edu.agh.ki.mmorts.client.frontend.modules.loginMod;

import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

public class LoginView extends LinearLayout {
	
	private static final String ID = "LoginView";
	private static final String viewId = "LoginView";
	
	private LoginListener presenter;
	private EditText mEmailView;
	private String mEmail;
	private EditText mPasswordView;
	private String mPassword;
	
	View view;
	
	public static abstract class VisibilityListener{
		public abstract void visible();
	}
	
	private final VisibilityListener visibilityListener;
	
	 //I think this one should be called when inflating like I do
	public LoginView(Context context, LoginModulePresenter presenter, VisibilityListener visibilityListener){
		super(context);
		
		this.visibilityListener = visibilityListener;
		this.presenter = presenter;
		LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		if (inflater != null) {
			view = (LoginView)LayoutInflater.from(context).inflate(R.layout.activity_login, this, true);
			initialize();
		}
		else{
			Log.e(ID, "Inflater sie nie tworzy, ale smutek :(");
			System.exit(-1); //TODO it shouldn't stay that way, though I dunno what way it should stay
		}
		
	}
	
	private void initialize(){
		
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
			mPasswordView.setError("This field is required");
			focusView = mPasswordView;
			cancel = true;
		} else if (mPassword.length() < 4) {
			mPasswordView.setError("This password is too short");
			focusView = mPasswordView;
			cancel = true;
		}

		// Check for a valid email address.
		if (TextUtils.isEmpty(mEmail)) {
			mEmailView.setError("This field is required");
			focusView = mEmailView;
			cancel = true;
		} else if (!mEmail.contains("@")) {
			mEmailView.setError("This email address is invalid");
			focusView = mEmailView;
			cancel = true;
		}

		if (cancel) {
			// There was an error; don't attempt login and focus the first
			// form field with an error.
			focusView.requestFocus();
		} else {
			mEmailView.setTextColor(Color.GRAY);
			presenter.login(mEmail, mPassword);
		}
	}
	
	//below methods used as setters - let's hope they work as we hope they do
	
	public void invalidLogIn(){
		mEmailView.setError("Email already in use, or possibly something deeply wrong");
		mEmailView.setTextColor(Color.BLACK);
		findViewById(R.id.sign_in_button).setClickable(true);
		mEmailView.requestFocus();
	}

	public static String getViewid() {
		return viewId;
	}

	
	@Override
	protected void onWindowVisibilityChanged(int visibility) {
		super.onWindowVisibilityChanged(visibility);
		if(visibility == VISIBLE){
			visibilityListener.visible();
		}
	}

	

}
