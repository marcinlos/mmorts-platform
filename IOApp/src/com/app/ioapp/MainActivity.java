package com.app.ioapp;


import java.util.ArrayList;
import java.util.List;

import com.app.ioapp.customDroidViews.AdditionalViewA;
import com.app.ioapp.customDroidViews.BoardView;
import com.app.ioapp.init.Initializer;
import com.app.ioapp.interfaces.ITile;
import com.app.ioapp.interfaces.UIListener;
import com.app.ioapp.modules.Board;
import com.app.ioapp.modules.Tile;
import com.app.ioapp.view.MainView;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.FrameLayout.LayoutParams;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends Activity implements UIListener {
	
	private static final String ID = "MainActivity";
	private static final String CONFIG_FILE = "resources/client.properties";
	private Initializer initializer;
	private BoardView board;
	private LinearLayout menu;
	private MenuManager manager;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializer = new Initializer(CONFIG_FILE);
		initializer.initialize();
		MainView v = initializer.getView();
		manager = new MenuManager(this,v);
		LinearLayout mainLayout = (LinearLayout) findViewById(R.id.main_layout);
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		board = new BoardView(this);
		board.setMap(new Board());
		layout.addView(board);
		
		//createMenu(mainLayout);
		menu = new LinearLayout(this);
		menu.setWeightSum(1);
		menu.setOrientation(LinearLayout.VERTICAL);
		menu.setBackgroundColor(Color.CYAN);
		mainLayout.addView(menu);
		
		
		setupBoard();
		//pewnie do PlayArea pójdzie...pójdzie hmm...
		
		
		//activity który bêdzie tym listenerem dodatkowo odpali
		// mainView.setListener(this)
	}
	
	public int addButton(String name){
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
		Log.e(ID, "setupBoard");
		List<ITile> tiles = new ArrayList<ITile>();
		Tile tile1 = new Tile("tile_fill",0,0,1,1);
		tiles.add(tile1);
		Tile tile2 = new Tile("tile_fill",2,2,1,1);
		tiles.add(tile2);
		Tile tile3 = new Tile(BitmapFactory.decodeResource(getResources(),R.drawable.tile_1x2),5,3,1,2);
		tiles.add(tile3);
		
		board.setupFields(tiles);
		Log.e(ID, "setup done");
	}
	
	@Override
	public void onWindowFocusChanged (boolean hasFocus) {
		board.invalidate();
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
		// TODO Auto-generated method stub
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

}
