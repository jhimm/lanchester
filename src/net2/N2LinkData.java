/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;
import sim.field.network.Edge;
/**
 *
 * @author Jeff
 */
public class N2LinkData {
    public int numCategories = 1;
    public int[] wts = new int[numCategories];
    public int wt = 0;
    public int fromIndex = -1;
    public int toIndex = -1;
    public double[] transitCosts={1.0,3.0,8.0};
    public N2LinkData(N2LinkInfo info){
        this.numCategories=info.numCategories;
        this.wts=info.getWeights();
        this.wt=info.wt;
        this.fromIndex=info.from.getNodeIndex();
        this.toIndex=info.to.getNodeIndex();
        this.transitCosts=info.transitCosts;
    }
    public N2LinkData(Edge tmpE){
        N2Node tmpN1 = (N2Node)tmpE.getFrom();
        N2Node tmpN2 = (N2Node)tmpE.getTo();
//        tmpE.
    }
}
