package pl.edu.agh.ki.mmorts.client.frontend.modules;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

//could be annotation specified if has button, or pointer interface
public interface ModulePresenter {
	public boolean hasMenuButton();
	public Button getButton(android.content.Context context);
	//hack now
	public View getMainModuleView(android.content.Context context, Activity activ, ViewGroup parent);
	public boolean isMainView();
}
