package pl.edu.agh.ki.mmorts.client.frontend.modules.loginMod;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.AbstractModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.LoginDoneMessageContent;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.messages.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.messages.LoginMessageContent;
import pl.edu.agh.ki.mmorts.client.messages.ModuleDataMessage;
import android.util.Log;

public class LoginModulePresenter extends AbstractModulePresenter implements
		LoginListener {

	private static final String ID = "LoginModulePresenter"; // for Android.Log

	private LoginView view;

	/**
	 * Called after proper creation of the universe. Checks with module whether
	 * we can login from file, logs in an says that it's done or sends it's view
	 * to top and awaits login attempt
	 */
	@Override
	@OnInit
	public void init() {
		/*
		 * TODO: Contains part of initizlization Possible registerings should be
		 * move to initializer
		 */
		Log.d(ID, "Login module OnInit");
		view = createView();
		modulesBroker.registerPresenter(this, name());
		bus.register(this);
		Log.d(ID, "Login module OnInit ended");
	}

	@Override
	public void dataChanged(ModuleDataMessage data) {
		if (!data.carries(LoginMessageContent.class)) {
			throw new IllegalArgumentException("Unacceptable message passed");
		}
		LoginMessageContent m = data.getMessage(LoginMessageContent.class);

		if (m.isRequest()) {
			throw new IllegalArgumentException("Unacceptable message passed");
		}
		if (m.isLoggedIn()) {
			Log.d(ID, "Logged in");
			sendBusMessage();
		} else {
			Log.d(ID, "Log failed");
		}

	}

	/**
	 * Login doesn't care what other modules do or say to each other.
	 */
	@Override
	public void gotMessage(PresentersMessage m) {
		return;
	}

	@Override
	public void login(String login, String pass) {
		LoginMessageContent message = new LoginMessageContent.Builder().request().password(pass).login(login).build();
		modulesBroker.tellModule(new ModuleDataMessage(name(), message), name());
	}

	/**
	 * sends the I'm done message
	 */
	private void sendBusMessage() {
		PresentersMessage pm = new PresentersMessage(name(),
				new LoginDoneMessageContent());
		bus.sendMessage(pm);

	}

	private LoginView createView() {
		LoginView view = new LoginView(context, this,
				new LoginView.VisibilityListener() {
					@Override
					public void visible() {
						Log.d(ID, "Communicating with module");
						LoginMessageContent message = new LoginMessageContent.Builder().request().build();
						modulesBroker.tellModule(new ModuleDataMessage(name(), message), name());;
					}
				});
		mainSpaceManager.register(name(), view);
		mainSpaceManager.toTop(name());
		return view;
	}

}
