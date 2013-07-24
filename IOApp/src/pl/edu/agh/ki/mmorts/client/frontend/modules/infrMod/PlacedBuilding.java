package pl.edu.agh.ki.mmorts.client.frontend.modules.infrMod;

import pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod.Building;

public class PlacedBuilding {
	private Building building;
	private int x;
	private int y;
	public PlacedBuilding(Building building, int x, int y) {
		super();
		this.building = building;
		this.x = x;
		this.y = y;
	}
	public Building getBuilding() {
		return building;
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	
}
