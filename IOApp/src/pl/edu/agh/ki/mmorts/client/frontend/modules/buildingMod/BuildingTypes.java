package pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod;

public enum BuildingTypes {
	PRZEDSZKOLE("przedszkole"),
	MCDONALDS("mcdonalds"),
	CMENTARZ("cmentarz");
	private String caption;
	
	private BuildingTypes(String caption) {
		this.caption = caption;
	}

	public String getCaption() {
		return caption;
	}
	

}
