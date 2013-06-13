package com.app.ioapp;


import java.util.ArrayList;
import java.util.List;

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
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.widget.LinearLayout;

public class MainActivity extends Activity implements UIListener {
	
	private static final String ID = "MainActivity";
	private static final String CONFIG_FILE = "resources/client.properties";
	private boolean firstTime = true;
	private Initializer initializer;
	private BoardView board;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		initializer = new Initializer(CONFIG_FILE);
		initializer.initialize();
		MainView v = initializer.getView();
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		board = new BoardView(this);
		board.setMap(new Board());
		layout.addView(board);
		setupBoard();
		//pewnie do PlayArea pójdzie...pójdzie hmm...
		
		
		//activity który bêdzie tym listenerem dodatkowo odpali
		// mainView.setListener(this)
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
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public void stuffHappened(Object whathappend) {
		//TODO
		// REACT to stuff that happened
		// it probably won't be this activity, but some activity will do it
		
	}

}
