/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchester;

import java.util.ArrayList;

/**
 *
 * @author Jeff
 */
public class MultiArenaEntry {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        MultiArena4 arena3 = null;
        SimpleMultiArena arena3 = null;
        MultiArena4 arena4 = null;
        SimpleMultiArena arena5 = null;
        if (args == null
                || args.length == 0) {
            MultiForce blue2 = new MultiForce("Blue 2", 10000.);
            blue2.setNumber(10000);
            blue2.setReplenishment(0);
            blue2.modifier.multMap.put(AthenaConstants.EQUIPMENT, 0.5);
            MultiForce red2 = new MultiForce("Red 2", 9000.);
            red2.setReplenishment(0);
            red2.modifier.multMap.put(AthenaConstants.TRAINING, 1.5);
            red2.modifier.multMap.put(AthenaConstants.EQUIPMENT, 1.5);
            MultiForce green = new MultiForce("Green 2", 15000.);
            green.setReplenishment(0);
            green.modifier.multMap.put(AthenaConstants.TRAINING, 0.4);
            MultiForce orange = new MultiForce("Orange", 12000.);
            orange.setReplenishment(0);
            orange.modifier.multMap.put(AthenaConstants.TRAINING, 1.5);
            orange.modifier.multMap.put(AthenaConstants.INTEL, 1.5);
            orange.modifier.multMap.put(AthenaConstants.EQUIPMENT, 1.5);
            red2.setADFloorRatio(blue2, 1.2);
            red2.setADFloorRatio(green, 1.1);
            red2.setADFloorRatio(orange, 1.1);
            blue2.setADFloorRatio(red2, 1.5);
            blue2.setADFloorRatio(green, 1.3);
            blue2.setADFloorRatio(orange, 1.3);
            green.setADFloorRatio(blue2, .8);
            green.setADFloorRatio(red2, .7);
            orange.setADFloorRatio(green, .9);
            orange.setADFloorRatio(blue2, .9);
            orange.setADFloorRatio(red2, .9);
            green.setADFloorRatio(orange, .7);
            red2.setADCeilingRatio(blue2, 1.6);
            red2.setADCeilingRatio(green, 1.4);
            red2.setADCeilingRatio(orange, 1.4);
            blue2.setADCeilingRatio(red2, 1.8);
            blue2.setADCeilingRatio(green, 1.6);
            blue2.setADCeilingRatio(orange, 1.6);
            green.setADCeilingRatio(blue2, 1.1);
            green.setADCeilingRatio(red2, 1.1);
            green.setADCeilingRatio(orange, 1.1);
            orange.setADCeilingRatio(blue2, 1.6);
            orange.setADCeilingRatio(green, 1.4);
            orange.setADCeilingRatio(red2, 1.4);
            red2.setDWFloorRatio(blue2, .6);
            red2.setDWFloorRatio(green, .5);
            red2.setDWFloorRatio(orange, .5);
            blue2.setDWFloorRatio(red2, .8);
            blue2.setDWFloorRatio(green, .7);
            blue2.setDWFloorRatio(orange, .7);
            green.setDWFloorRatio(blue2, .4);
            green.setDWFloorRatio(red2, .3);
            green.setDWFloorRatio(orange, .3);
            orange.setDWFloorRatio(red2, .8);
            orange.setDWFloorRatio(green, .7);
            orange.setDWFloorRatio(blue2, .7);
            red2.setDWCeilingRatio(blue2, .8);
            red2.setDWCeilingRatio(green, .7);
            red2.setDWCeilingRatio(orange, .7);
            blue2.setDWCeilingRatio(red2, 1.1);
            blue2.setDWCeilingRatio(green, .8);
            blue2.setDWCeilingRatio(orange, .8);
            green.setDWCeilingRatio(blue2, .6);
            green.setDWCeilingRatio(red2, .6);
            green.setDWCeilingRatio(orange, .6);
            orange.setDWCeilingRatio(blue2, .6);
            orange.setDWCeilingRatio(red2, .6);
            orange.setDWCeilingRatio(green, .6);
//            arena3 = new MultiArena4();
            arena3 = new SimpleMultiArena();
            arena3.setTimeStep(0.01);
            arena3.addForce(green);
            arena3.addForce(red2);
            arena3.addForce(blue2);
            arena3.addForce(orange);
//            arena4 = new SimpleMultiArena();
//            arena4.setTimeStep(0.01);
//            arena4.addForce(red2);
//            arena4.addForce(green);
//            arena4.addForce(blue2);
//            arena5 = new SimpleMultiArena();
//            arena5.setTimeStep(0.01);
//            arena5.addForce(green);
//            arena5.addForce(blue2);
//            arena5.addForce(red2);
            for (int i1 = 0; i1 < 2000; i1++) {
                if (!arena3.getLoneSurvivor()) {
                    arena3.step();
                }
//                if (!arena4.getLoneSurvivor()) {
//                    arena4.step();
//                }
//                if (!arena5.getLoneSurvivor()) {
//                    arena5.step();
//                }
            }
        } else if (args.length == 1) {
            String fName = args[0];
            if (fName.endsWith(".csv")) {

            } else {

            }
        } else if (args.length >= 2) {
            String fPath = args[0];
            String fName = args[1];
            if (fName.endsWith(".csv")) {

            } else {

            }

        }

        System.out.println("\n\n\n *****\n multiArena version\n *****\n\n\n");
        ArrayList<String> hist = arena3.getHistoryCSV();
//        ArrayList<String> hist4 = arena4.getHistoryCSV();
//        ArrayList<String> hist5 = arena5.getHistoryCSV();
        int n3 = hist.size();
        int histSize = n3;
//        int n4=hist4.size();
//        int n5=hist5.size();
//        int histSize = Math.max(n3,n4);
//        histSize = Math.max(histSize,n5);
        for (int i1 = 0; i1 < 100; i1++) {
            if (i1 < n3) {
                System.out.println("Arena 3 - " + hist.get(i1));
            }
//            if(i1<n4)System.out.println("Arena 4 - "+hist4.get(i1));
//            if(i1<n5)System.out.println("Arena 5 - "+hist5.get(i1));
        }
            MultiForce blue2 = new MultiForce("Blue 2", 10000.);
            blue2.setNumber(10000);
            blue2.setReplenishment(0);
            blue2.modifier.multMap.put(AthenaConstants.EQUIPMENT, 0.5);
            MultiForce red2 = new MultiForce("Red 2", 9000.);
            red2.setReplenishment(0);
            red2.modifier.multMap.put(AthenaConstants.TRAINING, 1.5);
            red2.modifier.multMap.put(AthenaConstants.EQUIPMENT, 1.5);
            MultiForce green = new MultiForce("Green 2", 15000.);
            green.setReplenishment(0);
            green.modifier.multMap.put(AthenaConstants.TRAINING, 0.4);
            MultiForce orange = new MultiForce("Orange", 12000.);
            orange.setReplenishment(0);
            orange.modifier.multMap.put(AthenaConstants.TRAINING, 1.5);
            orange.modifier.multMap.put(AthenaConstants.INTEL, 1.5);
            orange.modifier.multMap.put(AthenaConstants.EQUIPMENT, 1.5);
            red2.setADFloorRatio(blue2, 1.2);
            red2.setADFloorRatio(green, 1.1);
            red2.setADFloorRatio(orange, 1.1);
            blue2.setADFloorRatio(red2, 1.5);
            blue2.setADFloorRatio(green, 1.3);
            blue2.setADFloorRatio(orange, 1.3);
            green.setADFloorRatio(blue2, .8);
            green.setADFloorRatio(red2, .7);
            orange.setADFloorRatio(green, .9);
            orange.setADFloorRatio(blue2, .9);
            orange.setADFloorRatio(red2, .9);
            green.setADFloorRatio(orange, .7);
            red2.setADCeilingRatio(blue2, 1.6);
            red2.setADCeilingRatio(green, 1.4);
            red2.setADCeilingRatio(orange, 1.4);
            blue2.setADCeilingRatio(red2, 1.8);
            blue2.setADCeilingRatio(green, 1.6);
            blue2.setADCeilingRatio(orange, 1.6);
            green.setADCeilingRatio(blue2, 1.1);
            green.setADCeilingRatio(red2, 1.1);
            green.setADCeilingRatio(orange, 1.1);
            orange.setADCeilingRatio(blue2, 1.6);
            orange.setADCeilingRatio(green, 1.4);
            orange.setADCeilingRatio(red2, 1.4);
            red2.setDWFloorRatio(blue2, .6);
            red2.setDWFloorRatio(green, .5);
            red2.setDWFloorRatio(orange, .5);
            blue2.setDWFloorRatio(red2, .8);
            blue2.setDWFloorRatio(green, .7);
            blue2.setDWFloorRatio(orange, .7);
            green.setDWFloorRatio(blue2, .4);
            green.setDWFloorRatio(red2, .3);
            green.setDWFloorRatio(orange, .3);
            orange.setDWFloorRatio(red2, .8);
            orange.setDWFloorRatio(green, .7);
            orange.setDWFloorRatio(blue2, .7);
            red2.setDWCeilingRatio(blue2, .8);
            red2.setDWCeilingRatio(green, .7);
            red2.setDWCeilingRatio(orange, .7);
            blue2.setDWCeilingRatio(red2, 1.1);
            blue2.setDWCeilingRatio(green, .8);
            blue2.setDWCeilingRatio(orange, .8);
            green.setDWCeilingRatio(blue2, .6);
            green.setDWCeilingRatio(red2, .6);
            green.setDWCeilingRatio(orange, .6);
            orange.setDWCeilingRatio(blue2, .6);
            orange.setDWCeilingRatio(red2, .6);
            orange.setDWCeilingRatio(green, .6);
//            arena3 = new MultiArena4();
            arena4 = new MultiArena4();
            arena4.setTimeStep(0.01);
            arena4.addForce(green);
            arena4.addForce(red2);
            arena4.addForce(blue2);
            arena4.addForce(orange);
//            arena4 = new SimpleMultiArena();
//            arena4.setTimeStep(0.01);
//            arena4.addForce(red2);
//            arena4.addForce(green);
//            arena4.addForce(blue2);
//            arena5 = new SimpleMultiArena();
//            arena5.setTimeStep(0.01);
//            arena5.addForce(green);
//            arena5.addForce(blue2);
//            arena5.addForce(red2);
            for (int i1 = 0; i1 < 200; i1++) {
                if (!arena4.getLoneSurvivor()) {
                    arena4.step();
                }
//                if (!arena4.getLoneSurvivor()) {
//                    arena4.step();
//                }
//                if (!arena5.getLoneSurvivor()) {
//                    arena5.step();
//                }
            }
                ArrayList<String> hist4 = arena4.getHistoryCSV();
                histSize=hist4.size();
        for (int i1 = 0; i1 < 100; i1++) {
//            if (i1 < n3) {
                System.out.println("Arena 4 - " + hist4.get(i1));
//            }
//            if(i1<n4)System.out.println("Arena 4 - "+hist4.get(i1));
//            if(i1<n5)System.out.println("Arena 5 - "+hist5.get(i1));
        }

    }

}
