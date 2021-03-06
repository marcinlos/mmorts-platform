package pl.edu.agh.ki.mmorts.client.frontend.modules.mapMod;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.ki.mmorts.client.frontend.modules.OurView;
import pl.edu.agh.ki.mmorts.client.frontend.modules.ViewListener;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;


public class MapModuleView extends View implements OurView {
	private static final String ID = "MapView";
	
	private static final String viewId = "MapView";
	
	private List<ViewListener> presentersListeners = new ArrayList<ViewListener>();
	

	public MapModuleView(Context context) {
		super(context);
		setBackgroundColor(Color.RED);
	}

	public MapModuleView(Context context, AttributeSet attrs) {
		super(context, attrs);
		setBackgroundColor(Color.RED);
	}

	public MapModuleView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		setBackgroundColor(Color.RED);
		
	}
	
	
	/**
	 * Called automatically after {@code invalidate()} or {@code postInvalidate()}.
	 * In out model used to pass canvas to every presenter that registered listener to draw his stuff
	 * Canvas is passed in the order in which listeners are registered
	 * shouldn't be too hard to implement methods that would shift that around if needed
	 */
	@Override
	public void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		Log.e(ID,"Rysuj� co� fajnego");
		for(ViewListener v : presentersListeners){
			v.drawStuff(canvas);
		}
		
	}


	public void iWasClicked(float x, float y) {
		for(ViewListener v : presentersListeners){
			v.touchEvent(x, y);
		}
		invalidate();
		
	}


	public static String getViewId() {
		return viewId;
	}

	/**
	 * {@inheritDoc}
	 */
	public void createListeners() {
		setOnTouchListener(new OnTouchListener() {
			
			/**
			 * if you want fancy menus to work, you should see whether your buttons were
			 * clicked as View, and if so return with false for the event to go through
			 */
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				if(event.getAction() == MotionEvent.ACTION_DOWN)
					iWasClicked(event.getX(), event.getY());
				return false; //moving the screen around might want the event? hopefully
			}
		});
		
	}

	/**
	 * {@inheritDoc}
	 */
	public void addListener(ViewListener listener) {
		presentersListeners.add(listener);
		invalidate();
		
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public void actionFinished(boolean result) {
		if(!result){
			Log.d(ID,"action failed");
			setAlpha((float) 0.9);
			final Handler handler = new Handler();
			handler.postDelayed(new Runnable() {
			  @Override
			  public void run() {
				  setAlpha(1);
				  Log.d(ID,"runnable bringing alpha back to normal fired");
			  }
			}, 2000);
		}
		else{
			Log.d(ID,"action succeeded");
			invalidate();
		}
		return;
	}
	
	


	
	
	
	
	
	
	
	/*	private static final String ID = "MapModuleView";

	private String moduleName;
	private List<ITile> fields;
	private int imageSize = 50;
	//private int mapSize = 25;
	private int mapWidth = 25;
	private int mapHeight = 25;
	//private InfrastructureModule map;
	private Map<String,Bitmap> cache;
	private boolean refresh_in_progress;
	private MapModuleData currentData;
	*//**
	 * 0 - empty tile
	 * 1 - tile occupied by
	 *//*
	private ITile[][] virtual_map = new Tile[mapWidth][mapHeight];
	




	
	private void addRefresher(){
		Timer timer = new Timer();
        timer.schedule(new ViewRefresher(this), 5, 1000);
	}
	
	
	private void cacheBitmap(Bitmap b){
		
	}
	
	
	@Override
	public void refresh(){
		Log.d(ID,"Refresh called");
		//virtual_map = currentData.map;
		fields = new ArrayList<ITile>();
		for(int i=0;i<mapWidth;i++){
			for(int j=0;j<mapHeight;j++){
				ITile tile = virtual_map[i][j];
				if(tile != null && tile.isValid()){
					//TODO wybieranie obrazka na podstawie budynku kt�ry tam jest a nie
					//Building b = tile.getBuilding();
					//if(b == null){
					//	Log.e(ID,"Tile is valid but no building is built here, hmm");
					//}
					
					if (tile.getBitmap() == null){
						int id = getResources().getIdentifier(tile.getBitmapID(), "drawable", "com.app.ioapp");
						tile.setBitmap(BitmapFactory.decodeResource(getResources(),id));
					}
					fields.add(tile);
					Log.d(ID, "Added a tile");
					
					
				}
				else if(tile == null){
					fields.add(new Tile(
							BitmapFactory.decodeResource(getResources(),R.drawable.tile),i,j,1,1)
							);
					Log.d(ID, "Added empty tile at [" + i + "," + j + "]");
				}
			}
		}
		this.invalidate();
	}

	
	

	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
	    setMeasuredDimension(mapWidth*imageSize, mapHeight*imageSize);
	}

	@Override
	public void onDraw(Canvas canvas) {
		if(modulesBroker.stateChanged(moduleName)){
			modulesBroker.stateReceived(moduleName);
			currentData = modulesBroker.getData(moduleName, MapModuleData.class);
			refresh();
		}
		super.onDraw(canvas);
		Log.d(ID, "Drawing stuff");
		//canvas.save();
		if (fields != null) {
			Log.d(ID, "Drawing what needs to be drawn");
			drawBasicElements(canvas, fields);
		}
		//canvas.restore();
	}

	private void drawBasicElements(Canvas canvas, List<ITile> elements) {
		for (ITile element : elements) {
			canvas.drawBitmap(element.getBitmap(), element.getX()*imageSize,
					element.getY()*imageSize, null);
		}
	}

	@Override
	public void iWasClicked(float x, float y) {
		Log.d(ID,"I was clicked on: x=" + x + " y=" + y);
		float tmp = x/imageSize;
		int truex = (int)tmp;
		tmp = y/imageSize;
		int truey = (int)tmp;
		//map.click(truex,truey);
		//TODO - how to invoke specific methods? Pass functions defining object?
		
	}

	@Override
	public void init(List<String> modules, ConcreteModulesBroker v){
		modulesBroker = v;
		moduleName = modules.get(0);
		currentData = modulesBroker.getData(moduleName, MapModuleData.class);
		modulesBroker.register(this, moduleName);
		this.mapWidth = currentData.mapWidth;
		this.mapHeight = currentData.mapHeight;
		//TODO set tile size to somethin, based on phone specifics or somethin?
		addRefresher();
	}*/

}
