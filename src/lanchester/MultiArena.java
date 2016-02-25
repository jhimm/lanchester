/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchester;

import java.util.ArrayList;
import org.apache.commons.math3.linear.*;
//import org.apache.commons.math3.linear.;

/**
 *
 * @author Jeff
 */
public class MultiArena {

    public AthenaForce f1 = null;
    public AthenaForce f2 = null;
    public double currentTime = 0;
    public double maxTime = 10000.;
    public ArrayList<MultiTimeStep> history = new ArrayList();
    public ArrayList<DTimeStep> dHistory = new ArrayList();
    public ArrayList<AthenaForce> forces = new ArrayList();
    public boolean isBattle = true;

    public MultiArena() {
        AthenaConstants.fillArrays();
    }

    public void addForce(AthenaForce f) {
        forces.add(f);
    }

    public ArrayList<AthenaForce> getForces() {
        return this.forces;
    }

    public void addChange(MultiTimeStep timeStep) {
        history.add(timeStep);
    }

    public ArrayList<MultiTimeStep> getHistory() {
        return this.history;
    }

    public ArrayList<String> getHistoryCSV() {
        String tmpS = "Time Step," + f1.getName() + "'" + f2.getName();
        ArrayList<String> tmpList = new ArrayList();
        tmpList.add(tmpS);
        MultiTimeStep tmpH;
        for (int i1 = 0; i1 < history.size(); i1++) {
            tmpH = history.get(i1);
            tmpS = tmpH.getTimeStep();
            tmpList.add(tmpS);
        }
        return tmpList;
    }

    public void step() {
        int numFoes = forces.size();
        BlockRealMatrix mat = getMat();
//        fillMat(mat);
        EigenDecomposition eigen = new EigenDecomposition(mat);
        double det = eigen.getDeterminant();
        double[] eVals = eigen.getRealEigenvalues();
        if(eigen.hasComplexEigenvalues()){
            System.out.println("Complex eigenvalues");
        }
        double[] initialNums = getInitialNumbers(forces);
        BlockRealMatrix eVectors = (BlockRealMatrix)eigen.getV();
        EigenDecomposition eigen2 = new EigenDecomposition(eVectors);
        double det2 = eigen2.getDeterminant();
//        eVectors.
//        this.currentTime++;
//                Truncator truncator = new Truncator();
        if (true) {
//            System.out.println("time = " + time);
        }
//       System.out.println("time = " + time + "  C1 = " + c1 + "  C2 = " + c2);
//        int nextN1 = (int) (0.5 + nextD1);
//        if (nextN1 < 1) {
//            nextN1 = 0;
//        }
//        int nextN2 = (int) (0.5 + nextD2);
//        if (nextN2 < 1) {
//            nextN2 = 0;
//        }
//
//        this.currentTime += time;
//        nextD1 = truncator.truncate(nextD1, 1.e-15);
//        nextD2 = truncator.truncate(nextD2, 1.e-15);
//        TimeStep step = new TimeStep((int) (0.5 + this.currentTime), nextN1, nextN2);
//        DTimeStep dStep = new DTimeStep(this.currentTime, nextD1, nextD2);
//        history.add(step);
//        dHistory.add(dStep);
//        f1.previousForceSize = f1.getForceSize();
//        f1.setForceSize(nextD1);
//        f2.previousForceSize = f2.getForceSize();
//        f2.setForceSize(nextD2);
//        System.out.println("del 1 = "+(f1.previousForceSize-f1.forceSize)+"  del 2 = "+(f2.previousForceSize-f2.forceSize));
    }

    public double getX(double t, double c11, double c22, double rAA, double rBB) {
        double res = 1.;
        res *= c11 * rAA * Math.exp(t * rAA * rBB) + c22 * rAA * Math.exp(-1 * t * rAA * rBB);
        return res;
    }

    public double getY(double t, double c11, double c22, double rAA, double rBB) {
        double res = -1.;
        res *= c11 * rBB * Math.exp(t * rAA * rBB) - c22 * rBB * Math.exp(-1 * t * rAA * rBB);
        return res;
    }

    public BlockRealMatrix getMat() {
        int numFoes = forces.size();
        AthenaForce f1;
        AthenaForce f2;
        BlockRealMatrix mat = new BlockRealMatrix(numFoes, numFoes);
        for (int i1 = 0; i1 < numFoes - 1; i1++) {
            f1 = forces.get(i1);
            for (int i2 = i1 + 1; i2 < numFoes; i2++) {
                f2 = forces.get(i2);
                if (f1.isFriend(f2)) {
                    f1.setStance(AthenaConstants.ALLIED_POSTURE);
                    f2.setStance(AthenaConstants.ALLIED_POSTURE);
                    mat.setEntry(i1, i2, 0.);
                    mat.setEntry(i2, i1, 0.);
                } else {
                setStances(f1,f2);
                    double x0 = f1.getForceSize();
                    double y0 = f2.getForceSize();
                    double a1 = f2.getForceMultiplier();
                    double b2 = f1.getForceMultiplier();
                    if (isBattle) {
                        a1 *= AthenaConstants.battle[f1.getCurrentStance()][f2.getCurrentStance()];
                        b2 *= AthenaConstants.battle[f2.getCurrentStance()][f1.getCurrentStance()];
                    } else {
                        a1 *= AthenaConstants.skirmish[f1.getCurrentStance()][f2.getCurrentStance()];
                        b2 *= AthenaConstants.skirmish[f2.getCurrentStance()][f1.getCurrentStance()];
                    }

                    mat.setEntry(i1, i2, a1);
                    mat.setEntry(i2, i1, b2);
                }
            }
        }
        return mat;
    }
    public void setStances(AthenaForce f1,AthenaForce f2){
        double currentF1 = f1.getForceSize();
        double currentF2 = f2.getForceSize();
        double ratio1to2 = currentF1 / currentF2;
        if (ratio1to2 > f1.attackDefendRatio) {
            f1.setStance(AthenaConstants.ATTACK_POSTURE);
        } else if (ratio1to2 > f1.defendWithdrawRatio) {
            f1.setStance(AthenaConstants.DEFEND_POSTURE);
        } else {
            f1.setStance(AthenaConstants.WITHDRAW_POSTURE);
        }
        double ratio2to1 = currentF2 / currentF1;
        if (ratio2to1 > f2.attackDefendRatio) {
            f2.setStance(AthenaConstants.ATTACK_POSTURE);
        } else if (ratio2to1 > f2.defendWithdrawRatio) {
            f2.setStance(AthenaConstants.DEFEND_POSTURE);
        } else {
            f2.setStance(AthenaConstants.WITHDRAW_POSTURE);
        }
    }
    public double[] getInitialNumbers(ArrayList<AthenaForce> forces){
        double[] nums= new double[forces.size()];
        for(int i1=0;i1<forces.size();i1++){
            nums[i1]=forces.get(i1).getForceSize();
        }
        return nums;
    }
}
