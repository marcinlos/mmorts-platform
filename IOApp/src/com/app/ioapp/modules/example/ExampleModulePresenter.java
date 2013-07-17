package com.app.ioapp.modules.example;


import com.google.inject.Inject;

import pl.edu.agh.ki.mmorts.client.backend.core.annotations.OnInit;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ModulesBroker;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.BusListener;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModuleDataMessage;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.ModulePresenter;
import pl.edu.agh.ki.mmorts.client.frontend.modules.presenters.PresentersMessage;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.MainSpaceManager;
import pl.edu.agh.ki.mmorts.client.frontend.spaceManaging.TopSpaceManager;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

public class ExampleModulePresenter implements ModulePresenter, BusListener{
	
	private static final String ID = "ExampleModulePresenter";
	
	@Inject
	private Context context;
	
	@Inject
	private TopSpaceManager topSpaceManager;
	
	@Inject
	private MainSpaceManager mainSpaceManager;
	
	@Inject
	private ModulesBroker modulesBroker;

	@Override
	public boolean hasMenuButton() {
		return true;
	}

	@Override
	public Button getMenuButton() {
		Button button = new Button(context);
		button.setText("Example");
		return button;
	}

	public boolean isMainView() {
		return false;
	}

	public View getMainModuleView(Context context, Activity activ, ViewGroup parent) {
		//View toReturn = activ.findViewById(R.layout.example_layout);
		//View toReturn = activ.getLayoutInflater().inflate(R.layout.example_layout, null);
		
		LinearLayout horizontal = new LinearLayout(context);
		horizontal.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		Button button = new Button(context);
		button.setText("VIEWS");
		button.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT));
		horizontal.addView(button);
		return horizontal;
	}


	@Override
	public void dataChanged(ModuleDataMessage data) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void gotMessage(PresentersMessage m) {
		// TODO Auto-generated method stub
		
	}
	
	@OnInit
	public void onInit() {
		Log.d(ID, "context:");
		Log.d(ID, String.format("%s", context));
		Log.d(ID, "topSpaceManager:");
		Log.d(ID, String.format("%s", topSpaceManager));
		Log.d(ID, " mainSpaceManager:");
		Log.d(ID, String.format("%s", mainSpaceManager));
		Log.d(ID, "modulesBroker:");
		Log.d(ID, String.format("%s", modulesBroker));
	}
	
}
