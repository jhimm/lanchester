/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

//import heider8.HNode;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * ranks based upon number, strength, and or relative strengths of links.
 * determines for neighbors and next nearest neighbors
 *
 */
public class Connectedness implements Serializable{

    public int[][] links;
    public int numNodes = -1;
    public double[] connectedness;
    public double[] weightedConnectedness;
    public double[] nextNearestConnectedness;
    public double[] nextNearestWtConnectedness;
    public NodeUnit[] nodes = null;
    public Paths[][] paths = null;

    public Connectedness(int[][] net, Paths[][] paths) {
        this.links = net;
        this.numNodes = net.length;
        this.connectedness = new double[numNodes];
        this.weightedConnectedness = new double[numNodes];
        this.nextNearestConnectedness = new double[numNodes];
        this.nextNearestWtConnectedness = new double[numNodes];
        this.nodes = new NodeUnit[numNodes];
        this.paths = paths;
    }

    public void determineConnectedness() {
        getNodes();

    }

    public void findNumNeighbors() {
        for (int i1 = 0; i1 < numNodes; i1++) {
            this.connectedness[i1] = 0;
            this.weightedConnectedness[i1] = 0.;
            for (int i2 = 0; i2 < numNodes; i2++) {
                if (i2 != i1) {
                    this.connectedness[i1] += links[i1][i2];
                    this.weightedConnectedness[i1] += (links[i1][i2] - links[i2][i1]);
                }
            }
            this.nodes[i1].connectednessCentrality = this.connectedness[i1];
            this.nodes[i1].weightedConnectednessCentrality = this.weightedConnectedness[i1];
        }
        double maxConnect = 0;
        double maxWtConnect = 0;
        for (int i1 = 0; i1 < numNodes; i1++) {
            if (connectedness[i1] > maxConnect) {
                maxConnect = connectedness[i1];
            }
            if (weightedConnectedness[i1] > maxWtConnect) {
                maxWtConnect = weightedConnectedness[i1];
            }
        }
//        System.out.println("maxConnect = " + maxConnect);
        if (maxConnect > 0) {
            for (int i1 = 0; i1 < numNodes; i1++) {
                connectedness[i1] /= maxConnect;
                nodes[i1].connectednessCentrality = connectedness[i1];
                nodes[i1].currentStability = connectedness[i1];
            }
        }
        if (maxWtConnect > 0) {
            for (int i1 = 0; i1 < numNodes; i1++) {
                weightedConnectedness[i1] /= maxWtConnect;
                nodes[i1].weightedConnectednessCentrality = weightedConnectedness[i1];
                nodes[i1].currentStability = weightedConnectedness[i1];
            }
        }
    }

    public void findNumNextNearest() {
        findNumNeighbors();
        for (int i1 = 0; i1 < numNodes; i1++) {
            this.nextNearestConnectedness[i1] = 0;
            this.nextNearestWtConnectedness[i1] = 0.;
            for (int i2 = 0; i2 < numNodes; i2++) {
                if (i2 != i1) {
                    this.nextNearestConnectedness[i1] += links[i1][i2] * this.connectedness[i2];
                    this.nextNearestWtConnectedness[i1] += (links[i1][i2] - links[i2][i1]) * this.weightedConnectedness[i2];
                }
            }
            this.nodes[i1].nnConnectednessCentrality = this.nextNearestConnectedness[i1];
            this.nodes[i1].nnWeightedConnectednessCentrality = this.nextNearestWtConnectedness[i1];
        }
        double maxNNConnect = 0;
        double maxNNWtConnect = 0;
        for (int i1 = 0; i1 < numNodes; i1++) {
            if (nextNearestConnectedness[i1] > maxNNConnect) {
                maxNNConnect = nextNearestConnectedness[i1];
            }
            if (nextNearestWtConnectedness[i1] > maxNNWtConnect) {
                maxNNWtConnect = nextNearestWtConnectedness[i1];
            }
        }
        if (maxNNConnect > 0) {
            for (int i1 = 0; i1 < numNodes; i1++) {
                nextNearestConnectedness[i1] /= maxNNConnect;
                nodes[i1].nnConnectednessCentrality = nextNearestConnectedness[i1];
            }
        }
        if (maxNNWtConnect > 0) {
            for (int i1 = 0; i1 < numNodes; i1++) {
                nextNearestWtConnectedness[i1] /= maxNNWtConnect;
                nodes[i1].nnWeightedConnectednessCentrality = nextNearestWtConnectedness[i1];
            }
        }
    }

    public double[] getConnectedness() {
        return this.connectedness;
    }

    public double[] getNextNearestConnectedness() {
        return this.nextNearestConnectedness;
    }

    public double[] getWeightedConnectedness() {
        return this.weightedConnectedness;
    }

    public double[] getNextNearestWtConnectedness() {
        return this.nextNearestWtConnectedness;
    }

    public void getNodes() {
        for (int i1 = 0; i1 < numNodes; i1++) {
            if (i1 == 0) {
                this.nodes[i1] = paths[1][0].targetNode;
            } else {
                this.nodes[i1] = paths[i1][0].startNode;
            }
        }
    }
}
