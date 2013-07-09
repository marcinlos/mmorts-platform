package com.app.ioapp.customDroidViews;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Button;

import com.app.ioapp.MainActivity;

public class MenuButton extends Button {

	private AbstractModuleView v;
	private MainActivity context;
	
	public MenuButton(Context context) {
		super(context);
		if(context instanceof MainActivity)
			this.context = (MainActivity) context;
		// TODO Auto-generated constructor stub
	}
	public MenuButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		if(context instanceof MainActivity)
			this.context = (MainActivity) context;
		// TODO Auto-generated constructor stub
	}
	public MenuButton(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		if(context instanceof MainActivity)
			this.context = (MainActivity) context;
		// TODO Auto-generated constructor stub
	}
	
	public void setView(AbstractModuleView view){
		v = view;
	}
	
	public AbstractModuleView getView(){
		return v;
	}
	
	public void iWasClicked(){
		//TODO
	}

}
