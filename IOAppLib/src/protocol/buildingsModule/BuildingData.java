package protocol.buildingsModule;

import java.io.Serializable;

/**
 * Contains all the necessary information about the building.
 * 
 * @author los
 */
public class BuildingData implements Serializable {

    private String name;
    private int width;
    private int height;

    public BuildingData(String name, int width, int height) {
        this.name = name;
        this.width = width;
        this.height = height;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    @Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + ((name == null) ? 0 : name.hashCode());
		result = prime * result + width;
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		BuildingData other = (BuildingData) obj;
		if (height != other.height)
			return false;
		if (name == null) {
			if (other.name != null)
				return false;
		} else if (!name.equals(other.name))
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public BuildingData(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}
