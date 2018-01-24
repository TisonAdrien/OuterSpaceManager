package tison.com.outerspacemanagaer.outerspacemanager;

import java.util.List;

/**
 * Created by atison on 23/01/2018.
 */

public class Buildings {
    private String size;
    private Building[] buildings;

    public void setSize(String size) {
        this.size = size;
    }

    public void setBuildings(Building[] buildings) {
        this.buildings = buildings;
    }

    public String getSize() {
        return size;
    }

    public Building[] getBuildings() {
        return buildings;
    }
}
