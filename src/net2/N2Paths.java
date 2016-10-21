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
public class N2Paths implements Serializable{

    public int currentShortestLength = -1;
    public ArrayList<N2Path> shortestPaths = new ArrayList();
    public int startIndex = -1;
    public int targetIndex = -1;
    public N2Node targetNode = null;
    public N2Node startNode = null;

    public N2Paths(int numNodes) {
        //set path size to number of nodes, size of shortest path length will be at most n-1
        this.currentShortestLength = numNodes;
    }

    public int getShortestLength() {
        return this.currentShortestLength;
    }
    public void setStartNode(N2Node node){
        this.startNode = node;
        this.startIndex=node.nodeIndex;
    }
    public void setEndNode(N2Node node){
        this.targetNode = node;
        this.targetIndex=node.nodeIndex;
    }
    public void addPath(N2Path p) {
        int newNumSteps = p.getNumSteps();
        if (newNumSteps < this.currentShortestLength|| this.currentShortestLength<0) {
            this.shortestPaths.clear();
            this.shortestPaths.add(p);
            this.currentShortestLength = newNumSteps;
        } else if (newNumSteps == this.currentShortestLength) {
            this.shortestPaths.add(p);
        }
    }

    public int getNumPaths() {
        return this.shortestPaths.size();
    }

    public int getNumContaining(N2Node node) {
        int num = 0;
        for (N2Path p : shortestPaths) {
//            if (p.contains(node)&& !node.equals(p.lastNode)) {
            if (p.contains(node)) {
                num++;
            }
        }
        return num;
    }

    public double getFractionContaining(N2Node node) {
        double total = (double) getNumPaths();
        double containing = (double) getNumContaining(node);
        double res = 0.;
        if (total > 0.) {
            res = containing / total;
        }
        return res;
    }
    public boolean areSame(N2Path p1,N2Path p2){
        int len = p1.getNumSteps();
        if(p2.getNumSteps()!= len)return false;
        N2Node n1=null;
        N2Node n2=null;
//        boolean trueSoFar = true;
        for(int i1=0;i1<len;i1++){
            n1=p1.getPath().get(i1);
            n2=p2.getPath().get(i1);
            if(!n1.equals(n2))return false;
        }
        return true;
        
    }
}
