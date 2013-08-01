package pl.edu.agh.ki.mmorts.client.backend.modules.buildingModule;

import java.io.Serializable;

import pl.edu.agh.ki.mmorts.client.frontend.modules.buildingMod.BuildingInstance;

public class BuildingMessage implements Serializable {

    private BuildingInstance building;
    private String player;

    public BuildingMessage(BuildingInstance building, String player) {
        this.building = building;
        this.player = player;
    }

    public BuildingInstance getBuilding() {
        return building;
    }

    public String getPlayer() {
        return player;
    }

}
