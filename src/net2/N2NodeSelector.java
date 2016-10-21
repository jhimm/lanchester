/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

/**
 *
 * @author Jeff
 */
public class N2NodeSelector {

    public N2Node[] nodeArray = null;
    public N2NodeScore[] scoreArray = null;
    public ArrayList<N2Node> nList = new ArrayList();
    public int nextAvailableIndex = 0;
    public int numNodes = -1;
    public int rankingType = 0;
    public HashMap<N2Node, Double> valueMap = new HashMap();
    public int uncontrolledBorderScore = 10000;
    public int neutralBorderScore = 5000;
    public int uncontrolledInternalScore = 2000;
    public int neutralInternalScore = 1000;
    public int bgBorderScore = 500;

    public N2NodeSelector(ArrayList<N2Node> nodeList) {
        this.nList.clear();
        this.nList.addAll(nodeList);
        this.numNodes = this.nList.size();
        this.nodeArray = new N2Node[this.numNodes];
        this.scoreArray = new N2NodeScore[this.numNodes];
    }

    public void rankNodes(N2NodeSurveyor surveyor) {
        getNodeArray(surveyor);
//        for(int i1=0;i1<numNodes;i1++){
//            this.nodeArray[i1]=this.nList.get(i1);
//        }
    }

    public N2Node getNextInLine() {
        if (this.nextAvailableIndex < this.numNodes) {
            return scoreArray[this.nextAvailableIndex++].getNode();
        }
        return null;
    }

    public void setRankingType(int rt) {
        this.rankingType = rt;
    }

    public int getRankingType() {
        return this.rankingType;
    }

    public void getNodeArray(N2NodeSurveyor surveyor) {
        switch (rankingType) {
            case 0: {
                System.out.println(surveyor.getOutString());
                this.scoreArray = new N2NodeScore[this.numNodes];
                valueMap.clear();
                if (surveyor.getUncontrolledBorderList().size() > 0) {
                    System.out.println(" adding uncontrolled border nodes to value map");
                    for (N2Node tmpN : surveyor.getUncontrolledBorderList()) {
                        valueMap.put(tmpN, tmpN.getvalue() + this.uncontrolledBorderScore);
                    }
                }
                if (surveyor.getNeutralBorderList().size() > 0) {
                    System.out.println(" adding neutral border nodes to value map");
//            valueMap.clear();
                    for (N2Node tmpN : surveyor.getNeutralBorderList()) {
                        valueMap.put(tmpN, tmpN.getvalue() + this.neutralBorderScore);
                    }
                }
                if (surveyor.getInternalUncontrolledList().size() > 0) {
                    System.out.println(" adding internal uncontrolled nodes to value map");
//            valueMap.clear();
                    for (N2Node tmpN : surveyor.getInternalUncontrolledList()) {
                        valueMap.put(tmpN, tmpN.getvalue() + this.uncontrolledInternalScore);
                    }
                }
                if (surveyor.getInternalNeutralList().size() > 0) {
                    System.out.println(" adding internal neutral nodes to value map");
//            valueMap.clear();
                    for (N2Node tmpN : surveyor.getInternalNeutralList()) {
                        valueMap.put(tmpN, tmpN.getvalue() + this.neutralInternalScore);
                    }
                }
                if  (surveyor.getBGBorderList().size()>0){
                    System.out.println(" adding bg nodes to value map");
//            valueMap.clear();
                    for (N2Node tmpN : surveyor.getBGBorderList()) {
                        valueMap.put(tmpN, tmpN.getvalue() + this.bgBorderScore);
                    }
                }
                for(int i1=0;i1<numNodes;i1++){
                    N2Node tmpN = nList.get(i1);
                    N2NodeScore tmpScore=new N2NodeScore(tmpN,-1);
                    if(valueMap.keySet().contains(tmpN)){
                        tmpScore.setScore(valueMap.get(tmpN));
                    }
                    scoreArray[i1]=tmpScore;
                }
                Arrays.sort(scoreArray, new N2NodeScoreComparator());
                break;
            }
            case 1: {
                break;
            }
            case 2: {
                break;
            }
        }
    }
}
