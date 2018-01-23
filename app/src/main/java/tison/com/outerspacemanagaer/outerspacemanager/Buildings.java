package tison.com.outerspacemanagaer.outerspacemanager;

import java.util.List;

/**
 * Created by atison on 23/01/2018.
 */

public class Buildings {
    private String size;
    private List<Building> buildings;

    public void setSize(String size) {
        this.size = size;
    }

    public void setBuildings(List<Building> buildings) {
        this.buildings = buildings;
    }

    public String getSize() {
        return size;
    }

    public List<Building> getBuildings() {
        return buildings;
    }
}
