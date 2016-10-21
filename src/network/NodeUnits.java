/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

import java.util.HashMap;
import org.apache.commons.math3.linear.RealVector;
import sim.engine.SimState;
import static sim.engine.SimState.doLoop;
import sim.field.continuous.Continuous2D;
import sim.field.network.Edge;
import sim.field.network.Network;
import sim.util.Bag;
import sim.util.Double2D;
import Utils.IOUtils;
/**
 *
 * @author Jeff
 */

//import heider8.brokers.WeightedBigBroker;
//import heider8.brokers.HBroker2;
//import Finders.*;
//import Utils.IOUtils;
//import vso.mason.*;
//import VSOexp2.*;
//import sim.engine.*;
//import sim.util.*;
//import sim.field.continuous.*;
//import sim.field.network.*;
//import Modifiers.History;
import java.util.HashMap;
//import java.util.ArrayList;
//import org.apache.commons.math3.linear.*;

/**
 *
 * @author Jeff
 */
public class NodeUnits extends SimState {

    public double endogenousEffort = 0.1;
    public int numNodes = 50;
    public int minLinks = 3;
    public int maxLinks = 7;
    public int maxDistance = 20;
    public int eigenIters = 30;
    public int[][] boiledLinks;
    public Pathfinder finder = null;
//    public Betweenness tween = null;
    public Connectedness connect = null;
//    public Eigen eigen = null;
    public NodeUnit[] nodeArray = null;
    public boolean scaleFree = false;
    public boolean randomLinks = false;
    public boolean localLinks = true;
    public boolean betweenness = false;
    public boolean positiveLinks = false;
    public boolean eigenness = false;
    public boolean connectedness = true;
    public boolean weightedConnectedness = false;
    public boolean nextNearestConnectedness = false;
    public boolean nextNearestWeightedConnectedness = false;
    public boolean manualLoad = false;
    public boolean useWts1_1 = true;
    public boolean useWts10_1 = false;
    public boolean useWts21_1_2 = false;
    public boolean useWts210_1_2 = false;
    public boolean reposition = false;
    public boolean regularSelector = false;
    public boolean betweennessMod = false;
    public boolean connectednessMod = true;
    public boolean adversaryMod = false;
    public static boolean resetSeed = false;
    public Continuous2D region = new Continuous2D(1.0, 100, 100);
    public double meanStability = .4;
    public int[][] linkSummary = new int[numNodes][numNodes];
    public double[][] dLinkSummary = new double[numNodes][numNodes];
    public static double minWeight = 0.5;
    public static double maxWeight = 1.5;
    public static double delWeight = maxWeight - minWeight;
    public int[] wts1_1 = {1, -1};
    public int[] wts21_1_2 = {2, 1, -1, -2};
    public int[] wts10_1 = {1, 0, -1};
    public int[] wts210_1_2 = {2, 1, 0, -1, -2};
    public int[] wts = null;
    public boolean hasRoute = false;
    public String outFileName = "paths.csv";
    public boolean appendOutFile = false;
    public double moveLevel = 0.99;
    public long currentSeed = 0;
    public HashMap<String, NodeUnit> nodeMap = new HashMap();
    public HashMap<NodeUnit, Integer> nodeNumMap = new HashMap();
//    public HBroker2[] brokers = null;
    public int numBrokers = 1;
    public Edge[][] edges = new Edge[numNodes][numNodes];
    public Edge[][][] mgedges = new Edge[numNodes][numNodes][2];
    public double xMin = 2.;
    public double yMin = 2.;
    public double xMax = 98.;
    public double yMax = 98.;
    public boolean randomBrokers = false;
//    public History history = new History();
//    public WeightedBigBroker broker = null;
    public boolean considerZero = true;
    public boolean asymmetricLinks = false;
    public Bag fixedLinks = new Bag();
    public boolean[][] isMaleable = null;
    public boolean[][] areLinked = null;

    public NodeUnits() {
        super(System.currentTimeMillis());
    }

    public NodeUnits(Long seed) {
        super(seed);
        currentSeed = seed;
        if (this.resetSeed) {
            currentSeed = System.currentTimeMillis();
            this.random.setSeed(currentSeed);
        }
    }

    public NodeUnits(Long seed, boolean reseed) {
        super(seed);
        this.resetSeed = reseed;
        if (this.resetSeed) {
            this.random.setSeed(System.currentTimeMillis());
        }
        currentSeed = this.seed();
    }

    public long getCurrentSeed() {
        return this.currentSeed;
    }

    public int getNumBrokers() {
        return this.numBrokers;
    }

    public void setNumBrokers(int nb) {
        this.numBrokers = nb;
    }

    public boolean getrandomBrokers() {
        return this.randomBrokers;
    }

    public void setRandomBrokers(boolean rb) {
        this.randomBrokers = rb;
    }

    public int getMinLinks() {
        return this.minLinks;
    }

    public void setMinLinks(int ml) {
        this.minLinks = ml;
    }

    public int getMaxLinks() {
        return this.maxLinks;
    }

    public void setMaxLinks(int ml) {
        this.maxLinks = ml;
    }

    public int getMaxDistance() {
        return this.maxDistance;
    }

    public void setMaxDistance(int md) {
        this.maxDistance = md;
    }

    public boolean getAppendOutFile() {
        return this.appendOutFile;
    }

    public void setAppendOutFile(boolean aof) {
        this.appendOutFile = aof;
    }

    public String getOutFileName() {
        return this.outFileName;
    }

    public void setOutFileName(String ofn) {
        this.outFileName = ofn;
    }

    public boolean getBetweenness() {
        return this.betweenness;
    }

    public boolean getPositiveLinks() {
        return this.positiveLinks;
    }

    public void setPositiveLinks(boolean pb) {
        this.positiveLinks = pb;
    }

    public void setBetweenness(boolean bet) {
        this.betweenness = bet;
        this.eigenness = !bet;
        this.connectedness = !bet;
        this.weightedConnectedness = !bet;
        this.nextNearestWeightedConnectedness = !bet;
        this.nextNearestConnectedness = !bet;
    }

    public boolean getEigenness() {
        return this.eigenness;
    }

    public boolean getBetweennessMod() {
        return this.betweennessMod;
    }

    public boolean getConnectednessMod() {
        return this.connectednessMod;
    }

    public boolean getAdversaryMod() {
        return this.adversaryMod;
    }

    public void setBetweennessMod(boolean mod) {
        this.betweennessMod = mod;
    }

    public void setConnectednessMod(boolean mod) {
        this.connectednessMod = mod;
    }

    public void setAdversaryMod(boolean mod) {
        this.adversaryMod = mod;
    }

    public void setEigenness(boolean bet) {
        this.eigenness = bet;
        this.betweenness = !bet;
        this.connectedness = !bet;
        this.weightedConnectedness = !bet;
        this.nextNearestWeightedConnectedness = !bet;
        this.nextNearestConnectedness = !bet;
    }

    public boolean getConnectedness() {
        return this.connectedness;
    }

    public void setConnectedness(boolean con) {
        this.nextNearestWeightedConnectedness = !con;
        this.nextNearestConnectedness = !con;
        this.weightedConnectedness = !con;
        this.connectedness = con;
        this.betweenness = !con;
        this.eigenness = !con;
    }

    public boolean getWeightedConnectedness() {
        return this.weightedConnectedness;
    }

    public void setWeightedConnectedness(boolean con) {
        this.nextNearestWeightedConnectedness = !con;
        this.nextNearestConnectedness = !con;
        this.weightedConnectedness = con;
        this.connectedness = !con;
        this.betweenness = !con;
        this.eigenness = !con;
    }

    public boolean getNextNearestConnectedness() {
        return this.nextNearestConnectedness;
    }

    public void setNextNearestConnectedness(boolean con) {
        this.nextNearestWeightedConnectedness = !con;
        this.nextNearestConnectedness = con;
        this.weightedConnectedness = !con;
        this.connectedness = !con;
        this.betweenness = !con;
        this.eigenness = !con;
    }

//    public boolean getReposition() {
//        return this.reposition;
//    }
//
//    public void setReposition(boolean rpos) {
//        this.reposition = rpos;
//        for (HBroker2 tmpB : brokers) {
//            tmpB.reposition = rpos;
//        }
//    }
//
//    public boolean getRegularSelector() {
//        return this.regularSelector;
//    }
//
//    public void setRegularSelector(boolean rpos) {
//        this.regularSelector = rpos;
//        for (HBroker2 tmpB : brokers) {
//            tmpB.regularSelector = rpos;
//        }
//    }
    public boolean getUseWts1_1() {
        return this.useWts1_1;
    }

    public boolean getUseWts10_1() {
        return this.useWts10_1;
    }

    public boolean getUseWts21_1_2() {
        return this.useWts21_1_2;
    }

    public boolean getUseWts210_1_2() {
        return this.useWts210_1_2;
    }

    public void setUseWts1_1(boolean w1) {
        this.useWts1_1 = w1;
        if (w1) {
            this.useWts10_1 = false;
            this.useWts21_1_2 = false;
            this.useWts210_1_2 = false;
            this.wts = this.wts1_1;
        }
    }

    public void setUseWts10_1(boolean w1) {
        this.useWts10_1 = w1;
        if (w1) {
            this.useWts1_1 = false;
            this.useWts21_1_2 = false;
            this.useWts210_1_2 = false;
            this.wts = this.wts10_1;
        }
    }

    public void setUseWts21_1_2(boolean w1) {
        this.useWts21_1_2 = w1;
        if (w1) {
            this.useWts1_1 = false;
            this.useWts10_1 = false;
            this.useWts210_1_2 = false;
            this.wts = this.wts21_1_2;
        }
    }

    public void setUseWts210_1_2(boolean w1) {
        this.useWts210_1_2 = w1;
        if (w1) {
            this.useWts1_1 = false;
            this.useWts10_1 = false;
            this.useWts21_1_2 = false;

            this.wts = this.wts210_1_2;
        }
    }

    public boolean getConsiderZero() {
        return this.considerZero;
    }

    public void setConsiderZero(boolean cz) {
        if (useWts10_1 || useWts210_1_2) {
            this.considerZero = cz;
        } else {
            this.considerZero = false;
        }
    }

    public boolean getNextNearestWeightedConnectedness() {
        return this.nextNearestWeightedConnectedness;
    }

    public void setNextNearestWeightedConnectedness(boolean con) {
        this.nextNearestWeightedConnectedness = con;
        this.nextNearestConnectedness = !con;
        this.weightedConnectedness = !con;
        this.connectedness = !con;
        this.betweenness = !con;
        this.eigenness = !con;
    }

    public int getNumNodes() {
        return this.numNodes;
    }

    public void setNumNodes(int num) {
        if (num > 0) {
            this.numNodes = num;
            linkSummary = new int[numNodes][numNodes];
            dLinkSummary = new double[numNodes][numNodes];
            edges = new Edge[numNodes][numNodes];
            mgedges = new Edge[numNodes][numNodes][2];
        }
    }

    public boolean isScaleFree() {
        return this.scaleFree;

    }

    public void setScaleFree(boolean sf) {
        this.scaleFree = sf;
        this.randomLinks = !sf;
        this.localLinks = !sf;
    }

    public boolean getResetSeed() {
        return this.resetSeed;
    }

    public void setResetSeed(boolean s) {
        this.resetSeed = s;
    }

    public boolean isRandomLinks() {
        return this.randomLinks;
    }

    public void setRandomLinks(boolean rl) {
        this.randomLinks = rl;
        this.scaleFree = !rl;
        this.localLinks = !rl;
    }

    public boolean isLocalLinks() {
        return this.localLinks;
    }

    public void setLocalLinks(boolean ll) {
        this.localLinks = ll;
        this.randomLinks = !ll;
        this.scaleFree = !ll;
    }

    public void setAsymmetricLinks(boolean asl) {
        this.asymmetricLinks = asl;
    }

    public boolean getAsymmetricLinks() {
        return this.asymmetricLinks;
    }
    public Network neighborhood = new Network(false);

    public void load3DNodes() {
        Bag nodes = neighborhood.getAllNodes();
        for (int i = 0; i < nodes.size(); i++) {
            NodeUnit node = (NodeUnit) (nodes.get(i));
            node.setParams(this);
            node.setCurrentStability(0.1);
            setNodeCentrality(node);
        }
    }

    public void addNode(String vName, Double2D loc) {
        NodeUnit node = new NodeUnit();
        vName = vName.trim();
        node.nodeName = vName;
        node.location = loc;
        region.setObjectLocation(node, loc);
        neighborhood.addNode(node);
        node.stopper = schedule.scheduleRepeating(node);
        nodeMap.put(vName, node);
    }

    public void fizzNode(NodeUnit node) {
        boolean amicable = true;
        double delta = 3.;
        NodeUnit v1 = new NodeUnit();
        NodeUnit v2 = new NodeUnit();
        double x = node.location.x;
        double y = node.location.y;
        double theta = 2. * Math.PI * random.nextDouble();
        double sin = Math.sin(theta);
        double cos = Math.cos(theta);
        Double2D l1 = new Double2D(x + delta * cos, y + delta * sin);
        Double2D l2 = new Double2D(x - delta * cos, y - delta * sin);
        neighborhood.addNode(v1);
        neighborhood.addNode(v2);
        v1.stopper = schedule.scheduleRepeating(v1);
        v2.stopper = schedule.scheduleRepeating(v2);
        v1.location = l1;
        v2.location = l2;
        region.setObjectLocation(v1, v1.location);
        region.setObjectLocation(v2, v2.location);
        updateLinks();
        if (amicable) {
            makeDualHLinks(v1, v2, 0);
        } else {
            makeDualHLinks(v1, v2, 1);
        }
        int numNeighbors = node.neighbors.size();
        for (int i1 = 0; i1 < numNeighbors; i1++) {
            if (i1 % 2 == 0) {
                makeDualHLinks(v1, node.neighbors.get(i1));
            } else {
                makeDualHLinks(v2, node.neighbors.get(i1));
            }
        }
        if (numNeighbors == 1) {
            makeDualHLinks(v2, node.neighbors.get(0));
        }
        removeNode(node);
    }

    public void fuzeNodes(NodeUnit v1, NodeUnit v2) {
        updateLinks();
        Bag n1 = v1.neighbors;
        Bag n2 = v2.neighbors;
        Bag n31 = new Bag();
        Bag n32 = new Bag();
        Bag n4 = new Bag();
        for (Object tmpO : n1) {
            if (!n2.contains(tmpO)) {
                n31.add(tmpO);
            } else {
                n4.add(tmpO);
            }
        }
        for (Object tmpO : n2) {
            if (!n1.contains(tmpO)) {
                n32.add(tmpO);
            }
        }
        NodeUnit v3 = new NodeUnit();
        double x = (v1.location.x + v2.location.x) / 2.;
        double y = (v1.location.y + v2.location.y) / 2.;
        Double2D l3 = new Double2D(x, y);
        v3.location = l3;
        region.setObjectLocation(v3, l3);
        neighborhood.addNode(v3);
        v3.stopper = schedule.scheduleRepeating(v3);
        updateLinks();
        int ind1 = v1.nodeIndex;
        int ind2 = v2.nodeIndex;
        int tmpI = -10;
        int tmpWt = -10;
        int tmpWt1 = -10;
        int tmpWt2 = -10;
        try {
            for (Object tmpO : n31) {
                tmpI = ((NodeUnit) tmpO).nodeIndex;
                tmpWt = ((LinkInfo) edges[ind1][tmpI].getInfo()).wt;
                makeDualHLinks(v3, tmpO, (tmpWt < 0 ? 1 : 0));
            }
            for (Object tmpO : n32) {
                tmpI = ((NodeUnit) tmpO).nodeIndex;
                tmpWt = ((LinkInfo) edges[ind2][tmpI].getInfo()).wt;
                makeDualHLinks(v3, tmpO, (tmpWt < 0 ? 1 : 0));
            }
            for (Object tmpO : n4) {
                tmpI = ((NodeUnit) tmpO).nodeIndex;
                tmpWt1 = ((LinkInfo) edges[ind1][tmpI].getInfo()).wt;
                tmpWt2 = ((LinkInfo) edges[ind2][tmpI].getInfo()).wt;
                if (tmpWt1 == tmpWt2) {
                    makeDualHLinks(v3, tmpO, (tmpWt1 < 0 ? 1 : 0));
                } else if (random.nextBoolean()) {
                    makeDualHLinks(v3, tmpO, (tmpWt1 < 0 ? 1 : 0));
                } else {
                    makeDualHLinks(v3, tmpO, (tmpWt2 < 0 ? 1 : 0));
                }
            }
        } catch (Exception ex) {
            System.out.println("indices " + ind1 + ", " + ind2 + ", " + tmpI + " and wts " + tmpWt + ", " + tmpWt1 + ",  " + tmpWt2);
        }
        removeNode(v1);
        removeNode(v2);
    }

    public void removeNode(NodeUnit node) {
        int vInd = node.nodeIndex;
        for (int i1 = 0; i1 < numNodes; i1++) {
            for (int i2 = 0; i2 < numNodes; i2++) {
                if (i1 == vInd || i2 == vInd) {
                    linkSummary[i1][i2] = 0;
                    linkSummary[i2][i1] = 0;
                    edges[i1][i2] = null;
                    edges[i2][i1] = null;
                }
            }
        }
        for (int i1 = 0; i1 < nodeArray.length; i1++) {
            if (i1 != vInd) {
                if (nodeArray[i1].neighbors.contains(node)) {
                    nodeArray[i1].neighbors.remove(node);
                }
            }
        }
//        System.out.println("attempting to remove village " + node.nodeIndex);
        region.remove(node);
        neighborhood.removeNode(node);
        node.stopper.stop();
        nodeMap.remove(node.nodeName);
        int num = neighborhood.getAllNodes().size();
        numNodes = num;
        updateLinks();
        findRankings();
    }

    public void updateLinks() {
        int num = neighborhood.getAllNodes().size();
        numNodes = num;
//        System.out.println("number of villages = " + num);
        nodeArray = new NodeUnit[num];
        Bag nodes = neighborhood.getAllNodes();
        for (int i1 = 0; i1 < num; i1++) {
            Object tmpO = nodes.get(i1);
            NodeUnit tmpV = (NodeUnit) tmpO;
            tmpV.setNodeIndex(i1);
            nodeArray[i1] = tmpV;
        }
        linkSummary = new int[num][num];
        edges = new Edge[num][num];
        mgedges = new Edge[numNodes][numNodes][2];
        for (int i1 = 0; i1 < num; i1++) {
            Object tmpO = neighborhood.getAllNodes().get(i1);
            NodeUnit tmpVfrom = (NodeUnit) tmpO;
            for (Object tmpO2 : tmpVfrom.neighbors) {
                NodeUnit tmpVto = (NodeUnit) tmpO2;
                Edge tmpE = neighborhood.getEdge(tmpO, tmpO2);
                if (tmpE != null) {
//                    int tmpWt = 0;
                    LinkInfo tmpHLI = ((LinkInfo) tmpE.getInfo());
                    linkSummary[tmpVfrom.nodeIndex][tmpVto.nodeIndex] = tmpHLI.getWeight();
                    edges[tmpVfrom.nodeIndex][tmpVto.nodeIndex] = tmpE;
                } else {
//                    linkSummary[tmpVfrom.nodeIndex][tmpVto.nodeIndex] = 0;
                }
            }
            linkSummary[tmpVfrom.nodeIndex][tmpVfrom.nodeIndex] = 0;
        }
        boilLinks();
    }

    public void adjustLocation(NodeUnit node) {
        double delx = 0.;
        double dely = 0.;
        double delta = 0.;
        double x0 = node.location.x;
        double y0 = node.location.y;
        int v0 = node.nodeIndex;
        for (Object tmpO : node.neighbors) {
            NodeUnit tmpV = (NodeUnit) tmpO;
            int v1 = tmpV.nodeIndex;
            double x1 = tmpV.location.x;
            double y1 = tmpV.location.y;
            if (linkSummary[v0][v1] > 0) {
                if (x1 > x0) {
                    delta = (x1 - x0) * .8;
                    if (delta > 5.) {
                        delta = 5.;
                    }
                    delx += delta;
                } else {
                    delta = (x0 - x1) * .8;
                    if (delta > 5.) {
                        delta = 5.;
                    }
                    delx -= delta;
                }
                if (y1 > y0) {
                    delta = (y1 - y0) * .8;
                    if (delta > 5.) {
                        delta = 5.;
                    }
                    dely += delta;
                } else {
                    delta = (y0 - y1) * .8;
                    if (delta > 5.) {
                        delta = 5.;
                    }
                    dely -= delta;
                }
            } else if (linkSummary[v0][v1] < 0) {
                if (x1 > x0) {
                    delta = x1 - x0;
                    if (delta < 10.) {
                        delx -= 1.;
                    }
                } else {
                    delta = x0 - x1;
                    if (delta < 10.) {
                        delx += 1.;
                    }
                }
                if (y1 > y0) {
                    delta = y1 - y0;
                    if (delta < 10.) {
                        dely -= 1.;
                    }
                } else {
                    delta = y0 - y1;
                    if (delta < 10.) {
                        dely += 1.;
                    }
                }
            }
        }
        Double2D newLoc = checkBounds(x0 + delx, y0 + dely);
        adjustLocation(node, newLoc);
    }

    public void adjustLocation(NodeUnit node, Double2D loc) {
        node.location = loc;
        region.setObjectLocation(node, loc);
    }

    public Double2D checkBounds(double x, double y) {
        if (x < xMin) {
            x = xMin;
        }
        if (x > xMax) {
            x = xMax;
        }
        if (y < yMin) {
            y = yMin;
        }
        if (y > yMax) {
            y = yMax;
        }
        return new Double2D(x, y);
    }

    @Override
    public void start() {
        super.start();
        nodeArray = null;
//        villNumMap.clear();
        neighborhood.clear();
        region.clear();
//        brokers = null;
        schedule.clear();
//        region3D.clear();
        finder = null;
//        tween = null;

        if (this.manualLoad) {
            //assume all villages have been added, and links constructed
            load3DNodes();
        } else {
            nodeArray = new NodeUnit[numNodes];
            for (int i = 0; i < numNodes; i++) {
                NodeUnit node = new NodeUnit();
                node.setNodeIndex(i);
                region.setObjectLocation(node,
                        new Double2D(region.getWidth() * random.nextDouble(),
                                region.getHeight() * random.nextDouble()));
                node.location = region.getObjectLocation(node);
                neighborhood.addNode(node);
//                villNumMap.put(village, i);
                nodeArray[i] = node;
                node.stopper = schedule.scheduleRepeating(node);
            }
            // define like/dislike relationships
            Bag villages = neighborhood.getAllNodes();
            if (this.useWts1_1) {
                setUseWts1_1(true);
            } else if (this.useWts10_1) {
                setUseWts10_1(true);
            } else if (this.useWts21_1_2) {
                setUseWts21_1_2(true);
            } else if (this.useWts210_1_2) {
                setUseWts210_1_2(true);
            }
            this.edges = new Edge[numNodes][numNodes];
            this.linkSummary = new int[numNodes][numNodes];
            if (this.randomLinks) {
                generateRandomNet(villages);
            }
            if (this.scaleFree) {
                generateScaleFreeNet(villages);
            }
            if (this.localLinks) {
                generateLocalNetwork(villages);
            }
            load3DNodes();
            findRankings();
//            broker = new WeightedBigBroker(0);
//            broker.stopper = schedule.scheduleRepeating(broker);
        }
        boolean outputPaths = true;
        if (outputPaths) {
            java.io.File oFile = new java.io.File(outFileName);
            if (!appendOutFile) {
                IOUtils.string2file(oFile, "", false);
            }
//        String tmpS="";
            for (int i1 = 0; i1 < numNodes - 1; i1++) {
                for (int i2 = i1; i2 < numNodes; i2++) {
                    for (Path tmpP : finder.allPaths[i1][i2].shortestPaths) {
//                    tmpS=i1+","+i2+","+tmpP.toString();
                        IOUtils.string2file(oFile, tmpP.toString(), true);
                    }
                }
            }
        }
        this.hasRoute = true;

    }

    public void findRankings() {
        boilLinks();
        pathFind();
        if (betweenness) {
//            calculateBetweenness();
        } else if (connectedness) {
            calculateConnectedness();
        } else if (weightedConnectedness) {
            calculateConnectedness();
        } else if (nextNearestConnectedness) {
            calculateNextNearestConnectedness();
        } else if (nextNearestWeightedConnectedness) {
            calculateNextNearestConnectedness();
//        } else if (eigenness) {
//            calculateEigenness();
        }

    }

    public void setNodeCentralities() {
        for (int i1 = 0; i1 < numNodes; i1++) {
            setNodeCentrality(nodeArray[i1]);
        }
    }

    public void setNodeCentrality(NodeUnit node) {
        node.betweenness = this.betweenness;
        node.connectedness = this.connectedness;
        node.weightedConnectedness = this.weightedConnectedness;
        node.nextNearestConnectedness = this.nextNearestConnectedness;
        node.nextNearestWeightedConnectedness = this.nextNearestWeightedConnectedness;
        node.eigenness = this.eigenness;
    }

    public void pathFind() {
        finder = new Pathfinder(boiledLinks, nodeArray);
        int numMults = this.maxDistance;
        finder.updateShortdist(numMults);
    }

//    public void calculateBetweenness() {
//        tween = new Betweenness(finder.allPaths);
//    }
//    public void calculateEigenness() {
//        eigen = new Eigen(boiledLinks, eigenIters);
//        double[] dwts;
//        dwts = eigen.getWts();
//        for (int i1 = 0; i1 < this.numNodes; i1++) {
//            finder.nodes[i1].currentStability = dwts[i1];
//            finder.nodes[i1].eigenCentrality = dwts[i1];
//        }
//    }
    public void calculateConnectedness() {
        connect = new Connectedness(boiledLinks, finder.allPaths);
        connect.determineConnectedness();
        connect.findNumNeighbors();
    }

    public void calculateNextNearestConnectedness() {
        connect = new Connectedness(boiledLinks, finder.allPaths);
        connect.determineConnectedness();
        connect.findNumNextNearest();
    }

    public String eString(RealVector rv) {
        double[] v = rv.toArray();
        int len = v.length;
        String tmpS = " ";
        for (int i1 = 0; i1 < len; i1++) {
            tmpS += v[i1] + " , ";
        }
        return tmpS;
    }

    public void fillDLinkSummary() {
        for (int i1 = 0; i1 < numNodes; i1++) {
            for (int i2 = 0; i2 < numNodes; i2++) {
                dLinkSummary[i1][i2] = (double) linkSummary[i1][i2];
            }
            dLinkSummary[i1][i1] = 0.;
        }
    }

    public void boilLinks() {
        boiledLinks = new int[numNodes][numNodes];
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

    public int[][] matMult(int[][] a1, int[][] a2) {
        int nr1 = a1.length;
        int nc1 = a1[0].length;
        int nr2 = a2.length;
        int nc2 = a2[0].length;
        if (nc1 != nr2) {
            System.out.println("matrix mis-match for multiplying");
            return null;
        }
        int[][] res = new int[nr1][nc2];
        for (int i1 = 0; i1 < nr1; i1++) {
            for (int i2 = 0; i2 < nc2; i2++) {
                int sum = 0;
                for (int i3 = 0; i3 < nc1; i3++) {
                    sum += a1[i1][i3] * a2[i3][i2];
                }
                res[i1][i2] = sum;
            }
        }
        return res;
    }

    public void generateLocalNetwork(Bag nodes) {
        Bag nearestNeighbors = new Bag();
        Bag currentNeighbors = new Bag();
        int numNearest = 10;
        int numTries;
        for (Object tmpO : nodes) {
            numTries = 0;
            int v1Ind = ((NodeUnit) tmpO).nodeIndex;
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

    @Override
    public void awakeFromCheckpoint() {
        super.awakeFromCheckpoint();
        if (this.resetSeed) {
            this.random.setSeed(System.currentTimeMillis());
        }
    }

    public void generateScaleFreeNet(Bag nodes) {
        Bag linked = new Bag();
        linked.add(nodes.get(0));
        linked.add(nodes.get(1));
        makeDualHLinks(nodes.get(0), nodes.get(1));
        for (int i = 2; i < nodes.size(); i++) {
            Object village = nodes.get(i);
            Object villageB = null;
//add links to at least 2 villages
            do {
                villageB = linked.get(random.nextInt(linked.numObjs));
            } while (village == villageB);
            makeDualHLinks(village, villageB);
            linked.add(village);
            linked.add(villageB);
        }
    }

    public void updateNeighbors(Bag tmpB, Object tmpO) {
        tmpB.clear();
        for (Object tmpE : neighborhood.getEdgesIn(tmpO)) {
            tmpB.add(((Edge) tmpE).from());
        }
    }

    public void makeDualLinks(Object o1, Object o2) {
        double tmpWeight = minWeight + (delWeight) * random.nextDouble();
        neighborhood.addEdge(o1, o2, tmpWeight);
        NodeUnit tmpV = (NodeUnit) o1;
        tmpV.weight += tmpWeight;
        tmpWeight = minWeight + (delWeight) * random.nextDouble();
        neighborhood.addEdge(o2, o1, tmpWeight);
        tmpV = (NodeUnit) o2;
        tmpV.weight += tmpWeight;
    }

    public void makeDualHLinks(Object o1, Object o2) {
        int numWts = this.wts.length;
        int wtInd = random.nextInt(numWts);
        makeDualHLinks(o1, o2, wtInd);
    }

    public void makeDualHLinks(Object o1, Object o2, int wtInd) {
        LinkInfo li1 = new LinkInfo(this.wts[wtInd]);
        li1.setFrom(o1);
        li1.setTo(o2);
        LinkInfo li2 = new LinkInfo(this.wts[wtInd]);
        li2.setFrom(o2);
        li2.setTo(o1);
//        Edge e1 = new Edge(o1, o2, new LinkInfo(this.wts[wtInd]));
        Edge e1 = new Edge(o1, o2, li1);
        neighborhood.addEdge(e1);
        NodeUnit tmpV1 = (NodeUnit) o1;
        int v1Ind = tmpV1.nodeIndex;
        tmpV1.weight += this.wts[wtInd];
        tmpV1.neighbors.add(o2);
        Edge e2 = new Edge(o2, o1, new LinkInfo(this.wts[wtInd]));
        neighborhood.addEdge(e2);
        NodeUnit tmpV2 = (NodeUnit) o2;
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
//        ((LinkInfo) e1.getInfo()).from = tmpV1;
//        ((LinkInfo) e1.getInfo()).to = tmpV2;
//        ((LinkInfo) e2.getInfo()).to = tmpV1;
//        ((LinkInfo) e2.getInfo()).from = tmpV2;

    }

    public void generateRandomNet(Bag nodes) {
        for (int i = 0; i < nodes.size(); i++) {
            Object node = nodes.get(i);
            Object nodeB = null;
            Object nodeC = null;
//add links to at least 2 villages
            do {
                nodeB = nodes.get(random.nextInt(nodes.numObjs));
            } while (node == nodeB);
            makeDualHLinks(node, nodeB);
            do {
                nodeC = nodes.get(random.nextInt(nodes.numObjs));
            } while (nodeC == nodeB || nodeC == node);
            makeDualHLinks(node, nodeC);
        }
    }

    public static void main(String[] args) {
        doLoop(NodeUnits.class, args);
        System.exit(0);
    }
}
