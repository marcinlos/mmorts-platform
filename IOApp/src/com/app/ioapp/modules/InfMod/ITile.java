package com.app.ioapp.modules.InfMod;

import android.graphics.Bitmap;

/**
 * This Interface is the format of building (or any other element that should be showed
 * on the player board). It can either have filled Bitmap (image sent from server side),
 * or the name of the graphical file in drawable. Those bitmaps can ONLY be in increments of BoardView.imageSize.
 * 
 * @author Michal
 *
 */
public interface ITile {
	
/**
 * 
 * @return True unless changed by validity
 */
	public abstract boolean isValid();
	/**
	 * Used to change isValid state. When creating temporary/placeholding Tile set validity to false
	 * @param b change validity to b.
	 */
	public abstract void validity(boolean b);
	
	public abstract Bitmap getBitmap();

	public abstract String getBitmapID();

	public abstract int getX();

	public abstract int getY();
/**
 * 
 * @param x between 0 and BoardView.mapSize (or this thing in config when we write one),
 * represents position on the map in X axis
 */
	public abstract void setX(int x);

	/**
	 * 
	 * @param y between 0 and BoardView.mapSize (or this thing in config when we write one),
	 * represents position on the map in Y axis
	 */
	public abstract void setY(int y);

	public abstract void setBitmap(Bitmap bit);

	public abstract int getSize_x();

	/**
	 * 
	 * @param size_x between 1 and BoardView.mapSize (or this thing in config when we write one)
	 * determines width of building
	 */
	public abstract void setSize_x(int size_x);

	public abstract int getSize_y();
	/**
	 * 
	 * @param size_y between 1 and BoardView.mapSize (or this thing in config when we write one)
	 * determines height of building
	 */
	public abstract void setSize_y(int size_y);
	
	public abstract void setBuilding(Building b);
	
	public abstract Building getBuilding();

}