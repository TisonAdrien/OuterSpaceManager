package tison.com.outerspacemanagaer.outerspacemanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.preference.PreferenceManager;

import java.util.Date;

import static java.lang.Integer.parseInt;

/**
 * Created by atison on 23/01/2018.
 */

public class Building {
    private String level;
    private String amountOfEffectByLevel;
    private String amountOfEffectLevel0;
    private String buildingId;
    private String building;
    private String effect;
    private String gasCostByLevel;
    private String gasCostLevel0;
    private String imageUrl;
    private String mineralCostByLevel;
    private String mineralCostLevel0;
    private String name;
    private String timeToBuildByLevel;
    private String timeToBuildLevel0;
    private String timeToShow;

    public void setTimeToShow(String timeToShow){ this.timeToShow = timeToShow; }

    public String getTimeToShow(){
        if(this.timeToShow != null){
            return this.timeToShow;
        }else{
            Integer level0 = Integer.parseInt(this.getTimeToBuildLevel0());
            Integer byLevel = Integer.parseInt(this.getTimeToBuildByLevel());
            Integer level = Integer.parseInt(this.getLevel());
            Integer time =  level0 + (level * byLevel);
            return time.toString();
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAmountOfEffectByLevel(String amountOfEffectByLevel) {
        this.amountOfEffectByLevel = amountOfEffectByLevel;
    }

    public void setAmountOfEffectLevel0(String amountOfEffectLevel0) {
        this.amountOfEffectLevel0 = amountOfEffectLevel0;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setBuildingId(String buildingId) {
        this.buildingId = buildingId;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setGasCostByLevel(String gasCostByLevel) {
        this.gasCostByLevel = gasCostByLevel;
    }

    public void setGasCostLevel0(String gasCostLevel0) {
        this.gasCostLevel0 = gasCostLevel0;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setMineralCostByLevel(String mineralCostByLevel) {
        this.mineralCostByLevel = mineralCostByLevel;
    }

    public void setMineralCostLevel0(String mineralCostLevel0) {
        this.mineralCostLevel0 = mineralCostLevel0;
    }

    public void setTimeToBuildByLevel(String timeToBuildByLevel) {
        this.timeToBuildByLevel = timeToBuildByLevel;
    }

    public void setTimeToBuildLevel0(String timeToBuildLevel0) {
        this.timeToBuildLevel0 = timeToBuildLevel0;
    }

    public String getAmountOfEffectByLevel() {
        return amountOfEffectByLevel;
    }

    public String getName() {
        return name;
    }

    public String getAmountOfEffectLevel0() {
        return amountOfEffectLevel0;
    }

    public String getBuilding() {
        return building;
    }

    public String getBuildingId() {
        return buildingId;
    }

    public String getEffect() {
        String eff = effect;
        if(eff != null){
            eff = eff.replace("speed_building", "vitesse de construction");
            eff = eff.replace("speed_fleet", "vitesse de la flotte");
            eff = eff.replace("mineral_modifier","vitesse de ressourcement");
        }else{
            eff = "vitesse de ressourcement";
        }
        return eff;
    }

    public String getGasCostByLevel() {
        return gasCostByLevel;
    }

    public String getGasCostLevel0() {
        return gasCostLevel0;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getTimeToBuildByLevel() {
        return timeToBuildByLevel;
    }

    public String getLevel() {
        if(level == null)
            return "0";
        else
            return level;
    }

    public String getMineralCostByLevel() {
        return mineralCostByLevel;
    }

    public String getMineralCostLevel0() {
        return mineralCostLevel0;
    }

    public String getTimeToBuildLevel0() {
        return timeToBuildLevel0;
    }

    @Override
    public String toString() {
        Integer time = Integer.parseInt(this.getTimeToShow());
        Integer minutes = (int) Math.floor(time / 60);
        Integer seconds = (int) Math.floor(time % 60);
        Integer costMineral = (Integer.parseInt(this.getMineralCostLevel0()) + ( Integer.parseInt(this.getMineralCostByLevel()) * Integer.parseInt(this.getLevel())));
        Integer costGas = Integer.parseInt(this.getGasCostLevel0()) + ( Integer.parseInt(this.getGasCostByLevel()) * Integer.parseInt(this.getLevel()));
        Integer amountEffect = Integer.parseInt(this.getAmountOfEffectLevel0()) + (Integer.parseInt(this.getAmountOfEffectByLevel()) * Integer.parseInt(this.getLevel()));
        if(this.getBuilding().equals("true")) {
            return this.getName() + " level " + this.getLevel() + "\nEn cours de construction ("+minutes.toString()+"m" + seconds.toString() + "s)\n\tCoût de la construction :\n\t\t\t" + costMineral.toString() + " mineraux\n\t\t\t" + costGas.toString() + " gaz\n\tEffet : " + amountEffect + " " + this.getEffect();
        }else
            return this.getName() + " level " + this.getLevel()  + "\n\tCoût de la construction :\n\t\t\t" + costMineral.toString() + " mineraux\n\t\t\t" + costGas.toString() + " gaz\n\tEffet : " + amountEffect + " " + this.getEffect();
    }
}
