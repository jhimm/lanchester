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
 * @author Jeff
 */
public class Paths implements Serializable{

    public int currentShortestLength = -1;
    public ArrayList<Path> shortestPaths = new ArrayList();
    public int startIndex = -1;
    public int targetIndex = -1;
//    public int lastIndex = -1;
//    public int numSteps = -1;
//    public HNode lastNode = null;
    public NodeUnit targetNode = null;
    public NodeUnit startNode = null;

    public Paths(int numNodes) {
        //set path size to number of nodes, size of shortest path length will be at most n-1
        this.currentShortestLength = numNodes;
    }

    public int getShortestLength() {
        return this.currentShortestLength;
    }
    public void setStartNode(NodeUnit node){
        this.startNode = node;
        this.startIndex=node.nodeIndex;
    }
    public void setEndNode(NodeUnit node){
        this.targetNode = node;
        this.targetIndex=node.nodeIndex;
    }
    public void addPath(Path p) {
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

    public int getNumContaining(NodeUnit node) {
        int num = 0;
        for (Path p : shortestPaths) {
//            if (p.contains(node)&& !node.equals(p.lastNode)) {
            if (p.contains(node)) {
                num++;
            }
        }
        return num;
    }

    public double getFractionContaining(NodeUnit node) {
        double total = (double) getNumPaths();
        double containing = (double) getNumContaining(node);
        double res = 0.;
        if (total > 0.) {
            res = containing / total;
        }
        return res;
    }
    public boolean areSame(Path p1,Path p2){
        int len = p1.getNumSteps();
        if(p2.getNumSteps()!= len)return false;
        NodeUnit n1=null;
        NodeUnit n2=null;
//        boolean trueSoFar = true;
        for(int i1=0;i1<len;i1++){
            n1=p1.getPath().get(i1);
            n2=p2.getPath().get(i1);
            if(!n1.equals(n2))return false;
        }
        return true;
        
    }
}
