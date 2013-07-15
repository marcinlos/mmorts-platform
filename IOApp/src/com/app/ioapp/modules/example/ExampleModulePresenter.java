package com.app.ioapp.modules.example;

import android.app.Activity;
import android.content.Context;
import android.text.Layout;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;

import com.app.ioapp.R;
import com.app.ioapp.modules.ModulePresenter;

public class ExampleModulePresenter implements ModulePresenter{

	@Override
	public boolean hasMenuButton() {
		return true;
	}

	@Override
	public Button getButton(Context context) {
		Button button = new Button(context);
		button.setText("Example");
		return button;
	}

	@Override
	public boolean isMainView() {
		return false;
	}

	@Override
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
	
}
