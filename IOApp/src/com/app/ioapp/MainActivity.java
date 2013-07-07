package com.app.ioapp;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.security.auth.login.LoginException;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.ioapp.config.ConfigException;
import com.app.ioapp.config.StaticPropertiesLoader;
import com.app.ioapp.customDroidViews.AdditionalViewA;
import com.app.ioapp.customDroidViews.BoardView;
import com.app.ioapp.customDroidViews.MenuButton;
import com.app.ioapp.customDroidViews.AbstractModuleView;
import com.app.ioapp.init.InitException;
import com.app.ioapp.init.Initializer;
import com.app.ioapp.login.LogInException;
import com.app.ioapp.login.RegisterException;
import com.app.ioapp.modules.InfrastructureModule;
import com.app.ioapp.modules.ITile;
import com.app.ioapp.modules.Module;
import com.app.ioapp.modules.Tile;
import com.app.ioapp.view.MainView;

public class MainActivity extends Activity implements UIListener {
	
	private static final List<String> arbitraryViewsList = new ArrayList<String>();
	private static final String ID = "MainActivity";
	private static final String CONFIG_FILE = "client.properties";
	private Initializer initializer;
	private InfrastructureModule board;
	private BoardView boardView;
	private LinearLayout menu;
	private Properties boardConfig; //debug only
	private Map<String,Module> modules;
	private MainView view;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		fillViewsList();
		setContentView(R.layout.activity_main);
		initialize();
		modules = initializer.getModules();
		getMainView();
		
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		
		//this part is debug only
		boardView = new BoardView(this);
		board = new InfrastructureModule(boardConfig);
		boardView.init("InfrastructureModule", view);
		boardView.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            if (event.getAction() == MotionEvent.ACTION_DOWN){
	              //  textView.setText("Touch coordinates : " +
	              //          String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
	            	AbstractModuleView view = (AbstractModuleView) v;
	            	view.iWasClicked(event.getX(),event.getY());
	            }
	            return true;
	        }
	    });
		setupBoard();
		
		layout.addView(boardView);
		//end of debugonly part
		
		
		//createMenu(mainLayout);
		menu = new LinearLayout(this);
		menu.setWeightSum(1);
		menu.setOrientation(LinearLayout.VERTICAL);
		menu.setBackgroundColor(Color.CYAN);
		mainLayout.addView(menu);
		try {
			fillMenu();
		} catch (ClassNotFoundException e) {
			Log.e(ID,"No View with that name implemented - do it!",e);
			endProgram();
		} catch (InstantiationException e) {
			Log.e(ID,"Can't instantiate your view? What?",e);
			endProgram();
		} catch (IllegalAccessException e) {
			Log.e(ID,"Can't access your view? What?",e);
			endProgram();
		}

		
		
		
		
	}
	
	private void fillViewsList(){
		arbitraryViewsList.add("BoardView");
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
		InputStream i=null;
		InputStream iceConfigStream = null;
		InputStream jsonConfigStream = null;
		try {
			i = getResources().getAssets().open(CONFIG_FILE);
			iceConfigStream = getResources().getAssets().open("iceClient.config");
			jsonConfigStream = getResources().getAssets().open("modules.json");
			boardConfig = StaticPropertiesLoader.load(i); //debug only action
			
		} catch (IOException e) {
			Log.e(ID,"config file error",e);
		}
		try{
			if(!fileExists){
				 fos = openFileOutput(LoginActivity.loginFile,MODE_PRIVATE);
			}
		}
		catch(FileNotFoundException e){
			Log.e(ID,"Directory for apps internal files not existing or something... it's bad",e);
			endProgram();
		}
		
		try {
			initializer = new Initializer(i, jsonConfigStream, iceConfigStream, fos);
		} catch (ConfigException e) {
			Log.e(ID,"Initializer is bad",e);
			endProgram();
		}
		
		try {
			initializer.initialize();
		} catch (InitException e) {
			// initializing didn't work, it's sad. Tell the user to go to the corner and cry.
			Log.e(ID,"Initializer can't initialize",e);
			endProgram();
		}
		try{
			initializer.logIn(mail, pass, fileExists);
		} catch(LogInException e){
			Log.e(ID,"Login failure, dunno");
			//you cooould come back to Login Activity if you'd really like here, it would be appropiate.
			endProgram();
		}
		
		
	}
	
	private void getMainView(){
		view = initializer.getMainView();
	}
	
	/**
	 * Adds all the buttons needed to the menu. Used only after {@link modules} are filled.
	 * See {@link 
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 */
	
	private void fillMenu() throws ClassNotFoundException, InstantiationException, IllegalAccessException{
		for(String s : modules.keySet()){
			Module m = modules.get(s);
			Map<String,String> views = m.getMenus();
			for(String text : views.keySet()){
				AbstractModuleView t =(AbstractModuleView) Class.forName(views.get(text)).newInstance();
				t.init(views.get(text), view);
				MenuButton b = new MenuButton(this);
				b.setView(t);
				b.setText(text);
				OnClickListener ocl = new OnClickListener(){
				    @Override
				    public void onClick(View v){
				        if(v instanceof MenuButton){
				        	MenuButton b = (MenuButton) v;
				        	b.iWasClicked();
				        }
				        else{
				        	Log.e(ID,"Button bahaving weirdly");
				        }
				    }
				};
				b.setOnClickListener(ocl);
				menu.addView(b);
				menu.invalidate();
			}
		}
	}
	
	
	//debug only method - example board created
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

	
	public void buttonWasClicked(MenuButton b){
		AbstractModuleView v = b.getView();
		v.setOnTouchListener(new View.OnTouchListener() {
	        @Override
	        public boolean onTouch(View v, MotionEvent event) {
	            if (event.getAction() == MotionEvent.ACTION_DOWN){
	              //  textView.setText("Touch coordinates : " +
	              //          String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
	            	AbstractModuleView view = (AbstractModuleView) v;
	            	view.iWasClicked(event.getX(),event.getY());
	            }
	            return true;
	        }
	    });
		setContentView(R.layout.activity_menu);
		LinearLayout layout = (LinearLayout) findViewById(R.id.menu_layout);
		TextView a = new TextView(this);
		a.setText("menu here");
		layout.addView(a);
		layout.addView(v);
		
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
