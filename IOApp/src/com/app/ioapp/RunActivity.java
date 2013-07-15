package com.app.ioapp;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.app.ioapp.customDroidViews.AbstractModuleView;
import com.app.ioapp.customDroidViews.BoardView;
import com.app.ioapp.customDroidViews.MenuButton;
import com.app.ioapp.modules.ConfiguredModule;
import com.app.ioapp.modules.ModulePresenter;
import com.app.ioapp.modules.Tile;
import com.app.ioapp.modules.InfMod.ITile;
import com.app.ioapp.modules.InfMod.InfrastructureModule;
import com.app.ioapp.store.Storage;
import com.app.ioapp.view.ModulesBroker;

public class RunActivity extends Activity implements UIListener {

	private static final List<String> arbitraryViewsList = new ArrayList<String>();
	private static final String ID = RunActivity.class.getName();
	private InfrastructureModule board;
	private BoardView boardView;
	private LinearLayout menu;
	private Properties boardConfig; // debug only
	private List<String> modules;
	private ModulesBroker view;
	private Map<String, ModulePresenter> presentersMap = new HashMap<String, ModulePresenter>();

	private LinearLayout menuBar;
	private LinearLayout mainLayout;

	private static Context context;
	
	public static Context getContext(){
		return context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		context= this;
		Log.d(ID, "Started Run Activity");
		initializePresentersMap();
		super.onCreate(savedInstanceState);
		fillViewsList();
		setContentView(R.layout.activity_main);
		menuBar = (LinearLayout) findViewById(R.id.menusBar);
		mainLayout = (LinearLayout) findViewById(R.id.ModulesPresentationView);

		fillMenuButtons();

		fillMainModulesView();

		// modules = initializer.getModules(); <------------------------
		// getModulesBroker();

		/*
		 * LinearLayout mainLayout = (LinearLayout)
		 * findViewById(R.id.main_layout); LinearLayout layout = (LinearLayout)
		 * findViewById(R.id.layout);
		 * 
		 * //this part is debug only /*boardView = new BoardView(this); board =
		 * new InfrastructureModule(boardConfig);
		 * boardView.init("InfrastructureModule", view);
		 * boardView.setOnTouchListener(new View.OnTouchListener() {
		 * 
		 * @Override public boolean onTouch(View v, MotionEvent event) { if
		 * (event.getAction() == MotionEvent.ACTION_DOWN){ //
		 * textView.setText("Touch coordinates : " + //
		 * String.valueOf(event.getX()) + "x" + String.valueOf(event.getY()));
		 * AbstractModuleView view = (AbstractModuleView) v;
		 * view.iWasClicked(event.getX(),event.getY()); } return true; } });
		 * setupBoard();
		 * 
		 * layout.addView(boardView); //end of debugonly part
		 */

		/*
		 * createMenu(mainLayout);
		 */

	}

	private void initializePresentersMap() {
		Log.d(ID, "Initializing presenters");
		for (ConfiguredModule module : Storage.getStorage().getLoadedModules()) {
			String presenterClassString = module.descriptor.config
					.get("presenter");
			ModulePresenter presenter;
			try {
				presenter = (ModulePresenter) Class.forName(
						presenterClassString).newInstance();
			} catch (Exception e) {
				presenter = null;
			}
			if (presenter != null) {
				presentersMap.put(module.descriptor.name, presenter);
			} else {
				Log.d(ID, module.descriptor.name
						+ " cannot instantiante presenter");
			}

		}
	}

	private void setMainLayoutView(View newChild) {
		Log.d(ID, "Clearing old layout");
		((ViewGroup) mainLayout).removeAllViews();
		Log.d(ID, "Setting new layout");
		mainLayout.addView(newChild);
		mainLayout.invalidate();
		Log.d(ID, "Done");
	}

	private void fillMainModulesView() {
		Log.d(ID, "Filling main view");
		for (ModulePresenter presenter : presentersMap.values()) {
			if (presenter.isMainView()) {
				setMainLayoutView(presenter.getMainModuleView(this, this, (ViewGroup)mainLayout));
				return;
			}
		}

	}

	private void fillMenuButtons() {
		Log.d(ID, "Filling menu buttons");
		for (Map.Entry<String, ModulePresenter> presenterEntry : presentersMap
				.entrySet()) {
			if (presenterEntry.getValue().hasMenuButton()) {
				menuBar.addView(presenterEntry.getValue().getButton(this));
			} else {
				Log.d(ID, presenterEntry.getKey() + " no button detected");
			}
		}

		menuBar.invalidate();
	}

	private void createMenu(LinearLayout mainLayout) {
		menu = new LinearLayout(this);
		menu.setWeightSum(1);
		menu.setOrientation(LinearLayout.VERTICAL);
		menu.setBackgroundColor(Color.CYAN);
		mainLayout.addView(menu);
		try {
			initializeUI();
		} catch (ClassNotFoundException e) {
			Log.e(ID, "No View with that name implemented - do it!", e);
			endProgram();
		} catch (InstantiationException e) {
			Log.e(ID, "Can't instantiate your view? What?", e);
			endProgram();
		} catch (IllegalAccessException e) {
			Log.e(ID, "Can't access your view? What?", e);
			endProgram();
		} catch (Exception e) {
			Log.e(ID, "Problem with reflection!", e);
			endProgram();
		}
	}

	private void fillViewsList() {
		arbitraryViewsList.add("com.app.ioapp.customDroidViews.BoardView");
	}

	private void getModulesBroker() {
		// view = initializer.getModulesBroker();
		// <------------------------------
	}

	/**
	 * Initialises all the views in arbitraryViewsList. Adds all the buttons
	 * needed to the menu. Used only after {@link modules} are filled.
	 * 
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 * @throws IllegalAccessException
	 * @throws NoSuchMethodException
	 * @throws InvocationTargetException
	 * @throws IllegalArgumentException
	 */

	private void initializeUI() throws ClassNotFoundException,
			InstantiationException, IllegalAccessException,
			IllegalArgumentException, InvocationTargetException,
			NoSuchMethodException {
		for (String text : arbitraryViewsList) {
			AbstractModuleView t = (AbstractModuleView) Class.forName(text)
					.getConstructor(Context.class).newInstance((Context) this);
			t.init(modules, view);
			if (t.isButton) {
				MenuButton b = new MenuButton(this);
				b.setView(t);
				b.setText(text);
				OnClickListener ocl = new OnClickListener() {
					@Override
					public void onClick(View v) {
						if (v instanceof MenuButton) {
							MenuButton b = (MenuButton) v;
							b.iWasClicked();
						} else {
							Log.e(ID, "Button bahaving weirdly");
						}
					}
				};
				b.setOnClickListener(ocl);
				menu.addView(b);
				menu.invalidate();
			}

		}
	}

	// debug only method - example board created
	private void setupBoard() {
		Log.e(ID, "setupBoard - it's debug only procedure!");
		List<ITile> tiles = new ArrayList<ITile>();
		Tile tile1 = new Tile("tile_fill", 0, 0, 1, 1);
		tiles.add(tile1);
		Tile tile2 = new Tile("tile_fill", 2, 2, 1, 1);
		tiles.add(tile2);
		Tile tile3 = new Tile(BitmapFactory.decodeResource(getResources(),
				R.drawable.tile_1x2), 5, 3, 1, 2);
		tiles.add(tile3);

		board.setupFields(tiles);
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		// asfd@aboardView.invalidate();
	}

	@Override
	public void stuffHappened(Object whathappend) {
		// TODO
		// REACT to stuff that happened
		// it probably won't be this activity, but some activity will do it

	}

	public void buttonWasClicked(MenuButton b) {
		AbstractModuleView v = b.getView();
		v.setOnTouchListener(new View.OnTouchListener() {
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if (event.getAction() == MotionEvent.ACTION_DOWN) {
					// textView.setText("Touch coordinates : " +
					// String.valueOf(event.getX()) + "x" +
					// String.valueOf(event.getY()));
					AbstractModuleView view = (AbstractModuleView) v;
					view.iWasClicked(event.getX(), event.getY());
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
	 * called from menu_layout as a signal that we can return to main display
	 * mode
	 * 
	 * @param v
	 *            unused
	 */
	public void endMenu(View v) {
		setContentView(R.layout.activity_main);
	}

	public void endProgram() {
		finish();
		System.exit(0);
	}

}
