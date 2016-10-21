/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package network;

//import heider8.HNode;
import java.io.Serializable;
//import java.util.ArrayList;

/**
 *
 * @author Jeff
 */
public class Pathfinder implements Serializable{

    public Paths[][] allPaths;
    public int numNodes = -1;
    public int[][] dist;
    public int[][][] shortDist;
    public NodeUnit[] nodes;

    public Pathfinder(int[][] dist, NodeUnit[] nodeArray) {
        int num = dist.length;
//        if(num<2)System.out.println("************************************** array too small ");
        this.dist = dist;
        this.shortDist = new int[num / 2][num][num];
        this.numNodes = num;
        this.allPaths = new Paths[num][num];
        this.nodes = nodeArray;
        shortDist[0] = dist;
    }

    public void updateShortdist(int k) {
        // get paths for nearest neighbors...
        initializePaths();
        k=Math.min(k,this.dist.length/2);
        for (int i3 = 1; i3 < k; i3++) {
            shortDist[i3] = pseudoMatMult(shortDist[i3 - 1], dist);
        }

    }

    public void initializePaths() {
        this.allPaths = new Paths[numNodes][numNodes];
        for (int i1 = 0; i1 < numNodes - 1; i1++) {
            for (int i2 = i1 + 1; i2 < numNodes; i2++) {
                Paths tmpPSa;
                Paths tmpPSb;
                if (dist[i1][i2] != 0) {
                    Path tmpP = new Path(nodes[i1], nodes[i2]);
                    tmpP.addNode(nodes[i2]);
                    tmpPSa = new Paths(1);
                    tmpPSa.addPath(tmpP);
                    tmpPSb = new Paths(1);
                    tmpP = new Path(nodes[i2], nodes[i1]);
                    tmpP.addNode(nodes[i1]);
                    tmpPSb.addPath(tmpP);
                } else {
                    tmpPSa = new Paths(-1);
                    tmpPSb = new Paths(-1);
                }
                tmpPSa.setStartNode(nodes[i1]);
                tmpPSa.setEndNode(nodes[i2]);
                tmpPSb.setStartNode(nodes[i2]);
                tmpPSb.setEndNode(nodes[i1]);
                allPaths[i1][i2] = tmpPSa;
                allPaths[i2][i1] = tmpPSb;
            }

        }
        for (int i1 = 0; i1 < numNodes; i1++) {
            Path tmpP = new Path(nodes[i1], nodes[i1]);
            Paths tmpPS = new Paths(0);
            tmpPS.addPath(tmpP);
            tmpPS.setStartNode(nodes[i1]);
            tmpPS.setEndNode(nodes[i1]);
            allPaths[i1][i1] = tmpPS;
        }
    }

    public int[][] pseudoMatMult(int[][] a1, int[][] a2) {
        int nr1 = a1.length;
        int nc1 = a1[0].length;
        int nr2 = a2.length;
        int nc2 = a2[0].length;
        if (nc1 != nr2) {
            System.out.println("matrix mis-match for multiplying");
            return null;
        }
        int[][] res = new int[nr1][nc2];
        for (int i1 = 0; i1 < nr1 - 1; i1++) {
            for (int i2 = i1 + 1; i2 < nc2; i2++) {
                if (a1[i1][i2] == 0) {
                    int sum = 0;
                    for (int i3 = 0; i3 < nc1; i3++) {
                        int tmpI = a1[i1][i3] * a2[i3][i2];
                        if (tmpI > 0) {
                            mergePaths(allPaths[i1][i2], allPaths[i1][i3], allPaths[i3][i2]);
                            mergePaths(allPaths[i2][i1], allPaths[i2][i3], allPaths[i3][i1]);
                        }
                        sum += tmpI;
                    }
                    res[i1][i2] = sum;
                    res[i2][i1] = sum;
                } else {
                    res[i1][i2] = a1[i1][i2];
                    res[i2][i1] = a1[i2][i1];
                }
            }
        }
        return res;
    }

    public void mergePaths(Paths p, Paths p2, Paths p3) {
//        ArrayList<Path> newPaths = new ArrayList();
        NodeUnit nStart = p2.startNode;
        NodeUnit nEnd = p3.targetNode;
        int n2 = p2.shortestPaths.size();
        int n3 = p3.shortestPaths.size();
//        int len2 = p2.currentShortestLength;
//        int len3 = p3.currentShortestLength;
//        int len = len2 + len3;
        for (int i2 = 0; i2 < n2; i2++) {
            for (int i3 = 0; i3 < n3; i3++) {
                Path tmpP = new Path(nStart, nEnd);
                tmpP.appendPath(p2.shortestPaths.get(i2).getPath());
                tmpP.appendPath(p3.shortestPaths.get(i3).getPath());
                p.addPath(tmpP);
            }
        }
    }

}
