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
        System.out.println(truncator.truncate(Math.PI, 0.01));
        System.out.println(truncator.truncate(Math.PI, 0.001));
        AthenaArena arena = null;
        AthenaArena arena2 = null;
        MultiArena arena3 = null;
        if (args == null
                || args.length == 0) {
//            AthenaForce blue = new AthenaForce("Blue");
//            blue.setForceSize(10000);
//            blue.setStance(AthenaConstants.ATTACK_POSTURE);
//            blue.setReplenishment(0);
//            blue.setAttackDefendRatio(.75);
//            blue.setDefendWithdrawRatio(.45);
//            AthenaForce red = new AthenaForce("Red");
//            red.setForceSize(10000);
//            red.setStance(AthenaConstants.ATTACK_POSTURE);
//            red.setReplenishment(0);
//            red.setAttackDefendRatio(.75);
//            red.setDefendWithdrawRatio(.45);
//            red.modifier.multMap.put(AthenaConstants.TRAINING, 0.5);
//            arena = new AthenaArena(red, blue);
//            for (int i1 = 0; i1 < 10; i1++) {
////                System.out.println(" ************ step " + i1 + " ***************");
////                System.out.println(red.toString());
////                System.out.println(blue.toString());
//                arena.step();
//                double ar = AthenaConstants.battle[red.getCurrentStance()][blue.getCurrentStance()];
//                double ab = AthenaConstants.battle[blue.getCurrentStance()][red.getCurrentStance()];
////                System.out.println("ratio of forces = " + red.getForceSize() / blue.getForceSize() + " - ratio of root attrition = " + Math.sqrt(ar / ab));
//            }
            AthenaForce blue2 = new AthenaForce("Blue 2");
            blue2.setForceSize(10000);
            blue2.setStance(AthenaConstants.ATTACK_POSTURE);
            blue2.setReplenishment(0);
            blue2.setAttackDefendRatio(.75);
            blue2.setDefendWithdrawRatio(.45);
            AthenaForce red2 = new AthenaForce("Red 2");
            red2.setForceSize(10000);
            red2.setStance(AthenaConstants.ATTACK_POSTURE);
            red2.setReplenishment(0);
            red2.setAttackDefendRatio(.75);
            red2.setDefendWithdrawRatio(.45);
            red2.modifier.multMap.put(AthenaConstants.TRAINING, 0.5);
            arena3 = new MultiArena();
            arena3.addForce(blue2);
            arena3.addForce(red2);
            for (int i1 = 0; i1 < 200000; i1++) {
//                System.out.println(" ************ step " + i1 + " ***************");
//                System.out.println(red.toString());
//                System.out.println(blue.toString());
                arena3.step();
                double ar = AthenaConstants.battle[red2.getCurrentStance()][blue2.getCurrentStance()];
                double ab = AthenaConstants.battle[blue2.getCurrentStance()][red2.getCurrentStance()];
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
//        ArrayList<String> outList = arena.getHistoryCSV();
        AthenaArenaFloat farena = null;
//        for (int i1 = 0; i1 < outList.size(); i1++) {
//            System.out.println(outList.get(i1));
//        }
        System.out.println("\n\n ***** repeating  *****\n\n");
        {
//            AthenaForce blue = new AthenaForce("Blue");
//            blue.setForceSize(10000);
//            blue.setStance(AthenaConstants.ATTACK_POSTURE);
//            blue.setReplenishment(0);
//            blue.setAttackDefendRatio(.75);
//            blue.setDefendWithdrawRatio(.45);
//            AthenaForce red = new AthenaForce("Red");
//            red.setForceSize(10000);
//            red.setStance(AthenaConstants.ATTACK_POSTURE);
//            red.setReplenishment(0);
//            red.setAttackDefendRatio(.75);
//            red.setDefendWithdrawRatio(.45);
//            red.modifier.multMap.put(AthenaConstants.TRAINING, 0.5);
////            arena2 = new AthenaArena(blue, red);
////            farena=new AthenaArenaFloat(blue,red);
//            for (int i1 = 0; i1 < 10; i1++) {
////                System.out.println(" ************ step " + i1 + " ***************");
////                System.out.println(red.toString());
////                System.out.println(blue.toString());
////                arena2.step();
////                farena.step();
////                double ar = AthenaConstants.battle[red.getCurrentStance()][blue.getCurrentStance()];
////                double ab = AthenaConstants.battle[blue.getCurrentStance()][red.getCurrentStance()];
////                System.out.println("ratio of forces = " + red.getForceSize() / blue.getForceSize() + " - ratio of root attrition = " + Math.sqrt(ar / ab));
//            }
////            System.out.println(red.toString());
////            System.out.println(blue.toString());
        }
//        outList = arena2.getHistoryCSV();
//        for (int i1 = 0; i1 < outList.size(); i1++) {
//            System.out.println(outList.get(i1));
//        }
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
            farena = new AthenaArenaFloat(blue, red);
            for (int i1 = 0; i1 < 10; i1++) {
                farena.step();
            }
        }
        ArrayList<String> foutList = farena.getHistoryCSV();
        for (int i1 = 0; i1 < foutList.size(); i1++) {
            System.out.println(foutList.get(i1));
        }
        System.out.println("\n\n\n *****\n end of float version 1\n *****\n\n\n");
//        System.out.println("\n ***** repeating  *****\n");
//        {
//            AthenaForce blue = new AthenaForce("Blue");
//            blue.setForceSize(10000);
//            blue.setStance(AthenaConstants.ATTACK_POSTURE);
//            blue.setReplenishment(0);
//            blue.setAttackDefendRatio(.75);
//            blue.setDefendWithdrawRatio(.45);
//            AthenaForce red = new AthenaForce("Red");
//            red.setForceSize(10000);
//            red.setStance(AthenaConstants.ATTACK_POSTURE);
//            red.setReplenishment(0);
//            red.setAttackDefendRatio(.75);
//            red.setDefendWithdrawRatio(.45);
//            red.modifier.multMap.put(AthenaConstants.TRAINING, 0.5);
////            arena2 = new AthenaArena(blue, red);
//            farena=new AthenaArenaFloat(red,blue);
//            for (int i1 = 0; i1 < 10; i1++) {
////                System.out.println(" ************ step " + i1 + " ***************");
////                System.out.println(red.toString());
////                System.out.println(blue.toString());
////                arena2.step();
//                farena.step();
////                double ar = AthenaConstants.battle[red.getCurrentStance()][blue.getCurrentStance()];
////                double ab = AthenaConstants.battle[blue.getCurrentStance()][red.getCurrentStance()];
////                System.out.println("ratio of forces = " + red.getForceSize() / blue.getForceSize() + " - ratio of root attrition = " + Math.sqrt(ar / ab));
//            }
////            System.out.println(red.toString());
////            System.out.println(blue.toString());
//        }
//        foutList = farena.getHistoryCSV();
//        for (int i1 = 0; i1 < foutList.size(); i1++) {
//            System.out.println(foutList.get(i1));
//        }
//        System.out.println("\n\n\n *****\n end of float version\n *****\n\n\n");
//        double difft;
//        double diff1;
//            double diff2;
//        for(int i1=0;i1<arena.history.size();i1++){
//            DTimeStep dts1 = arena.dHistory.get(i1);
//            DTimeStep dts2 = arena2.dHistory.get(i1);
//            difft=dts1.getValues()[0]-dts2.getValues()[0];
//            diff1=dts1.getValues()[1]-dts2.getValues()[2];
//            diff2=dts1.getValues()[2]-dts2.getValues()[1];
//            System.out.println("Del-t = "+difft+" del-red = "+diff1+" del-blue = "+diff2);
//        }
        System.out.println("\n\n\n *****\n multiArena version\n *****\n\n\n");
        ArrayList<String> hist = arena3.getHistoryCSV();
        for (int i1 = 0; i1 < hist.size(); i1++) {
            System.out.println(hist.get(i1));
        };
        System.out.println("\n\n\n *****\n FloatArena version\n *****\n\n\n");
        ArrayList<MultiTimeStep> mTList = arena3.getHistory();
        int numFVals = farena.getDHistory().size();
        for (int i1 = 0; i1 < numFVals - 1; i1++) {
            double[] vals = farena.getDHistory().get(i1).getValues();
            double[] vals1 = farena.getDHistory().get(i1 + 1).getValues();
            for (int i2 = 0; i2 < mTList.size() - 1; i2++) {
                double[] mvals0 = mTList.get(i2).getValues();
                double[] mvals1 = mTList.get(i2 + 1).getValues();
                if (mvals0[0] < vals[0] && mvals1[0] > vals[0]) {
                    String tmpS = "" + vals[0];
                    for (int i3 = 1; i3 < vals.length; i3++) {
                        tmpS += "," + vals[i3];
                    }
                    System.out.println(tmpS);
                    tmpS = "MultiArena values " + mvals0[0];
                    for (int i3 = 1; i3 < mvals0.length; i3++) {
                        tmpS += "," + mvals0[i3];
                    }
                    System.out.println(tmpS);
                }
            }
//            String tmpS = "" + vals[0];
//            for (int i2 = 1; i2 < vals.length; i2++) {
//                tmpS += "," + vals[i2];
//            }
//            System.out.println(tmpS);
        }
    }

}
