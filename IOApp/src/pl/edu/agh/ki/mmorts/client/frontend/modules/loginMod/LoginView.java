package pl.edu.agh.ki.mmorts.client.frontend.modules.loginMod;

import pl.edu.agh.ki.mmorts.client.frontend.modules.infMod.InfrastructureModulePresenter;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class LoginView extends View {
	
	private LoginModulePresenter presenter;
	
	public LoginView(Context context) {
		super(context);
	}

	public LoginModulePresenter getPresenter() {
		return presenter;
	}

	public void setPresenter(LoginModulePresenter presenter) {
		this.presenter = presenter;
	}

}
