package com.app.ioapp;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.app.ioapp.view.MainView;

import android.app.Activity;
import android.util.SparseArray;
import android.widget.Button;

public class MenuManager {
	
	private SparseArray<String> buttons;
	private MainActivity main;
	private MainView v;
	
	public MenuManager(MainActivity a, MainView v){
		this.buttons = new SparseArray<String>();
		main = a;
		this.v = v;
	}
	
	public void addButton(String moduleName, String text){
		int id = main.addButton(text);
		buttons.append(id, moduleName);
	}
	
	public void onClick(int id){
		String s = buttons.get(id);
		v.handleMenuAction(s);
	}

}
