package pl.edu.agh.ki.mmorts.client.frontend.modules;


import pl.edu.agh.ki.mmorts.client.frontend.modules.infMod.Building;
import pl.edu.agh.ki.mmorts.client.frontend.modules.infMod.ITile;
import android.graphics.Bitmap;

public class Tile implements ITile {
	
	private boolean isValid = true;
	private Bitmap bitmap;
	private String bitmap_id;
    private int x;
    private int y;
    private int size_x;
    private int size_y;
    
    public Tile(Bitmap bit, int x, int y, int sx, int sy){
    	this.x = x;
    	this.y = y;
    	this.size_x = sx;
    	this.size_y = sy;
    	this.bitmap = bit;
    }
    public Tile(String bit_id, int x, int y,int sx, int sy){
    	this.x = x;
    	this.y = y;
    	bitmap_id = bit_id;
    	this.size_x = sx;
    	this.size_y = sy;
    }
    
    /* (non-Javadoc)
	 * @see com.app.board.ITile#getBitmap()
	 */
    @Override
	public Bitmap getBitmap() {
        return bitmap;
    }
    
    
    
    /* (non-Javadoc)
	 * @see com.app.board.ITile#getBitmapID()
	 */
    @Override
	public String getBitmapID(){
    	return bitmap_id;
    }

    /* (non-Javadoc)
	 * @see com.app.board.ITile#getX()
	 */
    @Override
	public int getX() {
        return x;
    }

    /* (non-Javadoc)
	 * @see com.app.board.ITile#getY()
	 */
    @Override
	public int getY() {
        return y;
    }

    /* (non-Javadoc)
	 * @see com.app.board.ITile#setX(int)
	 */
    @Override
	public void setX(int x) {
        this.x = x;
    }

    /* (non-Javadoc)
	 * @see com.app.board.ITile#setY(int)
	 */
    @Override
	public void setY(int y) {
        this.y = y;
    }
    
    /* (non-Javadoc)
	 * @see com.app.board.ITile#setBitmap(android.graphics.Bitmap)
	 */
    @Override
	public void setBitmap(Bitmap bit){
    	bitmap = bit;
    }

	/* (non-Javadoc)
	 * @see com.app.board.ITile#getSize_x()
	 */
	@Override
	public int getSize_x() {
		return size_x;
	}

	/* (non-Javadoc)
	 * @see com.app.board.ITile#setSize_x(int)
	 */
	@Override
	public void setSize_x(int size_x) {
		this.size_x = size_x;
	}

	/* (non-Javadoc)
	 * @see com.app.board.ITile#getSize_y()
	 */
	@Override
	public int getSize_y() {
		return size_y;
	}

	/* (non-Javadoc)
	 * @see com.app.board.ITile#setSize_y(int)
	 */
	@Override
	public void setSize_y(int size_y) {
		this.size_y = size_y;
	}
	@Override
	public boolean isValid() {
		return isValid;
	}
	@Override
	public void validity(boolean b){
    	isValid = b;
    }
	@Override
	public void setBuilding(Building b) {
		// TODO Auto-generated method stub
		
	}
	@Override
	public Building getBuilding() {
		// TODO Auto-generated method stub
		return null;
	}

}
