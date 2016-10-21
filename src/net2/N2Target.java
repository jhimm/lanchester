/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

import java.util.ArrayList;

/**
 *
 * @author Jeff
 */
public class N2Target {

    public N2Node target = null;
    public int numUnitsNeeded = 1;
    public int initialFaction = -1;
    public ArrayList<N2ForceUnit> ggUnitsAssigned = new ArrayList();
    public ArrayList<N2ForceUnit> bgUnitsAssigned = new ArrayList();

    public N2Target(N2Node node, int numNeeded) {
        this.target = node;
        this.initialFaction = node.getControllingFaction();
        this.numUnitsNeeded = numNeeded;
    }

    public int getIntiialFaction() {
        return this.initialFaction;
    }

    public void assignUnit(N2ForceUnit unit) {
        if (unit.getFaction() == N2Constants.BAD_GUYS) {
            this.bgUnitsAssigned.add(unit);
        } else {
            this.ggUnitsAssigned.add(unit);
            this.target.addUnit(unit);
        }
    }

    public void removeUnit(N2ForceUnit unit) {
        if (this.bgUnitsAssigned.contains(unit)) {
            this.bgUnitsAssigned.remove(unit);
            this.target.removeUnit(unit);
        }
        if (this.ggUnitsAssigned.contains(unit)) {
            this.ggUnitsAssigned.remove(unit);
            this.target.removeUnit(unit);
        }
    }

    public N2Node getTarget() {
        return this.target;
    }

    public boolean isTarget(N2Node possible) {
        if (possible.toString().matches(this.target.toString())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean changedFaction() {
        if (this.target.getControllingFaction() == this.initialFaction) {
            return false;
        } else {
            updateFaction();
//            for(Object tmpO:this.target.getUnits()){
//                N2ForceUnit tmpF= (N2ForceUnit) tmpO;
////                tmpF.setUnitStatus(N2Constants.UNIT_STATUS_READY);
////                tmpF.setHost(target);
//            }
            return true;
        }
    }

    public void updateFaction() {
        this.initialFaction = this.target.controllingFaction;
    }

    public void setNumUnitsNeeded(int nun) {
        this.numUnitsNeeded = nun;
    }

    public int getNumUnitsNeeded() {
        return this.numUnitsNeeded;
    }

    public int getNetUnitsNeeded() {
        return this.numUnitsNeeded - this.ggUnitsAssigned.size();
    }
}
