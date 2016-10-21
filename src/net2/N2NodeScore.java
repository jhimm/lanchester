/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

/**
 *
 * @author Jeff
 */
public class N2NodeScore {
    public N2Node node=null;
    public double score=-1.;
    public N2NodeScore(N2Node n,int sc){
        this.node=n;
        this.score=sc;
    }
    public double getScore(){
        return this.score;
    }
    public void setScore(double sc){
        this.score=sc;
    }public N2Node getNode(){
        return this.node;
    }
    public void setNode(N2Node n){
        this.node=n;
    }
}
