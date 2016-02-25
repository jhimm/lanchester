/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchester;

/**
 *
 * @author Jeff
 */
public class Force {

    public double number = 1000;
    public double previousNumber = 1000;
    public double combatLosses = 0.01;
    public double otherLosses = 0.01;
    public int replenishment = 25;
    public String name = "force 1";
    public Force foe = null;

    public Force(String n,double num) {
        this.name = n;
        this.number=num;
        this.previousNumber=num;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setNumber(double num) {
        this.number = num;
    }

    public void setCombatLosses(double cl) {
        this.combatLosses = cl;
    }

    public void setOtherLosses(double ol) {
        this.otherLosses = ol;
    }

    public void setReplenishment(int r) {
        this.replenishment = r;
    }

    public void setFoe(Force f) {
        this.foe = f;
    }

    public String getName() {
        return this.name;
    }

    public double getNumber() {
        return this.number;
    }

    public double getCombatLosses() {
        return this.combatLosses;
    }

    public double getOtherLosses() {
        return this.otherLosses;
    }

    public int getReplenishment() {
        return this.replenishment;
    }

    public Force getFoe() {
        return this.foe;
    }

}
