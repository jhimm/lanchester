/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Tests;
import net2.*;
import java.util.ArrayList;

/**
 *
 * @author Jeff
 */
public class TestN2MultiArena {
    

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
//        MultiArena4 arena3 = null;
//        SimpleMultiArena arena3 = null;
        N2MultiArena arena4 = null;

            N2ForceUnit blue2 = new N2ForceUnit("Blue 2", 10000.);
            blue2.setNumber(10000);
            blue2.setReplenishment(0);
            blue2.modifier.offenseMap.put(N2Constants.EQUIPMENT, 0.5);
            N2ForceUnit red2 = new N2ForceUnit("Red 2", 9000.);
            red2.setReplenishment(0);
            red2.modifier.offenseMap.put(N2Constants.TRAINING, 1.5);
            red2.modifier.offenseMap.put(N2Constants.EQUIPMENT, 1.5);
            N2ForceUnit green = new N2ForceUnit("Green 2", 15000.);
            green.setReplenishment(0);
            green.modifier.offenseMap.put(N2Constants.TRAINING, 0.4);
            N2ForceUnit orange = new N2ForceUnit("Orange", 12000.);
            orange.setReplenishment(0);
            orange.modifier.offenseMap.put(N2Constants.TRAINING, 1.5);
            orange.modifier.offenseMap.put(N2Constants.INTEL, 1.5);
            orange.modifier.offenseMap.put(N2Constants.EQUIPMENT, 1.5);
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
            arena4 = new N2MultiArena();
            arena4.setTimeStep(0.01);
            arena4.addForce(green);
            arena4.addForce(red2);
            arena4.addForce(blue2);
            arena4.addForce(orange);
            for (int i1 = 0; i1 < 200; i1++) {
                if (!arena4.getLoneSurvivor()) {
                    arena4.step();
                }
            }
                ArrayList<String> hist4 = arena4.getHistoryCSV();
                int histSize=hist4.size();
        for (int i1 = 0; i1 < 100; i1++) {
//            if (i1 < n3) {
                System.out.println("Arena 4 - " + hist4.get(i1));
//            }
//            if(i1<n4)System.out.println("Arena 4 - "+hist4.get(i1));
//            if(i1<n5)System.out.println("Arena 5 - "+hist5.get(i1));
        }

    }


}
