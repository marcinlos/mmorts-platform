package com.app.ioapp;

import android.util.SparseArray;

import com.app.ioapp.view.MainView;

public class MenuManager {
	
	private SparseArray<String> buttons;
	private MainActivity main;
	private MainView v;
	
	public MenuManager(MainActivity a, MainView v){
		this.buttons = new SparseArray<String>();
		main = a;
		this.v = v;
	}
	
	/**
	 * called (possibly from view?) by modules who want to have menu button
	 * Current implementation restricts use to one button per module
	 * It could be expanded to include
	 * @param moduleName button requesting the button, used in {@link #onClick} to call back
	 * @param text text to be displayed on button
	 */
	public void addButton(String moduleName, String text){
		int id = main.addMenuButton(text);
		buttons.append(id, moduleName);
	}
	
	/**
	 * called by each button, sends info that button was clicked through view to module by name
	 * @param id id of the button clicked
	 */
	public void onClick(int id){
		String s = buttons.get(id);
		v.handleMenuAction(s);
	}

}
