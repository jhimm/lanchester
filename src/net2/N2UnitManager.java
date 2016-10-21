/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

import sim.engine.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import sim.util.Bag;
import sim.util.Double2D;

/**
 *
 * @author Jeff
 */
public class N2UnitManager implements Steppable, Serializable {

    public int managerNumber = -1;
    public Stoppable stopper = null;
    public ArrayList<N2Node> nodeList = new ArrayList();
    public HashMap<N2Node, N2Target> targetMap = new HashMap();
    public HashMap<N2Node, Double> valueMap = new HashMap();
    public N2Nodes nodes = null;
    public N2NodeSelector selector = null;
    public ArrayList<N2BattleManager> battleList = new ArrayList();
    public boolean useSteps = false;
    public boolean useGraphDistance = true;
    public boolean useActualDistance = false;
    public boolean unitActive = false;

    public N2UnitManager(int num) {
        this.managerNumber = num;
    }

    @Override
    public void step(SimState state) {
        nodes = (N2Nodes) state;
        nodes.boilLinks(N2Constants.GOOD_GUYS);
        nodes.pathFind();
        selectTarget();
        updateTargetMap();
        assignUnits();
//        System.out.println("There are " + targetList.size() + " current targets at time = " + nodes.schedule.time());
//        nodes.schedule.
//        survey();
//        N2Node target = selectTarget();
//        if (target != null) {
//            N2Target newTarget = new N2Target(target, 1);
//            targetMap.put(target, newTarget);
//            System.out.println("Target node = " + target.nodeIndex);
//            if (target.getControllingFaction() == N2Constants.BAD_GUYS) {
//                int num = target.getNumUnits();
//                newTarget.setNumUnitsNeeded(Math.max(1, num * 4));
//            }
////            targetList.add(newTarget);
//            System.out.println(" add target " + newTarget.getTarget().toString());
//            assignUnits(newTarget);
//            positionUnits(target);
//        }
//        for (N2Target tmpTarg : targetList) {
//            System.out.println("numUnits assigned to " + tmpTarg.getTarget().toString() + " = " + tmpTarg.ggUnitsAssigned.size());
//        }
//        attack();

    }

    public void updateTargetMap() {
        ArrayList<N2Node> done = new ArrayList();
        for (N2Node tmpN : targetMap.keySet()) {
            N2Target tmpT = targetMap.get(tmpN);
            if (tmpT.changedFaction()) {
                done.add(tmpN);
                for (N2ForceUnit tmpF : tmpT.ggUnitsAssigned) {
                    tmpF.setUnitStatus(N2Constants.UNIT_STATUS_READY);
                    tmpF.setHost(tmpT.getTarget());
                }
            }
        }
        for (N2Node tmpT : done) {
            do {
                targetMap.remove(tmpT);
            } while (targetMap.keySet().contains(tmpT));
        }
        System.out.println("targetMap has " + targetMap.size() + " entries after pruning");
//        targetList.removeAll(done);
    }

//    public void survey() {
//        sim.util.Bag nodeBag = nodes.neighborhood.getAllNodes();
//        targetList.clear();
//        valueMap.clear();
//        for (Object tmpO : nodeBag) {
//            if (tmpO instanceof N2Node) {
//                N2Node tmpN = (N2Node) tmpO;
//                targetList.add(tmpN);
//                valueMap.put(tmpN, tmpN.getvalue());
//            }
//        }
//    }
//    public double getValue(N2Node node) {
//        return node.getvalue();
//        return 2.;
//    }
    public N2Node selectTarget() {
        N2NodeSurveyor surveyor = new N2NodeSurveyor(nodes);
//        updateNodeList();
        this.selector = new N2NodeSelector(surveyor.getNodeList());
//        surveyor.survey(targetList);
        surveyor.survey(targetMap);
        selector.rankNodes(surveyor);
//        System.out.println(surveyor.getOutString());
//        valueMap.clear();
//        if (surveyor.getUncontrolledBorderList().size() > 0) {
//            System.out.println(" adding uncontrolled border nodes to value map");
//            for (N2Node tmpN : surveyor.getUncontrolledBorderList()) {
//                valueMap.put(tmpN, tmpN.getvalue());
//            }
//        } else if (surveyor.getNeutralBorderList().size() > 0) {
//            System.out.println(" adding neutral border nodes to value map");
////            valueMap.clear();
//            for (N2Node tmpN : surveyor.getNeutralBorderList()) {
//                valueMap.put(tmpN, tmpN.getvalue());
//            }
//        } else if (surveyor.getInternalUncontrolledList().size() > 0) {
//            System.out.println(" adding internal uncontrolled nodes to value map");
////            valueMap.clear();
//            for (N2Node tmpN : surveyor.getInternalUncontrolledList()) {
//                valueMap.put(tmpN, tmpN.getvalue());
//            }
//        } else if (surveyor.getInternalNeutralList().size() > 0) {
//            System.out.println(" adding internal neutral nodes to value map");
////            valueMap.clear();
//            for (N2Node tmpN : surveyor.getInternalNeutralList()) {
//                valueMap.put(tmpN, tmpN.getvalue());
//            }
//        } else {
//            System.out.println(" adding bg nodes to value map");
////            valueMap.clear();
//            for (N2Node tmpN : surveyor.getBGBorderList()) {
//                valueMap.put(tmpN, tmpN.getvalue());
//            }
//        }
        double highest = -1.;
        N2Node highNode = null;
        for (N2Node tmpN : valueMap.keySet()) {
            double tmpD = valueMap.get(tmpN);
            if (tmpD > highest) {
                highest = tmpD;
                highNode = tmpN;
            }
        }
        return highNode;
    }

    public void updateNodeList() {

    }

    public void positionUnits(N2Node target) {
        System.out.println("positioning units for target " + target.toString());

    }

//    public void assignUnits(N2Target newTarget) {
//        N2Node target = newTarget.getTarget();
//        N2ForceUnit closestOne = getClosestUnit(target);
//
//        if (closestOne == null) {
//            System.out.println(" Failed to find a unit to assign");
//            targetMap.remove(target);
//            return;
//        }
//        if (target.getControllingFaction() == N2Constants.NEUTRAL_GUYS
//                || target.getControllingFaction() == N2Constants.UNCONTROLLED) {
////            N2ForceUnit closestOne = getClosestUnit(target);
//            if (closestOne.previousNode == null) {
//                System.out.println(" NO PREVIOUS NODE!");
//            }
//            int closeIndex = closestOne.getHost().getNodeIndex();
//            int targetIndex = target.getNodeIndex();
//            N2Paths paths = nodes.finder.allPaths[closeIndex][targetIndex];
//            if (paths.getShortestLength() < 0) {
//                System.out.println("No close units to assign");
//                return;
//            }
//            closestOne.setUnitStatus(N2Constants.UNIT_STATUS_DEPLOYING);
//            closestOne.setCurrentPath(paths.shortestPaths.get(0));
//            newTarget.assignUnit(closestOne);
//            System.out.println(closestOne.getCurrentPath().toString());
//        } else if (target.getControllingFaction() == N2Constants.BAD_GUYS) {
////            N2ForceUnit closestOne = getClosestUnit(target);
//            int closeIndex = closestOne.getHost().getNodeIndex();
//            int targetIndex = target.getNodeIndex();
//            N2Paths paths = nodes.finder.allPaths[closeIndex][targetIndex];
//            closestOne.setUnitStatus(N2Constants.UNIT_STATUS_DEPLOYING);
//            closestOne.setCurrentPath(paths.shortestPaths.get(0));
//            System.out.println(closestOne.getCurrentPath().toString());
//        }
//    }
    public void assignUnits() {
        nodes.pathFind();
        Bag nodesAndUnits = nodes.neighborhood.getAllNodes();
        ArrayList<N2ForceUnit> allUnits = new ArrayList();
        ArrayList<N2ForceUnit> totalUnits = new ArrayList();
        N2NodeSurveyor surveyor = new N2NodeSurveyor(nodes);
        this.selector = new N2NodeSelector(surveyor.getNodeList());
        surveyor.survey(targetMap);
        selector.rankNodes(surveyor);
        int allUnitsSize = -1;
        for (Object tmpO : nodesAndUnits) {
            if (tmpO instanceof N2ForceUnit) {
                N2ForceUnit tmpF = (N2ForceUnit) tmpO;
                totalUnits.add(tmpF);
                int faction = tmpF.getFaction();
                if (faction == N2Constants.GOOD_GUYS && tmpF.getUnitStatus() == N2Constants.UNIT_STATUS_READY) {
                    allUnits.add(tmpF);
                }
            }
        }
        allUnitsSize = allUnits.size();
        System.out.println("Num units avaiable = " + allUnits.size());
        for (int i1 = 0; i1 < selector.numNodes; i1++) {
            N2Node tNode = selector.getNextInLine();
            if (!targetMap.keySet().contains(tNode)) {
//            if (tNode.getControllingFaction() != N2Constants.GOOD_GUYS && !targetMap.keySet().contains(tNode)) {
                if (tNode.getControllingFaction() == N2Constants.NEUTRAL_GUYS || tNode.getControllingFaction() == N2Constants.UNCONTROLLED) {
                    N2Target newTarget;
                    N2ForceUnit closestOne = getClosestUnit(tNode, allUnits);
                    if (closestOne != null) {
                        newTarget = new N2Target(tNode, 1);
                        int closeIndex = closestOne.getHost().getNodeIndex();
                        int targetIndex = tNode.getNodeIndex();
                        N2Paths paths = nodes.finder.allPaths[closeIndex][targetIndex];
                        if (paths.getShortestLength() < 0) {
                            System.out.println("No close units to assign");
                            return;
                        }
                        closestOne.setUnitStatus(N2Constants.UNIT_STATUS_DEPLOYING);
                        closestOne.setCurrentPath(paths.shortestPaths.get(0));
                        newTarget.assignUnit(closestOne);
                        targetMap.put(tNode, newTarget);
                        System.out.println(closestOne.getCurrentPath().toString() + " assigned to node " + tNode.toString());
                        allUnits.remove(closestOne);
                    }
                } else if (tNode.getControllingFaction() == N2Constants.BAD_GUYS) {
                    boolean foundForces = false;
                    HashMap<N2ForceUnit, ArrayList<N2Paths>> pathMap = new HashMap();
                    ArrayList<N2ForceUnit> attackUnits = new ArrayList();
                    int numBGUnits = tNode.getNumUnits();
                    N2Target newTarget = new N2Target(tNode, numBGUnits * 3 + 1);
                    int numNeeded = newTarget.getNetUnitsNeeded();
                    if (allUnits.size() >= numNeeded) {
                        Bag neighbors = tNode.neighbors;
                        checkAlliance(neighbors);
                        for (int i3 = 0; i3 < allUnits.size(); i3++) {
                            N2ForceUnit unit = allUnits.get(i3);
                            int unitHostNum = unit.getHost().getNodeIndex();
                            N2Node tmpNode;
                            for (Object tmpO : neighbors) {
                                tmpNode = (N2Node) tmpO;
                                int neighborIndex = tmpNode.getNodeIndex();
                                N2Paths paths = nodes.finder.allPaths[unitHostNum][neighborIndex];
                                if (paths.getShortestLength() > 0) {
                                    if (!attackUnits.contains(unit)) {
                                        attackUnits.add(unit);
                                    }
                                    if (!pathMap.keySet().contains(unit)) {
                                        ArrayList<N2Paths> tmpPaths = new ArrayList();
                                        tmpPaths.add(paths);
                                        pathMap.put(unit, tmpPaths);
                                    } else {
                                        pathMap.get(unit).add(paths);
                                    }
                                }
                            }
                        }
                        if (attackUnits.size() >= numNeeded) {
                            foundForces = true;
//                            trimNumber(attackUnits, numNeeded, pathMap);
                        }
//                        N2BattleManager manager = new N2BattleManager();

                    }
                    if (foundForces) {
                        trimNumber(attackUnits, numNeeded, pathMap);
                        N2BattleManager manager = new N2BattleManager(tNode);
                        for (N2ForceUnit tmpF : tNode.getForceUnits()) {
                            manager.addBadGuy(tmpF);
                            tmpF.setUnitStatus(N2Constants.UNIT_STATUS_OCCUPYING);
                        }
                        for (N2ForceUnit tmpF : attackUnits) {
                            tmpF.setUnitStatus(N2Constants.UNIT_STATUS_MANEUVERING);
                            tmpF.setCurrentPath(pathMap.get(tmpF).get(0).shortestPaths.get(0));
                            manager.addGoodGuy(tmpF);
                        }
                        battleList.add(manager);
                        manager.stopper = nodes.schedule.scheduleRepeating(manager);
                        targetMap.put(tNode, newTarget);
                    }
                }
            }
        }
        System.out.println("Num units avaiable after assignment = " + allUnits.size());
        if (allUnits.size() == allUnitsSize) {
            for (N2ForceUnit tmpF : totalUnits) {
                System.out.println(tmpF.toString() + " is " + N2Constants.STATUS_DESCRIPTION[tmpF.getUnitStatus()]);
            }
        }
        for (int i1 = 0; i1 < totalUnits.size(); i1++) {
            N2ForceUnit tmpF = totalUnits.get(i1);
            if (tmpF.getUnitStatus() == N2Constants.UNIT_STATUS_DEPLOYING) {
                System.out.println(tmpF.toString() + " deploying to " + tmpF.getCurrentPath().toString());
            }
        }

    }

    public void checkAlliance(Bag n) {
        Bag bg = new Bag();
        for (Object tmpO : n) {
            N2Node tmpN = (N2Node) tmpO;
            if (tmpN.getControllingFaction() == N2Constants.BAD_GUYS) {
                bg.add(tmpO);
            }
        }
        for (Object tmpO : bg) {
            n.remove(tmpO);
        }
    }

    public void trimNumber(ArrayList<N2ForceUnit> fList, int num, HashMap<N2ForceUnit, ArrayList<N2Paths>> pMap) {
        for (N2ForceUnit unit : pMap.keySet()) {
            ArrayList<N2Paths> pList = pMap.get(unit);
            N2Paths paths = pList.get(0);
            double dist = paths.getShortestLength();
            for (int i1 = 1; i1 < pList.size(); i1++) {
                N2Paths p2 = pList.get(i1);
                if (p2.getShortestLength() < dist) {
                    paths = p2;
                    dist = p2.getShortestLength();
                }
            }
            pList.clear();
            pList.add(paths);
        }
        if (pMap.size() > num) {
            int diff = pMap.size() - num;
            do {
                diff = removeFurthest(pMap, fList);
            } while (diff > num);
        }
    }

    public int removeFurthest(HashMap<N2ForceUnit, ArrayList<N2Paths>> mp, ArrayList<N2ForceUnit> fList) {
        double longest = -1.;
        N2ForceUnit furthestUnit = null;
        for (N2ForceUnit tmpF : mp.keySet()) {
            ArrayList<N2Paths> tmpP = mp.get(tmpF);
            int len = tmpP.get(0).getShortestLength();
            if (len > longest) {
                longest = len;
                furthestUnit = tmpF;
            }
        }
        mp.remove(furthestUnit);
        fList.remove(furthestUnit);
        return mp.size();
    }

    public N2ForceUnit getClosestUnit(N2Node target, ArrayList<N2ForceUnit> allUnits) {
        N2ForceUnit closest = null;
        Double2D targetLocation = target.getLocation();
        double shortestDist = 100000.;
        if (useActualDistance) {
            for (int i1 = 0; i1 < allUnits.size(); i1++) {
                N2ForceUnit unit = allUnits.get(i1);
                Double2D unitLoc = nodes.region.getObjectLocation(unit);
                unit.getNextNode(unitLoc);
                double dist = unitLoc.distance(targetLocation);
                if (dist < shortestDist) {
                    shortestDist = dist;
                    closest = unit;
                }
            }
        }
        if (useSteps) {
            int targetIndex = target.getNodeIndex();
            for (int i1 = 0; i1 < allUnits.size(); i1++) {
                N2ForceUnit unit = allUnits.get(i1);
                int unitHostIndex = unit.getHost().getNodeIndex();
                if (nodes.finder.allPaths[unitHostIndex][targetIndex].getShortestLength() > 0) {
                    N2Path shortestPath = nodes.finder.allPaths[unitHostIndex][targetIndex].shortestPaths.get(0);
                    double dist = shortestPath.getNumSteps();
                    if (dist < shortestDist) {
                        shortestDist = dist;
                        closest = unit;
                    }
                }
            }
        }
        if (useGraphDistance) {
            int targetIndex = target.getNodeIndex();
            for (int i1 = 0; i1 < allUnits.size(); i1++) {
                N2ForceUnit unit = allUnits.get(i1);
                int unitHostIndex = unit.getHost().getNodeIndex();
                if (nodes.finder.allPaths[unitHostIndex][targetIndex].getShortestLength() > 0) {
                    N2Path shortestPath = nodes.finder.allPaths[unitHostIndex][targetIndex].shortestPaths.get(0);
                    double dist = shortestPath.getTotalLength();
                    if (dist < shortestDist) {
                        shortestDist = dist;
                        closest = unit;
                    }
                }
            }
        }
        if (closest != null) {
            System.out.println("Closest unit = " + closest.getName());
            System.out.println("Status = " + closest.getUnitStatus() + " and faction = " + closest.getFaction());
        }
        return closest;
    }

    public N2ForceUnit getClosestUnit(N2Node target) {
        N2ForceUnit closest = null;
        nodes.pathFind();
        Bag nodesAndUnits = nodes.neighborhood.getAllNodes();
        ArrayList<N2ForceUnit> allUnits = new ArrayList();
        for (Object tmpO : nodesAndUnits) {
            if (tmpO instanceof N2ForceUnit) {
                N2ForceUnit tmpF = (N2ForceUnit) tmpO;
                int faction = tmpF.getFaction();
                if (faction == N2Constants.GOOD_GUYS && tmpF.getUnitStatus() == N2Constants.UNIT_STATUS_READY) {
                    allUnits.add(tmpF);
                }
            }
        }
        System.out.println(" Number of units available: " + allUnits.size());
        Double2D targetLocation = target.getLocation();
        double shortestDist = 100000.;
        if (useActualDistance) {
            for (int i1 = 0; i1 < allUnits.size(); i1++) {
                N2ForceUnit unit = allUnits.get(i1);
                Double2D unitLoc = nodes.region.getObjectLocation(unit);
                unit.getNextNode(unitLoc);
                double dist = unitLoc.distance(targetLocation);
                if (dist < shortestDist) {
                    shortestDist = dist;
                    closest = unit;
                }
            }
        }
        if (useSteps) {
            int targetIndex = target.getNodeIndex();
            for (int i1 = 0; i1 < allUnits.size(); i1++) {
                N2ForceUnit unit = allUnits.get(i1);
                int unitHostIndex = unit.getHost().getNodeIndex();
                if (nodes.finder.allPaths[unitHostIndex][targetIndex].getShortestLength() > 0) {
                    N2Path shortestPath = nodes.finder.allPaths[unitHostIndex][targetIndex].shortestPaths.get(0);
                    double dist = shortestPath.getNumSteps();
                    if (dist < shortestDist) {
                        shortestDist = dist;
                        closest = unit;
                    }
                }
            }
        }
        if (useGraphDistance) {
            int targetIndex = target.getNodeIndex();
            for (int i1 = 0; i1 < allUnits.size(); i1++) {
                N2ForceUnit unit = allUnits.get(i1);
                int unitHostIndex = unit.getHost().getNodeIndex();
                if (nodes.finder.allPaths[unitHostIndex][targetIndex].getShortestLength() > 0) {
                    N2Path shortestPath = nodes.finder.allPaths[unitHostIndex][targetIndex].shortestPaths.get(0);
                    double dist = shortestPath.getTotalLength();
                    if (dist < shortestDist) {
                        shortestDist = dist;
                        closest = unit;
                    }
                }
            }
        }
        if (closest != null) {
            System.out.println("Closest unit = " + closest.getName());
            System.out.println("Status = " + closest.getUnitStatus() + " and faction = " + closest.getFaction());
        }
        return closest;
    }
}
