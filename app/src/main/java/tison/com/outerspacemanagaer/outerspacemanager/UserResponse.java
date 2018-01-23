package tison.com.outerspacemanagaer.outerspacemanager;

import java.util.List;

/**
 * Created by atison on 23/01/2018.
 */

public class UserResponse {
    private String gas;
    private String gasModifier;
    private String minerals;
    private String mineralModifier;
    private String points;
    private String username;

    public void setUsername(String username) {
        this.username = username;
    }

    public void setGas(String gas) {
        this.gas = gas;
    }

    public void setGasModifier(String gasModifier) {
        this.gasModifier = gasModifier;
    }

    public void setMineralModifier(String mineralModifier) {
        this.mineralModifier = mineralModifier;
    }

    public void setMinerals(String minerals) {
        this.minerals = minerals;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public String getGas() {
        return gas;
    }

    public String getMinerals() {
        return minerals;
    }

    public String getPoints() {
        return points;
    }

    public String getGasModifier() {
        return gasModifier;
    }

    public String getMineralModifier() {
        return mineralModifier;
    }

    public UserResponse(String gas,
            String gasModifier,
            String minerals,
            String mineralModifier,
            String points,
            String username){
        this.gas = gas;
        this.gasModifier = gasModifier;
        this.minerals = minerals;
        this.mineralModifier = mineralModifier;
        this.points = points;
        this.username = username;
    }

    @Override
    public String toString() {
        return this.getUsername() + " : " + this.getPoints() + " points";
    }
}
