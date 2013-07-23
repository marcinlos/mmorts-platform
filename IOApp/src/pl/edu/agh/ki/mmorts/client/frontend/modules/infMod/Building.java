package pl.edu.agh.ki.mmorts.client.frontend.modules.infMod;

import java.util.ArrayList;
import java.util.List;

import pl.edu.agh.ki.mmorts.client.frontend.activities.RunningActivity;
import pl.edu.agh.ki.mmorts.client.frontend.generated.R;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;


public class Building {
	private int sizeX;
	private int sizeY;
	//Linearized by rows
	private List<Bitmap> bitmaps;
	
	private static Building crossy;
	private static Building orange;
	private static Building green;
	
	public Building(int sizeX, int sizeY, List<Bitmap> bitmaps) {
		this.sizeX = sizeX;
		this.sizeY = sizeY;
		this.bitmaps = bitmaps;
	}
	
	public static Building getCrossy(){
		if(crossy == null){
//			Bitmap bitmap = BitmapFactory.decodeResource(RunningActivity.getContext().getResources(), R.drawable.tile_cross);
//			List<Bitmap> list = new ArrayList<Bitmap>();
//			list.add(bitmap);
//			crossy = new Building(1,1,list);
		}
		return crossy;
	}
	public static Building getGreeny(){
		if(green == null){
//			Bitmap bitmap = BitmapFactory.decodeResource(RunningActivity.getContext().getResources(), R.drawable.tile);
//			List<Bitmap> list = new ArrayList<Bitmap>();
//			list.add(bitmap);
//			list.add(bitmap);
//			green = new Building(1,2,list);
		}
		return green;
	}
	public static Building getOrangy(){
		if(orange == null){
//			Bitmap bitmap = BitmapFactory.decodeResource(RunningActivity.getContext().getResources(), R.drawable.tile_orange);
//			List<Bitmap> list = new ArrayList<Bitmap>();
//			list.add(bitmap);
//			list.add(bitmap);
//			orange = new Building(2,1,list);
		}
		return orange;
	}

	public int getSizeX() {
		return sizeX;
	}

	public void setSizeX(int sizeX) {
		this.sizeX = sizeX;
	}

	public int getSizeY() {
		return sizeY;
	}

	public void setSizeY(int sizeY) {
		this.sizeY = sizeY;
	}

	public List<Bitmap> getBitmaps() {
		return bitmaps;
	}

	public void setBitmaps(List<Bitmap> bitmaps) {
		this.bitmaps = bitmaps;
	}
	
	
}
