package pl.edu.agh.ki.mmorts.client.frontend.spaceManaging;

import android.view.View;

public interface MainSpaceManager {
	
	void register(String id, View view);
	void unregister(String id);
	void toTop(String id);
	View getTop();

}
