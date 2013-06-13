package com.app.ioapp;

import java.io.File;

import com.app.ioapp.init.Initializer;
import com.app.ioapp.interfaces.UIListener;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;

public class MainActivity extends Activity implements UIListener {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		//pewnie do PlayArea pójdzie...pójdzie hmm...
		Intent intent = new Intent(this, PlayAreaActivity.class);
    	startActivity(intent);
		
		//activity który bêdzie tym listenerem dodatkowo odpali
		// mainView.setListener(this)
	}
	
	@Override
	public void onWindowFocusChanged (boolean hasFocus) {
		//w tym miejscu jesteœmy pewni ¿e user widzi to co mu wyœwietliliœmy
		//mo¿emy zacz¹æ ³adowaæ co potencjalnie mo¿e chwilê potrwaæ
		if(hasFocus){
			String configurationFile = "";         // trzeba go skads wziac
			Initializer initializer = new Initializer(configurationFile);
			initializer.initialize();
		}
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
