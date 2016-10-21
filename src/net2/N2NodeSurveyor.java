/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

import java.util.ArrayList;
import java.util.HashMap;
import sim.engine.SimState;
import sim.util.Bag;
import sim.field.network.Edge;

/**
 *
 * @author Jeff
 */
public class N2NodeSurveyor {

    public ArrayList<N2Node> nodeList = new ArrayList();
    public ArrayList<N2Node> bgList = new ArrayList();
    public ArrayList<N2Node> allyList = new ArrayList();
    public ArrayList<N2Node> neutralList = new ArrayList();
    public ArrayList<N2Node> uncontrolledList = new ArrayList();
    public ArrayList<N2Node> bgBorderList = new ArrayList();
    public ArrayList<N2Node> allyBorderList = new ArrayList();
    public ArrayList<N2Node> neutralBorderList = new ArrayList();
    public ArrayList<N2Node> uncontrolledBorderList = new ArrayList();
    public ArrayList<N2Node> internalNeutralList = new ArrayList();
    public ArrayList<N2Node> internalUncontrolledList = new ArrayList();
    public N2Nodes nodes = null;

    public N2NodeSurveyor(SimState state) {
        this.nodes = (N2Nodes) state;
        Bag nBag = removeUnits(nodes.neighborhood.getAllNodes());
        for (Object tmpO : nBag) {
            nodeList.add((N2Node) tmpO);
        }

    }

    public void survey(HashMap<N2Node, N2Target> tMap) {
//        nodeList.clear();
        bgList.clear();
        allyList.clear();
        neutralList.clear();
        uncontrolledList.clear();
        bgBorderList.clear();
        allyBorderList.clear();
        neutralBorderList.clear();
        uncontrolledBorderList.clear();
        internalNeutralList.clear();
        internalUncontrolledList.clear();
        System.out.println("there are " + nodes.neighborhood.getAllNodes().size() + " objects in the network");
        Bag nBag = removeUnits(nodes.neighborhood.getAllNodes());
//        for(Object tmpO:nBag){
//            nodeList.add((N2Node)tmpO);
//        }
        System.out.println("there are " + nBag.size() + " objects in the network after removing force units");
        Bag possibleBag = removeCurrentTargets(nBag, tMap);
        System.out.println("there are " + possibleBag.size() + " objects in the network after removing targets");
        //        nBag = removeUnits(nBag);
//        Edge[][][] edges = nodes.mgedges;
        for (int i1 = 0; i1 < nBag.size(); i1++) {
            N2Node tmpN1 = (N2Node) nBag.get(i1);
            updateLists(tmpN1);
        }
        for (int i1 = 0; i1 < possibleBag.size(); i1++) {
            N2Node tmpN1 = (N2Node) possibleBag.get(i1);
            updateBorderLists(tmpN1);
        }
        screenLists(tMap);
    }

    public void screenLists(HashMap<N2Node, N2Target> tMap) {
        screenList(bgBorderList, tMap);
        screenList(allyBorderList, tMap);
        screenList(neutralBorderList, tMap);
        screenList(uncontrolledBorderList, tMap);
        screenList(internalUncontrolledList, tMap);
        screenList(internalNeutralList, tMap);
    }

    public void screenList(ArrayList<N2Node> nList, HashMap<N2Node, N2Target> tMap) {
        for (N2Node tmpN : tMap.keySet()) {
            if (nList.contains(tmpN)) {
                nList.remove(tmpN);
            }
        }
    }

    public Bag removeUnits(Bag nb) {
        Bag nBag = new Bag();
        for (Object tmpO : nb) {
            if (tmpO instanceof N2Node) {
                nBag.add(tmpO);
            }
        }
        return nBag;
    }

//    public Bag removeCurrentTargets(Bag nBag, ArrayList<N2Target> tList) {
//        boolean isTarget;
//        Bag tBag = new Bag();
//        Bag rBag = new Bag(nBag);
//        for (Object tmpO : nBag) {
//            N2Node tmpN = (N2Node) tmpO;
//            isTarget = false;
//            for (N2Target tmpT : tList) {
//                if (tmpT.isTarget(tmpN)) {
//                    isTarget = true;
//                }
//            }
//            if (isTarget) {
//                tBag.add(tmpO);
//            }
//        }
//        for (Object tmpT : tBag) {
//            rBag.remove(tmpT);
//            N2Node tmpT2 = (N2Node) tmpT;
//            System.out.println("removing node " + tmpT2.toString() + " from possible target list");
//        }
//
//        return rBag;
//    }
    public Bag removeCurrentTargets(Bag nBag, HashMap<N2Node, N2Target> tMap) {
        Bag tBag = new Bag();
        Bag rBag = new Bag(nBag);
        for (Object tmpO : nBag) {
            N2Node tmpN = (N2Node) tmpO;
            if (tMap.keySet().contains(tmpN)) {
                tBag.add(tmpO);
            }
        }
        for (Object tmpT : tBag) {
            rBag.remove(tmpT);
            N2Node tmpT2 = (N2Node) tmpT;
            System.out.println("removing node " + tmpT2.toString() + " from possible target list");
        }

        return rBag;
    }

    public void updateLists(N2Node node) {
        if (node.getControllingFaction() == N2Constants.BAD_GUYS) {
            if (!bgList.contains(node)) {
                bgList.add(node);
            }
        } else if (node.getControllingFaction() == N2Constants.GOOD_GUYS) {
            if (!allyList.contains(node)) {
                allyList.add(node);
            }
        } else if (node.getControllingFaction() == N2Constants.UNCONTROLLED) {
            if (!uncontrolledList.contains(node)) {
                uncontrolledList.add(node);
            }
        } else if (!neutralList.contains(node)) {
            neutralList.add(node);
        }

    }

    public void updateBorderLists(N2Node node) {
        if (node.getControllingFaction() == N2Constants.BAD_GUYS) {
            boolean ggNeighbor = false;
            for (Object tmpO : node.neighbors) {
                N2Node tmpN2 = (N2Node) tmpO;
                if (tmpN2.getControllingFaction() == N2Constants.GOOD_GUYS) {
                    ggNeighbor = true;
                }
            }
            if (ggNeighbor) {
                if (!bgBorderList.contains(node)) {
                    bgBorderList.add(node);
                }
            }
        }
        if (node.getControllingFaction() == N2Constants.GOOD_GUYS) {
            boolean bgNeighbor = false;
            for (Object tmpO : node.neighbors) {
                N2Node tmpN2 = (N2Node) tmpO;
                if (tmpN2.getControllingFaction() == N2Constants.BAD_GUYS) {
                    bgNeighbor = true;
                }
            }
            if (bgNeighbor) {
                if (!allyBorderList.contains(node)) {
                    allyBorderList.add(node);
                }
            }
        }
        for (N2Node tmpN : neutralList) {
            boolean ggNeighbor = false;
            boolean bgNeighbor = false;
            for (Object tmpO : tmpN.neighbors) {
                N2Node tmpN2 = (N2Node) tmpO;
                if (tmpN2.getControllingFaction() == N2Constants.BAD_GUYS) {
                    bgNeighbor = true;
                }
                if (tmpN2.getControllingFaction() == N2Constants.GOOD_GUYS) {
                    ggNeighbor = true;
                }
            }
            if (ggNeighbor && bgNeighbor) {
                if (!neutralBorderList.contains(tmpN)) {
                    neutralBorderList.add(tmpN);
                }
            } else if (ggNeighbor) {
                if (!internalNeutralList.contains(tmpN)) {
                    internalNeutralList.add(tmpN);
                }
            }
        }
        for (N2Node tmpN : uncontrolledList) {
            boolean ggNeighbor = false;
            boolean bgNeighbor = false;
            for (Object tmpO : tmpN.neighbors) {
                N2Node tmpN2 = (N2Node) tmpO;
                if (tmpN2.getControllingFaction() == N2Constants.BAD_GUYS) {
                    bgNeighbor = true;
                }
                if (tmpN2.getControllingFaction() == N2Constants.GOOD_GUYS) {
                    ggNeighbor = true;
                }
            }
            if (ggNeighbor && bgNeighbor) {
                if (!uncontrolledBorderList.contains(tmpN)) {
                    uncontrolledBorderList.add(tmpN);
                }
            } else if (ggNeighbor) {
                if (!internalUncontrolledList.contains(tmpN)) {
                    internalUncontrolledList.add(tmpN);
                }
            }
        }
    }
    

//    public void updateBorderLists() {
//        for (N2Node tmpN : allyList) {
//            boolean neutralNeighbor = false;
//            boolean bgNeighbor = false;
//            boolean uncontrolledNeighbor = false;
//            for (Object tmpO : tmpN.neighbors) {
//                N2Node tmpN2 = (N2Node) tmpO;
//                if (tmpN2.getControllingFaction() == N2Constants.BAD_GUYS) {
//                    bgNeighbor = true;
//                }
//                if (tmpN2.getControllingFaction() == N2Constants.NEUTRAL_GUYS) {
//                    neutralNeighbor = true;
//                }
//                if (tmpN2.getControllingFaction() == N2Constants.UNCONTROLLED) {
//                    uncontrolledNeighbor = true;
//                }
//            }
//            if (neutralNeighbor || bgNeighbor || uncontrolledNeighbor) {
//                allyBorderList.add(tmpN);
//            }
//        }
//        for (N2Node tmpN : bgList) {
//            boolean ggNeighbor = false;
//            boolean neutralNeighbor = false;
//            for (Object tmpO : tmpN.neighbors) {
//                N2Node tmpN2 = (N2Node) tmpO;
//                if (tmpN2.getControllingFaction() == N2Constants.NEUTRAL_GUYS) {
//                    neutralNeighbor = true;
//                }
//                if (tmpN2.getControllingFaction() == N2Constants.UNCONTROLLED) {
//                    neutralNeighbor = true;
//                }
//                if (tmpN2.getControllingFaction() == N2Constants.GOOD_GUYS) {
//                    ggNeighbor = true;
//                }
//            }
//            if (ggNeighbor || neutralNeighbor) {
//                bgBorderList.add(tmpN);
//            }
//        }
//        for (N2Node tmpN : neutralList) {
//            boolean ggNeighbor = false;
//            boolean bgNeighbor = false;
//            for (Object tmpO : tmpN.neighbors) {
//                N2Node tmpN2 = (N2Node) tmpO;
//                if (tmpN2.getControllingFaction() == N2Constants.BAD_GUYS) {
//                    bgNeighbor = true;
//                }
//                if (tmpN2.getControllingFaction() == N2Constants.GOOD_GUYS) {
//                    ggNeighbor = true;
//                }
//            }
//            if (ggNeighbor && bgNeighbor) {
//                neutralBorderList.add(tmpN);
//            } else if (ggNeighbor) {
//                internalNeutralList.add(tmpN);
//            }
//        }
//        for (N2Node tmpN : uncontrolledList) {
//            boolean ggNeighbor = false;
//            boolean bgNeighbor = false;
//            for (Object tmpO : tmpN.neighbors) {
//                N2Node tmpN2 = (N2Node) tmpO;
//                if (tmpN2.getControllingFaction() == N2Constants.BAD_GUYS) {
//                    bgNeighbor = true;
//                }
//                if (tmpN2.getControllingFaction() == N2Constants.GOOD_GUYS) {
//                    ggNeighbor = true;
//                }
//            }
//            if (ggNeighbor && bgNeighbor) {
//                uncontrolledBorderList.add(tmpN);
//            } else if (ggNeighbor) {
//                internalUncontrolledList.add(tmpN);
//            }
//
//        }
//    }

    public ArrayList<N2Node> getNodeList() {
        return this.nodeList;
    }

    public ArrayList<N2Node> getAllyList() {
        return this.allyList;
    }

    public ArrayList<N2Node> getBGList() {
        return this.bgList;
    }

    public ArrayList<N2Node> getNeutralList() {
        return this.neutralList;
    }

    public ArrayList<N2Node> getAllyBorderList() {
        return this.allyBorderList;
    }

    public ArrayList<N2Node> getNeutralBorderList() {
        return this.neutralBorderList;
    }

    public ArrayList<N2Node> getInternalNeutralList() {
        return this.internalNeutralList;
    }

    public ArrayList<N2Node> getUncontrolledBorderList() {
        return this.uncontrolledBorderList;
    }

    public ArrayList<N2Node> getInternalUncontrolledList() {
        return this.internalUncontrolledList;
    }

    public ArrayList<N2Node> getBGBorderList() {
        return this.bgBorderList;
    }

    public String getOutString() {
        String tmpS = "number of nodes: \n";
        tmpS += "\tGG " + this.allyList.size();
        tmpS += "\n\tBG " + this.bgList.size();
        tmpS += "\n\tneutrals " + this.neutralList.size();
        tmpS += "\n\tuncontrolled " + this.uncontrolledList.size();
        tmpS += "\n\tGG on border " + this.allyBorderList.size();
        tmpS += "\n\tneutrals on border " + this.neutralBorderList.size();
        tmpS += "\n\tuncontrolled on border " + this.uncontrolledBorderList.size();
        tmpS += "\n\tneutrals internal " + this.internalNeutralList.size();
        tmpS += "\n\tuncontrolled internal " + this.internalUncontrolledList.size();
        return tmpS;
    }

}
