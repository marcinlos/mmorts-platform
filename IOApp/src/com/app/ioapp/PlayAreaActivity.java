package com.app.ioapp;

import java.util.ArrayList;
import java.util.List;

import com.app.board.BoardView;
import com.app.board.ITile;
import com.app.board.Tile;
import android.os.Bundle;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.view.Menu;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;
import android.widget.ScrollView;

public class PlayAreaActivity extends Activity {
	
	static private final String ID = "PlayArea";
	private BoardView board;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_play_area);
		LinearLayout layout = (LinearLayout) findViewById(R.id.layout);
		board = new BoardView(this);
		layout.addView(board);
		setupBoard();
		/*ScrollView sv = new ScrollView(this);
		WScrollView hsv = new WScrollView(this);
		hsv.sv = sv;
		board = new BoardView(this);
		sv.addView(board);
		hsv.addView(sv, new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT));
		setContentView(hsv);
		setupBoard();*/
		
		
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

	/*
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.play_area, menu);
		return true;
	}*/

}
