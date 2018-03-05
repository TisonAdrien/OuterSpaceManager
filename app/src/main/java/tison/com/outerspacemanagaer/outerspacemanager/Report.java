package tison.com.outerspacemanagaer.outerspacemanager;

/**
 * Created by atison on 05/03/2018.
 */

public class Report {
     private Ships attackerFleet;
     private Ships attackerFleetAfterBattle;
     private String date;
     private String dateInv;
     private Ships defenderFleet;
     private Ships defenderFleetAfterBattle;
     private String from;
     private String gasWon;
     private String mineralsWon;
     private String to;
     private String type;

    public Ships getAttackerFleet() {
        return attackerFleet;
    }

    public void setAttackerFleet(Ships attackerFleet) {
        this.attackerFleet = attackerFleet;
    }

    public Ships getAttackerFleetAfterBattle() {
        return attackerFleetAfterBattle;
    }

    public void setAttackerFleetAfterBattle(Ships attackerFleetAfterBattle) {
        this.attackerFleetAfterBattle = attackerFleetAfterBattle;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDateInv() {
        return dateInv;
    }

    public void setDateInv(String dateInv) {
        this.dateInv = dateInv;
    }

    public Ships getDefenderFleet() {
        return defenderFleet;
    }

    public void setDefenderFleet(Ships defenderFleet) {
        this.defenderFleet = defenderFleet;
    }

    public Ships getDefenderFleetAfterBattle() {
        return defenderFleetAfterBattle;
    }

    public void setDefenderFleetAfterBattle(Ships defenderFleetAfterBattle) {
        this.defenderFleetAfterBattle = defenderFleetAfterBattle;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getGasWon() {
        return gasWon;
    }

    public void setGasWon(String gasWon) {
        this.gasWon = gasWon;
    }

    public String getMineralsWon() {
        return mineralsWon;
    }

    public void setMineralsWon(String mineralsWon) {
        this.mineralsWon = mineralsWon;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.getTo() + " has been " + this.getType() + " by " + this.getFrom() + " !\n\tGas won : " + this.getGasWon() + "\n\tMinerals won : " + this.getMineralsWon();
    }
}
