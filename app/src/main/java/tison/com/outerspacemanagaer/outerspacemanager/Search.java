package tison.com.outerspacemanagaer.outerspacemanager;

/**
 * Created by atison on 23/01/2018.
 */

public class Search {
    private String amountOfEffectByLevel;
    private String amountOfEffectLevel0;
    private String building;
    private String effect;
    private String gasCostByLevel;
    private String gasCostLevel0;
    private String level;
    private String mineralCostByLevel;
    private String mineralCostLevel0;
    private String name;
    private String timeToBuildByLevel;
    private String timeToBuildLevel0;

    public void setTimeToBuildLevel0(String timeToBuildLevel0) {
        this.timeToBuildLevel0 = timeToBuildLevel0;
    }

    public void setTimeToBuildByLevel(String timeToBuildByLevel) {
        this.timeToBuildByLevel = timeToBuildByLevel;
    }

    public void setMineralCostLevel0(String mineralCostLevel0) {
        this.mineralCostLevel0 = mineralCostLevel0;
    }

    public void setMineralCostByLevel(String mineralCostByLevel) {
        this.mineralCostByLevel = mineralCostByLevel;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public void setGasCostLevel0(String gasCostLevel0) {
        this.gasCostLevel0 = gasCostLevel0;
    }

    public void setGasCostByLevel(String gasCostByLevel) {
        this.gasCostByLevel = gasCostByLevel;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public void setAmountOfEffectByLevel(String amountOfEffectByLevel) {
        this.amountOfEffectByLevel = amountOfEffectByLevel;
    }

    public void setAmountOfEffectLevel0(String amountOfEffectLevel0) {
        this.amountOfEffectLevel0 = amountOfEffectLevel0;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTimeToBuildLevel0() {
        return timeToBuildLevel0;
    }

    public String getMineralCostLevel0() {
        return mineralCostLevel0;
    }

    public String getMineralCostByLevel() {
        return mineralCostByLevel;
    }

    public String getLevel() {
        return level;
    }

    public String getTimeToBuildByLevel() {
        return timeToBuildByLevel;
    }

    public String getGasCostLevel0() {
        return gasCostLevel0;
    }

    public String getGasCostByLevel() {
        return gasCostByLevel;
    }

    public String getEffect() {
        String eff = effect;
        if(eff != null){
            eff = eff.replace("speed_building", "vitesse de construction");
            eff = eff.replace("speed_fleet", "vitesse de la flotte");
        }else{
            eff = "vitesse de ressourcement";
        }
        return eff;
    }

    public String getBuilding() {
        return building;
    }

    public String getAmountOfEffectLevel0() {
        return amountOfEffectLevel0;
    }

    public String getName() {
        return name;
    }

    public String getAmountOfEffectByLevel() {
        return amountOfEffectByLevel;
    }

    @Override
    public String toString() {
        Double time =  Double.parseDouble(this.getTimeToBuildLevel0()) + ( Double.parseDouble(this.getTimeToBuildByLevel()) * Double.parseDouble(this.getLevel()));
        Integer minutes = (int) Math.floor(time / 60);
        Integer seconds = (int) Math.floor(time % 60);

        Double costMineral = Double.parseDouble(this.getMineralCostLevel0()) + ( Double.parseDouble(this.getMineralCostByLevel()) * Double.parseDouble(this.getLevel()));
        Double costGas = Double.parseDouble(this.getGasCostLevel0()) + ( Double.parseDouble(this.getGasCostByLevel()) * Double.parseDouble(this.getLevel()));
        Double amountEffect = Double.parseDouble(this.getAmountOfEffectLevel0()) + (Double.parseDouble(this.getAmountOfEffectByLevel()) * Double.parseDouble(this.getLevel()));
        if(this.getBuilding().equals("true")) {
            return this.getName() + " level " + this.getLevel() + "\nEn cours de construction ("+minutes.toString()+"m" + seconds.toString() + "s)\n\tCoût de la recherche :\n\t\t\t" + costMineral.toString() + " mineraux\n\t\t\t" + costGas.toString() + " gaz\n\tEffet : " + amountEffect + " " + this.getEffect();
        }else
            return this.getName() + " level " + this.getLevel() + "\n\tCoût de la recherche :\n\t\t\t" + costMineral.toString() + " mineraux\n\t\t\t" + costGas.toString() + " gaz\n\tEffet : " + amountEffect + " " + this.getEffect();
    }
}
