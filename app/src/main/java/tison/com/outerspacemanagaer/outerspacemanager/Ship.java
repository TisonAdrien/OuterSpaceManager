package tison.com.outerspacemanagaer.outerspacemanager;

/**
 * Created by atison on 23/01/2018.
 */

public class Ship {
    private String gasCost;
    private String life;
    private String maxAttack;
    private String minAttack;
    private String mineralCost;
    private String name;
    private String shipId;
    private String shield;
    private String spatioportLevelNeeded;
    private String speed;
    private String timeToBuild;

    public Ship(String shipId, String amount){
        this.shipId = shipId;
        this.amount = amount;
    }

    private String amount;

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public void setGasCost(String gasCost) {
        this.gasCost = gasCost;
    }

    public void setLife(String life) {
        this.life = life;
    }

    public void setMaxAttack(String maxAttack) {
        this.maxAttack = maxAttack;
    }

    public void setMinAttack(String minAttack) {
        this.minAttack = minAttack;
    }

    public void setMineralCost(String mineralCost) {
        this.mineralCost = mineralCost;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setShield(String shield) {
        this.shield = shield;
    }

    public void setShipId(String shipId) {
        this.shipId = shipId;
    }

    public void setSpatioportLevelNeeded(String spatioportLevelNeeded) {
        this.spatioportLevelNeeded = spatioportLevelNeeded;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public void setTimeToBuild(String timeToBuild) {
        this.timeToBuild = timeToBuild;
    }

    public String getAmount() {
        return amount;
    }

    public String getGasCost() {
        return gasCost;
    }

    public String getLife() {
        return life;
    }

    public String getMaxAttack() {
        return maxAttack;
    }

    public String getMinAttack() {
        return minAttack;
    }

    public String getMineralCost() {
        return mineralCost;
    }

    public String getName() {
        return name;
    }

    public String getShield() {
        return shield;
    }

    public String getShipId() {
        return shipId;
    }

    public String getSpatioportLevelNeeded() {
        return spatioportLevelNeeded;
    }

    public String getSpeed() {
        return speed;
    }

    public String getTimeToBuild() {
        return timeToBuild;
    }

    @Override
    public String toString() {
        if(this.amount != null)
            return this.amount + " " + this.name + "\n\t\t\t" + this.getLife() + " HP\n\t\t\t" + this.getShield() + " SP\n\t\t\t" + this.getMinAttack() + "-" + this.getMaxAttack() + " AP\n\t\t\t" + this.getSpeed() + " SPEED";
        else
            return "0 " + this.name + " : " + this.getLife() + " HP\n\t\t\t" + this.getShield() + " SP\n\t\t\t" + this.getMinAttack() + "-" + this.getMaxAttack() + " AP\n\t\t\t" + this.getSpeed() + " SPEED";
    }
}
