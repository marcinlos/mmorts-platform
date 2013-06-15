package com.app.ioapp;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.ioapp.config.ConfigException;
import com.app.ioapp.config.StaticPropertiesLoader;
import com.app.ioapp.customDroidViews.AdditionalViewA;
import com.app.ioapp.customDroidViews.BoardView;
import com.app.ioapp.init.Initializer;
import com.app.ioapp.modules.Infrastruture;
import com.app.ioapp.modules.ITile;
import com.app.ioapp.modules.Tile;
import com.app.ioapp.view.MainView;

public class MainActivity extends Activity implements UIListener {
	
	private static final String ID = "MainActivity";
	private static final String CONFIG_FILE = "client.properties";
	private Initializer initializer;
	private Infrastruture board;
	private BoardView boardView;
	private LinearLayout menu;
	private MenuManager manager;
	private Properties boardConfig;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initialize();
		
		
		MainView v = initializer.getView();
		manager = new MenuManager(this,v);
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		boardView = new BoardView(this);
		board = new Infrastruture(boardConfig);
		board.setView(boardView);
		setupBoard();
		
		layout.addView(boardView);
		
		//createMenu(mainLayout);
		menu = new LinearLayout(this);
		menu.setWeightSum(1);
		menu.setOrientation(LinearLayout.VERTICAL);
		menu.setBackgroundColor(Color.CYAN);
		mainLayout.addView(menu);
		
		
		
		
	}
	
	/**
	 * receives intent that created this activity and reads it
	 * creates Initializer properly (in compliance with {@link #Initializer} constructor)
	 * creates {@link #boardConfig} from config file
	 */
	private void initialize(){
		Intent intent = getIntent();
		Properties p = null;
		try{
			Serializable o = intent.getSerializableExtra(LoginActivity.PROPERTIES);
			@SuppressWarnings("rawtypes")
			HashMap map = (HashMap)o; //somehow Properties serialize into HashMap O_o
			p = new Properties();
			p.put("mail", (String)map.get("mail"));
			p.put("password", (String)map.get("password"));
		}
		catch(Exception e){
			Log.e(ID,"Properties from intent can't be loaded :(",e);
			endProgram();
		}
		
		
		String mail = p.getProperty("mail");
		String pass = p.getProperty("password");
		boolean fileExists = intent.getBooleanExtra(LoginActivity.FILEEXISTS,false);
		FileOutputStream fos = null;
		FileInputStream fis = null;
		InputStream i;
		try {
			i = getResources().getAssets().open(CONFIG_FILE);
			boardConfig = StaticPropertiesLoader.load(i);
			
		} catch (IOException e) {
			Log.e(ID,"config file error",e);
		}
		try{
			if(!fileExists){
				 fos = openFileOutput(LoginActivity.loginFile,MODE_PRIVATE);
			}
			fis = openFileInput(LoginActivity.loginFile);
		}
		catch(FileNotFoundException e){
			Log.e(ID,"Directory for apps internal files not existing or something... it's bad",e);
			endProgram();
		}
		
		try {
			initializer = new Initializer(mail, pass, fileExists, i, fos);
		} catch (ConfigException e) {
			Log.e(ID,"Initializer is bad",e);
			endProgram();
		} catch (IOException e) {
			Log.e(ID,"Initializer is bad",e);
			endProgram();
		}
		
		
	}
	
	/**
	 * see {@link #MenuManager.addButton}
	 * @param name Text displayed by the button
	 * @return ID of button created
	 */
	public int addMenuButton(String name){
		Button b = new Button(this);
		b.setText(name);
		
		OnClickListener ocl = new OnClickListener(){
		    @Override
		    public void onClick(View v){
		        manager.onClick(v.getId());
		    }
		};
		
		b.setOnClickListener(ocl);
		menu.addView(b);
		menu.invalidate();
		return b.getId();
	}
	
	
	private void setupBoard(){
		Log.e(ID, "setupBoard - it's debug only procedure!");
		List<ITile> tiles = new ArrayList<ITile>();
		Tile tile1 = new Tile("tile_fill",0,0,1,1);
		tiles.add(tile1);
		Tile tile2 = new Tile("tile_fill",2,2,1,1);
		tiles.add(tile2);
		Tile tile3 = new Tile(BitmapFactory.decodeResource(getResources(),R.drawable.tile_1x2),5,3,1,2);
		tiles.add(tile3);
		
		board.setupFields(tiles);
	}
	
	@Override
	public void onWindowFocusChanged (boolean hasFocus) {
		boardView.invalidate();
	}


	@Override
	public void stuffHappened(Object whathappend) {
		//TODO
		// REACT to stuff that happened
		// it probably won't be this activity, but some activity will do it
		
	}

	/**
	 *  {@inheritDoc}
	 */
	@Override
	public void showMenuForModule(String name) {
		setContentView(R.layout.activity_menu);
		LinearLayout layout = (LinearLayout) findViewById(R.id.menu_layout);
		TextView a = new TextView(this);
		a.setText("menu here");
		layout.addView(a);
		if(name.equals("examplemodulename")){
			//do stuff this module would need
		}
		else if(name.equals("anotherexamplemodulename")){
			//do stuff
		}
		//...
		else{
			AdditionalViewA va = new AdditionalViewA(this);
			layout.addView(va);
		}
		
		
	}
	
	/**
	 * called from menu_layout as a signal that we can return to main display mode
	 * @param v unused
	 */
	public void endMenu(View v){
		setContentView(R.layout.activity_main);
	}
	
	public void endProgram(){
    	finish();
    	System.exit(0);
    }

}
