/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

//import heider8.HNode;
//import network.*;
import java.io.Serializable;
import java.util.ArrayList;

/**
 *
 * @author Jeff
 */
public class N2Path implements Serializable{

    public int startIndex = -1;
    public int targetIndex = -1;
    public int lastIndex = -1;
    public int numSteps = -1;
    public N2Node lastNode = null;
    public N2Node targetNode = null;
    public N2Node startNode = null;
    public ArrayList<N2Node> path = new ArrayList();
    public double transitCost = 0.;

    public N2Path(N2Node start, N2Node end) {
        this.startNode = start;
        this.startIndex = this.startNode.nodeIndex;
        this.targetNode = end;
        this.targetIndex = this.targetNode.nodeIndex;
//        LinkInfo info = startNode.
    }

    public void addNode(N2Node node) {
        path.add(node);
        this.numSteps = path.size();
        this.lastNode = node;
        this.lastIndex = node.nodeIndex;
    }

    public int getNumSteps() {
        this.numSteps = this.path.size();
        return this.numSteps;
    }
    public double getTotalLength(){
        N2Node n1=this.startNode;
        N2Node n2=null;
        double len = 0.;
        for(int i1=0;i1<this.path.size();i1++){
            n2=this.path.get(i1);
            len+=n2.getLocation().distance(n1.getLocation());
            n1=n2;
        }
        return len;
    }

    public N2Node getLastNode() {
        return this.lastNode;
    }

    public void clearPath() {
        this.path.clear();
        this.numSteps = -1;
    }

    public ArrayList<N2Node> getPath() {
        return this.path;
    }

    public void appendPath(ArrayList<N2Node> endPath) {
        this.path.addAll(endPath);
        int epSize = endPath.size();
        this.lastNode = endPath.get(epSize - 1);
        this.numSteps = this.path.size();
    }

    public boolean contains(N2Node node) {
        if (path.contains(node)) {
            return true;
        } else {
            return false;
        }
    }
    public String toString(){
        String tmpS=startNode.nodeIndex+",";
        for(N2Node node:path){
            tmpS+=node.nodeIndex+",";
        }
        return tmpS;
    }
}
