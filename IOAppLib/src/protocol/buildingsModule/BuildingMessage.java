package protocol.buildingsModule;

import java.io.Serializable;

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
