/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

import loaders.N2DataSet;
import sim.engine.SimState;
import sim.field.continuous.Continuous2D;
import sim.field.network.Edge;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Double2D;
import Utils.IOUtils;
import java.util.ArrayList;

/**
 *
 * @author Jeff
 */
public class N2Nodes extends SimState {

    public Network neighborhood = new Network(false);
    public Continuous2D region = new Continuous2D(1.0, 100, 100);
    public N2Pathfinder finder = null;
    public int maxDistance = 30;
    public int[][] boiledLinks;
    public int numNodes = 50;
    public int numUnits = 40;
    public boolean localLinks = true;
    public boolean positiveLinks = true;
    public N2Node[] nodeArray = null;
    public N2ForceUnit[] unitArray = null;
    public int[][] linkSummary = new int[numNodes][numNodes];
    public int minLinks =4;
    public int maxLinks = 8;
    public int numLinks = -1;
    public int[] wts = {0, 1, 2, 3, 4, 5, 6};
    public Edge[][] edges = new Edge[numNodes][numNodes];
    public Edge[][][] mgedges = new Edge[numNodes][numNodes][2];
    public N2UnitManager manager = null;
    public N2DataSet data = null;

    public N2Nodes(long seed) {
        super(seed);
    }

    public void setData(N2DataSet data) {
        this.data = data;
        this.numNodes = data.nodes.size();
        reset();
        this.nodeArray = new N2Node[numNodes];
        for (int i1 = 0; i1 < numNodes; i1++) {
            N2Node tmpN = data.nodes.get(i1);
            region.setObjectLocation(tmpN,
                    tmpN.location);
            neighborhood.addNode(tmpN);
            nodeArray[tmpN.getNodeIndex()] = tmpN;
            tmpN.stopper = schedule.scheduleRepeating(tmpN);
        }
        this.numUnits = data.forces.size();
        unitArray = new N2ForceUnit[numUnits];
        for (int i1 = 0; i1 < numUnits; i1++) {
            N2ForceUnit tmpF = data.forces.get(i1);
            unitArray[tmpF.getForceIndex()] = tmpF;
            setDestination(tmpF);
            tmpF.stopper = schedule.scheduleRepeating(tmpF);
        }
        this.numLinks = data.links.size();
        for (int i1 = 0; i1 < numLinks; i1++) {
            N2LinkData link = data.links.get(i1);
            makeDualHLinks(nodeArray[link.fromIndex], nodeArray[link.toIndex], link.wt);
        }
        manager = new N2UnitManager(0);
        manager.stopper = schedule.scheduleRepeating(manager);
    }

    @Override
    public void start() {
        reset();
        nodeArray = new N2Node[numNodes];
        for (int i = 0; i < numNodes; i++) {
            N2Node node = new N2Node();
            node.setNodeIndex(i);
            node.setControllingFaction(random.nextInt(4));
            
            node.setValue(50.+random.nextDouble()*100);
            region.setObjectLocation(node,
                    new Double2D(region.getWidth() * random.nextDouble(),
                            region.getHeight() * random.nextDouble()));
            node.setLocation(region.getObjectLocation(node));
            neighborhood.addNode(node);
            nodeArray[i] = node;
            node.stopper = schedule.scheduleRepeating(node);
        }
        generateLocalNetwork(neighborhood.getAllNodes());
        unitArray = new N2ForceUnit[numUnits];
        for (int i = 0; i < numUnits; i++) {
            N2ForceUnit unit = new N2ForceUnit("Unit " + (i + 1), 500);
            unit.setForceIndex(i);
            N2Node node = nodeArray[random.nextInt(numNodes)];
            unit.setFaction(node.getControllingFaction());
            unit.setUnitType(1 + random.nextInt(4));
            unit.setHost(node);
            unit.setUnitStatus(N2Constants.UNIT_STATUS_READY);
            unit.setNumber(300.+random.nextDouble()*400.);
            setDestination(unit);
            unit.setReadinessLevel(random.nextInt(4));
            region.setObjectLocation(unit,
                    region.getObjectLocation(node));
            neighborhood.addNode(unit);
            unitArray[i] = unit;
            unit.stopper = schedule.scheduleRepeating(unit);
        }
        System.out.println("done with start");
        manager = new N2UnitManager(0);
        manager.stopper = schedule.scheduleRepeating(manager);

    }

    public void reset() {
        neighborhood = new Network(false);
        region = new Continuous2D(1.0, 100, 100);
        finder = null;
        linkSummary = new int[numNodes][numNodes];
        edges = new Edge[numNodes][numNodes];
        mgedges = new Edge[numNodes][numNodes][2];
        schedule.reset();
    }

    public void generateLocalNetwork(Bag nodes) {
        Bag nearestNeighbors = new Bag();
        Bag currentNeighbors = new Bag();
        int numNearest = 10;
        int numTries;
        for (Object tmpO : nodes) {
            numTries = 0;
            int v1Ind = ((N2Node) tmpO).nodeIndex;
            nearestNeighbors = getNearest(tmpO, numNearest);
            nearestNeighbors.remove(tmpO);
            updateNeighbors(currentNeighbors, tmpO);
//            System.out.println(" village " + v1Ind + " has " + bagSize + " neighbors");
            for (Object tmpO2 : currentNeighbors) {
                if (nearestNeighbors.contains(tmpO2)) {
                    nearestNeighbors.remove(tmpO2);
                }
            }
            for (Object tmpO2 : nearestNeighbors) {
//                makeDualLinks(tmpO, tmpO2);
                makeDualHLinks(tmpO, tmpO2);
            }
            linkSummary[v1Ind][v1Ind] = 0;
        }
        boilLinks();
        pathFind();
        System.out.println("done with local net");
    }

    public void setDestination(N2ForceUnit unit) {
        N2Node startNode = unit.getHost();
        int startIndex = startNode.getNodeIndex();
        finder.updateShortdist(maxDistance);
        N2Paths[] tmpPaths = finder.allPaths[startIndex];
        ArrayList<N2Paths> goodPaths = new ArrayList();
        for (int i1 = 0; i1 < tmpPaths.length; i1++) {
            if (tmpPaths[i1] != null) {
                goodPaths.add(tmpPaths[i1]);
            }
        }
        int numGood = goodPaths.size();
        int ind = random.nextInt(numGood);
        if (tmpPaths[ind].getShortestLength() > 0) {
            unit.setCurrentPath(tmpPaths[ind].shortestPaths.get(0));
        } else {
            unit.setCurrentPath(finder.allPaths[startIndex][startIndex].shortestPaths.get(0));
        }
    }

    public Bag getNearest(Object tmpO, int num) {
        Bag tmpB = new Bag();
        double width = region.getWidth();
        double frac = .1;
        int numClose = 0;
        Double2D loc = region.getObjectLocation(tmpO);
//        tmpB = region.getNearestNeighbors(loc, minLinks, false, false, true, tmpB);
        do {
            tmpB = region.getNeighborsWithinDistance(loc, width * frac);
            tmpB.remove(tmpO);
            numClose = tmpB.size();
            if (tmpB.size() < minLinks) {
                frac *= (1. + frac);
            }
            if (numClose > maxLinks) {
                frac *= (1 - frac);
            }
        } while (numClose < minLinks || numClose > maxLinks);
        return tmpB;
    }

    public void updateNeighbors(Bag tmpB, Object tmpO) {
        tmpB.clear();
        for (Object tmpE : neighborhood.getEdgesIn(tmpO)) {
            tmpB.add(((Edge) tmpE).from());
        }
    }

    public void makeDualHLinks(Object o1, Object o2) {
        int numWts = this.wts.length;
        int wtInd = random.nextInt(numWts);
        makeDualHLinks(o1, o2, wtInd);
    }

    public void makeDualHLinks(Object o1, Object o2, int wtInd) {
        N2LinkInfo li1 = new N2LinkInfo(this.wts[wtInd]);
        li1.setFrom(o1);
        li1.setTo(o2);
        N2LinkInfo li2 = new N2LinkInfo(this.wts[wtInd]);
        li2.setFrom(o2);
        li2.setTo(o1);
//        Edge e1 = new Edge(o1, o2, new N2LinkInfo(this.wts[wtInd]));
        Edge e1 = new Edge(o1, o2, li1);
        neighborhood.addEdge(e1);
        N2Node tmpV1 = (N2Node) o1;
        int v1Ind = tmpV1.nodeIndex;
        tmpV1.weight += this.wts[wtInd];
        tmpV1.neighbors.add(o2);
        Edge e2 = new Edge(o2, o1, new N2LinkInfo(this.wts[wtInd]));
        neighborhood.addEdge(e2);
        N2Node tmpV2 = (N2Node) o2;
        tmpV2.neighbors.add(o1);
        tmpV2.weight += this.wts[wtInd];
        int v2Ind = tmpV2.nodeIndex;
        linkSummary[v1Ind][v2Ind] = this.wts[wtInd];
        linkSummary[v2Ind][v1Ind] = this.wts[wtInd];
        edges[v1Ind][v2Ind] = e1;
        edges[v2Ind][v1Ind] = e2;
        mgedges[v1Ind][v2Ind] = new Edge[2];
        mgedges[v2Ind][v1Ind] = new Edge[2];
        mgedges[v1Ind][v2Ind][0] = e1;
        mgedges[v1Ind][v2Ind][1] = e2;
        mgedges[v2Ind][v1Ind][0] = e2;
        mgedges[v2Ind][v1Ind][1] = e1;
//        ((N2LinkInfo) e1.getInfo()).from = tmpV1;
//        ((N2LinkInfo) e1.getInfo()).to = tmpV2;
//        ((N2LinkInfo) e2.getInfo()).to = tmpV1;
//        ((N2LinkInfo) e2.getInfo()).from = tmpV2;

    }

    public void pathFind() {
        finder = new N2Pathfinder(boiledLinks, nodeArray);
        int numMults = this.maxDistance;
        finder.updateShortdist(numMults);
    }

    public int getMaxDistance() {
        return this.maxDistance;
    }

    public void setMaxDistance(int md) {
        this.maxDistance = md;
    }

    public void boilLinks() {
        boiledLinks = new int[numNodes][numNodes];
//        updateLinks();
        for (int i1 = 0; i1 < numNodes; i1++) {
            for (int i2 = 0; i2 < numNodes; i2++) {
                if (linkSummary[i1][i2] != 0) {
                    boiledLinks[i1][i2] = 1;
                }
                if (positiveLinks) {
                    if (linkSummary[i1][i2] > 0) {
                        boiledLinks[i1][i2] = 1;
                    } else {
                        boiledLinks[i1][i2] = 0;
                    }
                }
            }
        }
    }

    public void boilLinks(int faction) {
        boiledLinks = new int[numNodes][numNodes];
        updateLinks(faction);
        for (int i1 = 0; i1 < numNodes; i1++) {
            for (int i2 = 0; i2 < numNodes; i2++) {
                if (linkSummary[i1][i2] != 0) {
                    boiledLinks[i1][i2] = 1;
                }
                if (positiveLinks) {
                    if (linkSummary[i1][i2] > 0) {
                        boiledLinks[i1][i2] = 1;
                    } else {
                        boiledLinks[i1][i2] = 0;
                    }
                }
            }
        }
    }

    public void updateLinks(int faction) {
        linkSummary = new int[numNodes][numNodes];
        for (int i1 = 0; i1 < numNodes - 1; i1++) {
            for (int i2 = i1 + 1; i2 < numNodes; i2++) {
                Edge tmpE = edges[i1][i2];
                if (tmpE == null) {
                    linkSummary[i1][i2] = 0;
                    linkSummary[i2][i1] = 0;
                } else {
                    N2Node n1 = (N2Node) tmpE.getFrom();
                    N2Node n2 = (N2Node) tmpE.getTo();
                    int o1Faction = n1.getControllingFaction();
                    int o2Faction = n2.getControllingFaction();
                    linkSummary[i1][i2] = N2Constants.linkages[faction][o1Faction][o2Faction];
                    linkSummary[i2][i1] = linkSummary[i1][i2];
                }
            }
        }
    }
    public int getNumNodes(){
        return this.numNodes;
    }
    public void setNumNodes(int nn){
        this.numNodes=nn;
    }
    public int getMaxLinks(){
        return this.maxLinks;
    }
    public void setMaxLinks(int ml){
        this.maxLinks=ml;
    }
    public int getMinLinks(){
        return this.minLinks;
    }
    public void setMinLinks(int ml){
        this.minLinks=ml;
    }
    public int getNumUnits(){
        return this.numUnits;
    }
    public void setNumUnits(int nu){
        this.numUnits=nu;
    }

    public static void main(String[] args) {
        doLoop(N2Nodes.class, args);
        System.exit(0);
    }
}
