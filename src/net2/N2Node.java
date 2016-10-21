/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Double2D;
import sim.util.Bag;
import java.util.ArrayList;

/**
 *
 * @author Jeff
 */
public class N2Node implements Steppable {

    public int nodeIndex = -1;
    public Double2D location;
    public Stoppable stopper = null;
    public int weight = 0;
    public Bag neighbors = new Bag();
    public Bag units = new Bag();
    public int controllingFaction = N2Constants.UNCONTROLLED;
    public int nodeType = N2Constants.POPULATION_CENTER_TYPE;
    public N2ForceUnit forceDecription = null;
    public double recruitmentRate = 10.;
    public double trainingImpact = 1.01;
//    public Bag friends = new Bag();
//    public Bag foes = new Bag();
    public double value = 100.;

    public N2Node() {

    }

    public N2Node(int nType) {
        this.nodeType = nType;
    }

    public void setForceDescription(N2ForceUnit force) {
        this.forceDecription = force;
    }

    public N2ForceUnit getForceDescription() {
        return this.forceDecription;
    }

    @Override
    public String toString() {
        return "Node " + this.nodeIndex;
    }

    @Override
    public void step(SimState state) {
//        setControllingFaction(state.random.nextInt(4));
    }

    public void setNodeIndex(int i) {
        this.nodeIndex = i;
    }

    public int getNodeIndex() {
        return this.nodeIndex;
    }

    public void setLocation(Double2D loc) {
        this.location = loc;
    }

    public Double2D getLocation() {
        return this.location;
    }

    public void setValue(double val) {
        this.value = val;
    }

    public double getvalue() {
        return this.value;
    }

    public void setControllingFaction(int val) {
        this.controllingFaction = val;
    }

    public int getControllingFaction() {
        return this.controllingFaction;
    }

    public void addUnit(N2ForceUnit force) {
        this.units.add(force);
//        force.setHost(this);
    }

    public void removeUnit(N2ForceUnit force) {
        this.units.remove(force);
//        force.setHost(null);
    }

    public void setRecruitmentRate(double rr) {
        this.recruitmentRate = rr;
    }

    public double getRecruitmentRate() {
        return this.recruitmentRate;
    }

    public void setTraingImpact(double ti) {
        this.trainingImpact = ti;
    }

    public double getTrainingImpact() {
        return this.trainingImpact;
    }

    public Bag getUnits() {
        return this.units;
    }
    public ArrayList<N2ForceUnit> getForceUnits(){
        ArrayList<N2ForceUnit> fList = new ArrayList();
        for(Object tmpO:units){
            fList.add((N2ForceUnit)tmpO);
        }
        return fList;
    }
    public int getNumUnits(){
        return this.units.size();
    }

    public java.awt.Shape getShape() {
        switch (this.nodeType) {
            case N2Constants.POPULATION_CENTER_TYPE:
                return null;
            case N2Constants.MOBILE_UNIT_TYPE:
                return this.forceDecription.getShape();
        }
        return null;
    }
}
