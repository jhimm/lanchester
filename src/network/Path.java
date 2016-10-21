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
public class Path implements Serializable{

    public int startIndex = -1;
    public int targetIndex = -1;
    public int lastIndex = -1;
    public int numSteps = -1;
    public NodeUnit lastNode = null;
    public NodeUnit targetNode = null;
    public NodeUnit startNode = null;
    public ArrayList<NodeUnit> path = new ArrayList();

    public Path(NodeUnit start, NodeUnit end) {
        this.startNode = start;
        this.startIndex = this.startNode.nodeIndex;
        this.targetNode = end;
        this.targetIndex = this.targetNode.nodeIndex;
    }

    public void addNode(NodeUnit node) {
        path.add(node);
        this.numSteps = path.size();
        this.lastNode = node;
        this.lastIndex = node.nodeIndex;
    }

    public int getNumSteps() {
        this.numSteps = this.path.size();
        return this.numSteps;
    }

    public NodeUnit getLastNode() {
        return this.lastNode;
    }

    public void clearPath() {
        this.path.clear();
        this.numSteps = -1;
    }

    public ArrayList<NodeUnit> getPath() {
        return this.path;
    }

    public void appendPath(ArrayList<NodeUnit> endPath) {
        this.path.addAll(endPath);
        int epSize = endPath.size();
        this.lastNode = endPath.get(epSize - 1);
        this.numSteps = this.path.size();
    }

    public boolean contains(NodeUnit node) {
        if (path.contains(node)) {
            return true;
        } else {
            return false;
        }
    }
    public String toString(){
        String tmpS=startNode.nodeIndex+",";
        for(NodeUnit node:path){
            tmpS+=node.nodeIndex+",";
        }
        return tmpS;
    }
}
