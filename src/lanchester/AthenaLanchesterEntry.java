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
public class AthenaLanchesterEntry {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Truncator truncator = new Truncator();
        System.out.println(truncator.truncate(Math.PI));
        System.out.println(truncator.truncate(Math.PI,0.01));
        System.out.println(truncator.truncate(Math.PI,0.001));
        AthenaArena arena = null;
        AthenaArena arena2 = null;
        if (args == null
                || args.length == 0) {
            AthenaForce blue = new AthenaForce("Blue");
            blue.setForceSize(10000);
            blue.setStance(AthenaConstants.ATTACK_POSTURE);
            blue.setReplenishment(0);
            blue.setAttackDefendRatio(.75);
            blue.setDefendWithdrawRatio(.45);
            AthenaForce red = new AthenaForce("Red");
            red.setForceSize(10000);
            red.setStance(AthenaConstants.ATTACK_POSTURE);
            red.setReplenishment(0);
            red.setAttackDefendRatio(.75);
            red.setDefendWithdrawRatio(.45);
            red.modifier.multMap.put(AthenaConstants.TRAINING, 0.5);
            arena = new AthenaArena(red, blue);
            for (int i1 = 0; i1 < 10; i1++) {
//                System.out.println(" ************ step " + i1 + " ***************");
//                System.out.println(red.toString());
//                System.out.println(blue.toString());
                arena.step();
                double ar = AthenaConstants.battle[red.getCurrentStance()][blue.getCurrentStance()];
                double ab = AthenaConstants.battle[blue.getCurrentStance()][red.getCurrentStance()];
//                System.out.println("ratio of forces = " + red.getForceSize() / blue.getForceSize() + " - ratio of root attrition = " + Math.sqrt(ar / ab));
            }
//            System.out.println(red.toString());
//            System.out.println(blue.toString());
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
        ArrayList<String> outList = arena.getHistoryCSV();
            AthenaArenaFloat farena=null;
//        for (int i1 = 0; i1 < outList.size(); i1++) {
//            System.out.println(outList.get(i1));
//        }
        System.out.println("\n\n ***** repeating  *****\n\n");
        {
            AthenaForce blue = new AthenaForce("Blue");
            blue.setForceSize(10000);
            blue.setStance(AthenaConstants.ATTACK_POSTURE);
            blue.setReplenishment(0);
            blue.setAttackDefendRatio(.75);
            blue.setDefendWithdrawRatio(.45);
            AthenaForce red = new AthenaForce("Red");
            red.setForceSize(10000);
            red.setStance(AthenaConstants.ATTACK_POSTURE);
            red.setReplenishment(0);
            red.setAttackDefendRatio(.75);
            red.setDefendWithdrawRatio(.45);
            red.modifier.multMap.put(AthenaConstants.TRAINING, 0.5);
            arena2 = new AthenaArena(blue, red);
            farena=new AthenaArenaFloat(blue,red);
            for (int i1 = 0; i1 < 10; i1++) {
//                System.out.println(" ************ step " + i1 + " ***************");
//                System.out.println(red.toString());
//                System.out.println(blue.toString());
                arena2.step();
//                farena.step();
//                double ar = AthenaConstants.battle[red.getCurrentStance()][blue.getCurrentStance()];
//                double ab = AthenaConstants.battle[blue.getCurrentStance()][red.getCurrentStance()];
//                System.out.println("ratio of forces = " + red.getForceSize() / blue.getForceSize() + " - ratio of root attrition = " + Math.sqrt(ar / ab));
            }
//            System.out.println(red.toString());
//            System.out.println(blue.toString());
        }
        outList = arena2.getHistoryCSV();
        for (int i1 = 0; i1 < outList.size(); i1++) {
            System.out.println(outList.get(i1));
        }
        System.out.println("\n *****\n float version\n *****\n");
//        System.out.println("\n ***** repeating  *****\n");
        {
            AthenaForce blue = new AthenaForce("Blue");
            blue.setForceSize(10000);
            blue.setStance(AthenaConstants.ATTACK_POSTURE);
            blue.setReplenishment(0);
            blue.setAttackDefendRatio(.75);
            blue.setDefendWithdrawRatio(.45);
            AthenaForce red = new AthenaForce("Red");
            red.setForceSize(10000);
            red.setStance(AthenaConstants.ATTACK_POSTURE);
            red.setReplenishment(0);
            red.setAttackDefendRatio(.75);
            red.setDefendWithdrawRatio(.45);
            red.modifier.multMap.put(AthenaConstants.TRAINING, 0.5);
//            arena2 = new AthenaArena(blue, red);
            farena=new AthenaArenaFloat(blue,red);
            for (int i1 = 0; i1 < 10; i1++) {
//                System.out.println(" ************ step " + i1 + " ***************");
//                System.out.println(red.toString());
//                System.out.println(blue.toString());
//                arena2.step();
                farena.step();
//                double ar = AthenaConstants.battle[red.getCurrentStance()][blue.getCurrentStance()];
//                double ab = AthenaConstants.battle[blue.getCurrentStance()][red.getCurrentStance()];
//                System.out.println("ratio of forces = " + red.getForceSize() / blue.getForceSize() + " - ratio of root attrition = " + Math.sqrt(ar / ab));
            }
//            System.out.println(red.toString());
//            System.out.println(blue.toString());
        }
        ArrayList<String> foutList = farena.getHistoryCSV();
        for (int i1 = 0; i1 < foutList.size(); i1++) {
            System.out.println(foutList.get(i1));
        }
        System.out.println("\n\n\n *****\n end of float version 1\n *****\n\n\n");
        System.out.println("\n ***** repeating  *****\n");
        {
            AthenaForce blue = new AthenaForce("Blue");
            blue.setForceSize(10000);
            blue.setStance(AthenaConstants.ATTACK_POSTURE);
            blue.setReplenishment(0);
            blue.setAttackDefendRatio(.75);
            blue.setDefendWithdrawRatio(.45);
            AthenaForce red = new AthenaForce("Red");
            red.setForceSize(10000);
            red.setStance(AthenaConstants.ATTACK_POSTURE);
            red.setReplenishment(0);
            red.setAttackDefendRatio(.75);
            red.setDefendWithdrawRatio(.45);
            red.modifier.multMap.put(AthenaConstants.TRAINING, 0.5);
//            arena2 = new AthenaArena(blue, red);
            farena=new AthenaArenaFloat(red,blue);
            for (int i1 = 0; i1 < 10; i1++) {
//                System.out.println(" ************ step " + i1 + " ***************");
//                System.out.println(red.toString());
//                System.out.println(blue.toString());
//                arena2.step();
                farena.step();
//                double ar = AthenaConstants.battle[red.getCurrentStance()][blue.getCurrentStance()];
//                double ab = AthenaConstants.battle[blue.getCurrentStance()][red.getCurrentStance()];
//                System.out.println("ratio of forces = " + red.getForceSize() / blue.getForceSize() + " - ratio of root attrition = " + Math.sqrt(ar / ab));
            }
//            System.out.println(red.toString());
//            System.out.println(blue.toString());
        }
        foutList = farena.getHistoryCSV();
        for (int i1 = 0; i1 < foutList.size(); i1++) {
            System.out.println(foutList.get(i1));
        }
        System.out.println("\n\n\n *****\n end of float version\n *****\n\n\n");
        double difft;
        double diff1;
            double diff2;
        for(int i1=0;i1<arena.history.size();i1++){
            DTimeStep dts1 = arena.dHistory.get(i1);
            DTimeStep dts2 = arena2.dHistory.get(i1);
            difft=dts1.getValues()[0]-dts2.getValues()[0];
            diff1=dts1.getValues()[1]-dts2.getValues()[2];
            diff2=dts1.getValues()[2]-dts2.getValues()[1];
            System.out.println("Del-t = "+difft+" del-red = "+diff1+" del-blue = "+diff2);
        }
    }

}
