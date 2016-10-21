/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

import java.util.ArrayList;
import java.util.HashMap;
import java.awt.Polygon;
import sim.engine.SimState;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.util.Double2D;
//import sim.engine.Stoppable;
//import lanchester.Modifier;
//import lanchester.MultiForce;

/**
 *
 * @author Jeff
 */
public class N2ForceUnit implements Steppable {

    public Stoppable stopper = null;
    public int faction = 0;
    public double number = 1000;
    public double fullStrength = 1000;
    public double previousNumber = 1000;
    public double combatLosses = 0.01;
    public double otherLosses = 0.01;
    public int replenishment = 25;
    public int forceIndex = -1;
    public int unitType = 1;
    public int unitStatus = 0;
    public N2Node host = null;
    public String name = "force 1";
    public ArrayList<N2ForceUnit> foes = new ArrayList();
    public HashMap<N2ForceUnit, Double> adFloorMap = new HashMap();
    public HashMap<N2ForceUnit, Double> adCeilingMap = new HashMap();
    public HashMap<N2ForceUnit, Double> dwFloorMap = new HashMap();
    public HashMap<N2ForceUnit, Double> dwCeilingMap = new HashMap();
    public ArrayList<N2ForceUnit> friends = new ArrayList();
    public N2Modifier modifier = new N2Modifier();
    public double attackDefendFloorRatio = 1.5;
    public double defendWithdrawFloorRatio = 0.6;
    public double attackDefendCeilingRatio = 1.8;
    public double defendWithdrawCeilingRatio = 0.8;
    public Polygon forceUnitShape = null;
    public N2Path currentPath = null;
    public N2Node currentNode = null;
    public N2Node nextNode = null;
    public N2Node previousNode = null;
    public double tolerance = 1.e-5;
    public double travelSpeed = 1.;
    public int readinessLevel = 0;

    public N2ForceUnit(String n, double num) {
        this.name = n;
        this.number = num;
        this.previousNumber = num;
        int[] xs = {4, 0, -4, 0};
        int[] ys = {0, -4, 0, 4};
        this.forceUnitShape = new Polygon(xs, ys, 4);
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

    public void setFoes(ArrayList<N2ForceUnit> fList) {
        this.foes.clear();
        this.foes.addAll(fList);
    }

    public void addFoe(N2ForceUnit f) {
        this.foes.add(f);
        setADFloorRatio(f, this.attackDefendFloorRatio);
        setADCeilingRatio(f, this.attackDefendCeilingRatio);
        setDWFloorRatio(f, this.defendWithdrawFloorRatio);
        setDWCeilingRatio(f, this.defendWithdrawCeilingRatio);
    }

    public void setADFloorRatio(N2ForceUnit f, double d) {
        if (this.adFloorMap.containsKey(f)) {
            Double val = adFloorMap.get(f);
            this.adFloorMap.remove(f, val);
        }
        this.adFloorMap.put(f, d);

    }

    public void setADCeilingRatio(N2ForceUnit f, double d) {
        if (this.adCeilingMap.containsKey(f)) {
            Double val = adCeilingMap.get(f);
            this.adCeilingMap.remove(f, val);
        }
        this.adCeilingMap.put(f, d);

    }

    public void setDWFloorRatio(N2ForceUnit f, double d) {
        if (this.dwFloorMap.containsKey(f)) {
            Double val = dwFloorMap.get(f);
            this.dwFloorMap.remove(f, val);
        }
        this.dwFloorMap.put(f, d);

    }

    public void setDWCeilingRatio(N2ForceUnit f, double d) {
        if (this.dwCeilingMap.containsKey(f)) {
            Double val = dwCeilingMap.get(f);
            this.dwCeilingMap.remove(f, val);
        }
        this.dwCeilingMap.put(f, d);

    }

    public void setFriend(N2ForceUnit force) {
        if (!friends.contains(force)) {
            this.friends.add(force);
        }
    }

    public void setForceIndex(int n) {
        this.forceIndex = n;
    }

    public void setReadinessLevel(int rl) {
        this.readinessLevel = rl;
    }

    public int getReadinessLevel() {
        return this.readinessLevel;
    }

    public int getForceIndex() {
        return this.forceIndex;
    }

    public void setUnitStatus(int us) {
        this.unitStatus = us;
    }

    public int getUnitStatus() {
        return this.unitStatus;
    }

    public void setUnitType(int ut) {
        this.unitType = ut;
    }

    public int getUnitType() {
        return this.unitType;
    }

    public boolean isFriend(N2ForceUnit force) {
        if (friends.contains(force)) {
            return true;
        }
        return false;
    }

    public void removeFriend(N2ForceUnit force) {
        if (friends.contains(force)) {
            friends.remove(force);
        }
    }

    public void setCurrentPath(N2Path path) {
        this.currentPath = path;
    }

    public N2Path getCurrentPath() {
        return this.currentPath;
    }

    @Override
    public void step(SimState state) {
        N2Nodes net = (N2Nodes) state;
        switch (this.unitStatus) {
            case N2Constants.UNIT_STATUS_RECRUITING:
                recruit();
                break;
            case N2Constants.UNIT_STATUS_TRAINING:
                train();
                break;
            case N2Constants.UNIT_STATUS_DEPLOYING:
                deploy(net);
                break;
            case N2Constants.UNIT_STATUS_MANEUVERING:
                maneuver(net);
                break;
            case N2Constants.UNIT_STATUS_READY:
                ready();
                break;
            case N2Constants.UNIT_STATUS_FIGHTING:
                fight();
                break;
            case N2Constants.UNIT_STATUS_RETREATING:
                retreat();
                break;
            case N2Constants.UNIT_STATUS_RESTING:
                rest();
                break;
            case N2Constants.UNIT_STATUS_OCCUPYING:
                occupy();
                break;

        }
    }

    public void recruit() {
        if (this.host.getControllingFaction() == this.faction) {
            setNumber(getNumber() + this.host.getRecruitmentRate());
            updateReadiness();
        }
    }

    public void train() {
        double trainingLevel = this.modifier.getOffensiveTrainingLevel();
        this.modifier.setOffensiveTrainingLevel(trainingLevel * this.host.getTrainingImpact());
        trainingLevel = this.modifier.getDefensiveTrainingLevel();
        this.modifier.setDefensiveTrainingLevel(trainingLevel * this.host.getTrainingImpact());
        updateReadiness();
    }

    public void deploy(N2Nodes net) {
        if (currentPath.getNumSteps() < 0) {
            return;
        }
        setHost(null);
        Double2D currentPos = net.region.getObjectLocation(this);
        double newX = 100000.;
        double newY = 100000.;
        getNextNode(currentPos);
        if (nextNode == null) {
            System.out.println("nextNode is null for " + currentPath.toString());
//            for (N2Node tmpN : currentPath.getPath()) {
//                System.out.println(" node " + tmpN.nodeIndex + " is on path " + currentPath.toString());
//            }
            System.out.println("Start node = " + currentPath.startNode.nodeIndex);
            System.out.println("End node = " + currentPath.lastNode.nodeIndex);
            System.out.println("Target node = " + currentPath.targetNode.nodeIndex);
            setUnitStatus(N2Constants.UNIT_STATUS_READY);
            return;
        }
        Double2D targetPos = nextNode.getLocation();
        if (currentPos.distance(targetPos) <= travelSpeed) {
            net.region.setObjectLocation(this, targetPos);
            if (nextNode == currentPath.lastNode) {
                setUnitStatus(N2Constants.UNIT_STATUS_READY);
                if (nextNode.getControllingFaction() == N2Constants.UNCONTROLLED) {
//                    setUnitStatus(N2Constants.UNIT_STATUS_OCCUPYING);
                    nextNode.setControllingFaction(N2Constants.GOOD_GUYS);
                    for (Object tmpO : nextNode.getUnits()) {
                        N2ForceUnit tmpF = (N2ForceUnit) tmpO;
                        tmpF.setFaction(N2Constants.GOOD_GUYS);
                    }
                } else if (nextNode.getControllingFaction() == N2Constants.NEUTRAL_GUYS) {
//                    setUnitStatus(N2Constants.UNIT_STATUS_OCCUPYING);
                    nextNode.setControllingFaction(N2Constants.GOOD_GUYS);
                    for (Object tmpO : nextNode.getUnits()) {
                        N2ForceUnit tmpF = (N2ForceUnit) tmpO;
                        tmpF.setFaction(N2Constants.GOOD_GUYS);
                    }
                }
                setHost(nextNode);
            }
//            this.setLocation(targetPos);
        } else {
            if (Math.abs(currentPos.getX() - targetPos.getX()) < tolerance) {
                newX = targetPos.getX();
                if (currentPos.getY() > targetPos.getY()) {
                    newY = currentPos.getY() - travelSpeed;
                } else {
                    newY = currentPos.getY() + travelSpeed;
                }
            } else {
                double delY = currentPos.getY() - targetPos.getY();
                double delX = currentPos.getX() - targetPos.getX();
                double hyp = Math.sqrt(delX * delX + delY * delY);
                double slope = (delY) / (delX);
                newY = currentPos.getY() - delY / hyp * travelSpeed;
                newX = currentPos.getX() - delX / hyp * travelSpeed;
            }
            Double2D newPos = new Double2D(newX, newY);
            net.region.setObjectLocation(this, newPos);
//            setPosition()
        }
    }

    public void maneuver(N2Nodes net) {
        System.out.println("try to maneuver");
         if (currentPath.getNumSteps() < 0) {
            return;
        }
        setHost(null);
        Double2D currentPos = net.region.getObjectLocation(this);
        double newX = 100000.;
        double newY = 100000.;
        getNextNode(currentPos);
        if (nextNode == null) {
            System.out.println("nextNode is null for " + currentPath.toString());
//            for (N2Node tmpN : currentPath.getPath()) {
//                System.out.println(" node " + tmpN.nodeIndex + " is on path " + currentPath.toString());
//            }
            System.out.println("Start node = " + currentPath.startNode.nodeIndex);
            System.out.println("End node = " + currentPath.lastNode.nodeIndex);
            System.out.println("Target node = " + currentPath.targetNode.nodeIndex);
            setUnitStatus(N2Constants.UNIT_STATUS_READY);
            return;
        }
        Double2D targetPos = nextNode.getLocation();
        if (currentPos.distance(targetPos) <= travelSpeed) {
            net.region.setObjectLocation(this, targetPos);
            if (nextNode == currentPath.lastNode) {
                setUnitStatus(N2Constants.UNIT_STATUS_IN_POSITION);
                setHost(nextNode);
            }
        } else {
            if (Math.abs(currentPos.getX() - targetPos.getX()) < tolerance) {
                newX = targetPos.getX();
                if (currentPos.getY() > targetPos.getY()) {
                    newY = currentPos.getY() - travelSpeed;
                } else {
                    newY = currentPos.getY() + travelSpeed;
                }
            } else {
                double delY = currentPos.getY() - targetPos.getY();
                double delX = currentPos.getX() - targetPos.getX();
                double hyp = Math.sqrt(delX * delX + delY * delY);
                double slope = (delY) / (delX);
                newY = currentPos.getY() - delY / hyp * travelSpeed;
                newX = currentPos.getX() - delX / hyp * travelSpeed;
            }
            Double2D newPos = new Double2D(newX, newY);
            net.region.setObjectLocation(this, newPos);
        }
       
    }

    public void ready() {
//System.out.println("unit "+this.name+" is ready");

    }

    public void fight() {
        System.out.println("unit " + this.name + " is fighting");

    }

    public void retreat() {
        System.out.println("unit " + this.name + " is retreating");
    }

    public void rest() {
        updateReadiness();
    }

    public void occupy() {
        System.out.println("unit " + this.name + " is occupying");

    }

    public void updateReadiness() {

    }

    public void getNextNode(Double2D pos) {
        previousNode = currentPath.startNode;
        ArrayList<N2Node> nList = this.currentPath.getPath();
        if (nList.isEmpty()) {
            nextNode = currentPath.startNode;
            setUnitStatus(N2Constants.UNIT_STATUS_READY);
            return;
        }
//        this.previousNode = nList.get(0);
        if (nList.size() == 1) {
            this.nextNode = nList.get(0);
            return;
        }
        Double2D previousNodePos = previousNode.getLocation();
        Double2D nextNodePos;
        for (int i1 = 0; i1 < nList.size(); i1++) {
            this.nextNode = nList.get(i1);
            nextNodePos = this.nextNode.getLocation();
            double d1 = previousNodePos.distance(pos);
            double d3 = nextNodePos.distance(pos);
            if (d1 < tolerance) {
                setHost(previousNode);
//                break;
//            }else if(d3 < tolerance){
//                setHost(nextNode);
//                setUnitStatus(N2Constants.UNIT_STATUS_READY);
            }
            double d2 = previousNodePos.distance(nextNodePos);
            if (Math.abs(d2 - d1 - d3) < tolerance && d3 > tolerance) {
                return;
            }
            previousNodePos = nextNodePos;
            this.previousNode = this.nextNode;
        }
    }

    public void setFaction(int f) {
        this.faction = f;
    }

    public int getFaction() {
        return this.faction;
    }

    public double getOffenseAttrition() {
        return this.modifier.getTotalOffenseMultiplier();
    }

    public double getForceOffenseMultiplier() {
        return this.modifier.getTotalOffenseMultiplier();
    }

    public void setOffenseMultplier(String k, double v) {
        this.modifier.setOffenseMultplier(k, v);
    }

    public double getOffenseMultiplier(String k) {
        return this.modifier.getOffenseMultiplier(k);
    }

    public double getDefenseAttrition() {
        return this.modifier.getTotalDefenseMultiplier();
    }

    public double getForceDefenseMultiplier() {
        return this.modifier.getTotalDefenseMultiplier();
    }

    public void setDefenseMultplier(String k, double v) {
        this.modifier.setDefenseMultplier(k, v);
    }

    public double getDefenseMultiplier(String k) {
        return this.modifier.getDefenseMultiplier(k);
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

    public ArrayList<N2ForceUnit> getFoes() {
        return this.foes;
    }

    public void setHost(N2Node h) {
        if (this.host != null && h == null) {
            this.host.removeUnit(this);
        }
        this.host = h;
        if (h != null) {
            h.addUnit(this);
        }

    }

    public N2Node getHost() {
        return this.host;
    }

    public void removeFoe(N2ForceUnit f) {
        if (this.foes.contains(f)) {
            this.foes.remove(f);
        }
    }

    public void setFullStrangth(double fs) {
        this.fullStrength = fs;
    }

    public double getFullStrength() {
        return this.fullStrength;
    }

    public void updateForce(double f) {
        this.previousNumber = this.number;
        this.number = f;
    }

    public java.awt.Shape getShape() {
        return this.forceUnitShape;
    }

    @Override
    public String toString() {
        return getName();
    }
}
