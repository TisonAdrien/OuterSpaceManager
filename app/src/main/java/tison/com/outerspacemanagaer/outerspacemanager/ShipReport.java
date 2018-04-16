package tison.com.outerspacemanagaer.outerspacemanager;

/**
 * Created by atison on 09/04/2018.
 */

public class ShipReport {
    private String amount;
    private String shipId;
    private String capacity;
    private Ships[] fleet;
    private String survivingShips;
    private String gasCost;
    private String life;
    private String maxAttack;
    private String minAttack;
    private String mineralCost;
    private String name;
    private String shield;
    private String spatioportLevelNeeded;
    private String speed;
    private String timeToBuild;

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getShipId() {
        return shipId;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public String getCapacity() {
        return capacity;
    }

    public void setCapacity(String capacity) {
        this.capacity = capacity;
    }

    public Ships[] getFleet() {
        return fleet;
    }

    public void setFleet(Ships[] fleet) {
        this.fleet = fleet;
    }

    public String getSurvivingShips() {
        return survivingShips;
    }

    public void setSurvivingShips(String survivingShips) {
        this.survivingShips = survivingShips;
    }

    public String getGasCost() {
        return gasCost;
    }

    public void setGasCost(String gasCost) {
        this.gasCost = gasCost;
    }

    public String getLife() {
        return life;
    }

    public void setLife(String life) {
        this.life = life;
    }

    public String getMaxAttack() {
        return maxAttack;
    }

    public void setMaxAttack(String maxAttack) {
        this.maxAttack = maxAttack;
    }

    public String getMinAttack() {
        return minAttack;
    }

    public void setMinAttack(String minAttack) {
        this.minAttack = minAttack;
    }

    public String getMineralCost() {
        return mineralCost;
    }

    public void setMineralCost(String mineralCost) {
        this.mineralCost = mineralCost;
    }

    public String getName() {
        if(this.name != null)
            return name;
        else{
            switch (this.getShipId()){
                case "0": return "Chasseur léger";
                case "1": return "Chasseur lourd";
                case "2": return "Sonde d'espionnage";
                case "3": return "Destroyer";
                case "4": return "Etoile de la mort";
                default: return "OVNI";
            }
        }
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getShield() {
        return shield;
    }

    public void setShield(String shield) {
        this.shield = shield;
    }

    public String getSpatioportLevelNeeded() {
        return spatioportLevelNeeded;
    }

    public void setSpatioportLevelNeeded(String spatioportLevelNeeded) {
        this.spatioportLevelNeeded = spatioportLevelNeeded;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public String getTimeToBuild() {
        return timeToBuild;
    }

    public void setTimeToBuild(String timeToBuild) {
        this.timeToBuild = timeToBuild;
    }

    @Override
    public String toString() {
        return this.getAmount() + " " + this.getName();
    }
}
