/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Bag;
import sim.util.Double2D;

/**
 *
 * @author Jeff
 */

//import vso.mason.*;
//import VSOexp2.*;
import sim.engine.*;
import sim.field.continuous.Continuous2D;
import sim.util.*;
import sim.field.network.*;
//import sim.engine.s

/**
 *
 * @author Jeff
 */
public class NodeUnit implements Steppable {

    public static int nodeNum = 0;
    public int nodeIndex=-1;
    public double betweennessCentrality=.5;
    public double eigenCentrality=.5;
    public double adversarialWt=.5;
    public double betweennessWt=.5;
    public double connectednessWt=.5;
    public double cummulativeWt=.5;
    public double closenessCentrality=-1.;
    public double nnConnectednessCentrality=-1.;
    public double nnWeightedConnectednessCentrality=-1.;
    public double connectednessCentrality=-1.;
    public double weightedConnectednessCentrality=-1.;
    public double graphCentrality=-1.;
    public double stressCentrality = -1.;
    public double currentStability = .5;
    public boolean betweenness = true;
    public boolean eigenness = true;
    public boolean connectedness = false;
    public boolean weightedConnectedness = false;
    public boolean nextNearestConnectedness = false;
    public boolean nextNearestWeightedConnectedness = false;
    public Bag neighbors = new Bag();
    public boolean updatinginfo = false;
    public boolean gatheringInfo = true;
    public double weight = 0.;
    public double currentEnvironment = .5;
    public boolean fixedValue = false;
    public double exogenousEffort = 0.;
    public static double baseExoEffort = 0.1;
    public String nodeName;
    public boolean hasVisitor = false;
    public int numNeighbors = 1;
    public Double2D location;
    public Stoppable stopper=null;

    public NodeUnit() {
        this.nodeName = "Node " + nodeNum;
        NodeUnit.nodeNum++;
    }
    public void setNodeIndex(int i){
        this.nodeIndex=i;
    }
    public double getBetweennessCentrality(){
        return this.betweennessCentrality;
    }
    public double getConnectednessCentrality(){
        return this.connectednessCentrality;
    }
    public double getWeightedConnectednessCentrality(){
        return this.weightedConnectednessCentrality;
    }
    public double getNnConnectednessCentrality(){
        return this.nnConnectednessCentrality;
    }
    public double getNnWeightedConnectednessCentrality(){
        return this.nnWeightedConnectednessCentrality;
    }

    public double getCurrentStability() {
        return this.currentStability;
    }

    public void setCurrentStability(double newStab) {
        this.currentStability = newStab;
    }

    public void setParams(SimState state) {
    }

    public void step(SimState state) {
        NodeUnits villages = (NodeUnits) state;
        if (villages.schedule.getSteps() < 2) {
            this.gatheringInfo = true;
            this.updatinginfo = false;
        }
        if (this.gatheringInfo) {
        }
        if (this.updatinginfo) {
        }
        if (!fixedValue) {
        }

    }




    public String toString() {
        return this.nodeName;
    }
}
