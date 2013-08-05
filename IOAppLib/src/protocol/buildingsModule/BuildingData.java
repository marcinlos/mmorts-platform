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
