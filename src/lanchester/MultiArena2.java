/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchester;

import java.util.ArrayList;
import java.util.HashMap;
import org.apache.commons.math3.linear.*;
//import org.apache.commons.math3.linear.;

/**
 *
 * @author Jeff
 */
public class MultiArena2 {

    public AthenaForce f1 = null;
    public AthenaForce f2 = null;
    public double currentTime = 0.;
    public double maxTime = 10000.;
    public ArrayList<MultiTimeStep> history = new ArrayList();
    public ArrayList<DTimeStep> dHistory = new ArrayList();
    public ArrayList<AthenaForce> forces = new ArrayList();
    public boolean isBattle = true;
    public double timeStep = 0.1;
    public HashMap<AthenaForce, Integer> forceMap = new HashMap();
    public boolean[][] isMyTurn = null;
    public int[][] stanceArray = null;
    public double[][] currentFloor = null;
    public double lb = 1.e-2;
    public boolean loneSurvivor = false;

    public MultiArena2() {
        AthenaConstants.fillArrays();
    }

    public void addForce(AthenaForce f) {
        int num = forces.size();
        forces.add(f);
        forceMap.put(f, num);
    }

    public ArrayList<AthenaForce> getForces() {
        return this.forces;
    }

    public double getTimeStep() {
        return this.timeStep;
    }

    public void setTimeStep(double t) {
        this.timeStep = t;
    }

    public void addChange(MultiTimeStep timeStep) {
        history.add(timeStep);
    }

    public ArrayList<MultiTimeStep> getHistory() {
        return this.history;
    }

    public ArrayList<String> getHistoryCSV() {
        String tmpS = "Time";
        for (int i1 = 0; i1 < forces.size(); i1++) {
            tmpS += "," + forces.get(i1).getName();
        }
        ArrayList<String> tmpList = new ArrayList();
        tmpList.add(tmpS);
        MultiTimeStep tmpH;
        for (int i1 = 0; i1 < history.size(); i1++) {
            tmpH = history.get(i1);
            tmpS = tmpH.getCSVString();
            tmpList.add(tmpS);
        }
        return tmpList;
    }

    public void step() {
        boolean aboveFloor = true;
        double currentCycle = 0.;
        int numFoes = forces.size();
        if (isMyTurn == null) {
            isMyTurn = new boolean[numFoes][numFoes];
            stanceArray = new int[numFoes][numFoes];
            currentFloor = new double[numFoes][numFoes];
            for (int i1 = 0; i1 < numFoes; i1++) {
                int ind1 = forceMap.get(forces.get(i1));
                for (int i2 = 0; i2 < numFoes; i2++) {
                    int ind2 = forceMap.get(forces.get(i2));
                    isMyTurn[i1][i2] = true;
                    if (i1 == i2) {
                        stanceArray[i1][i2] = AthenaConstants.ALLIED_POSTURE;
                        currentFloor[i1][i2] = 0.;
                    } else {
                        stanceArray[i1][i2] = initializeStance(forces.get(i1), forces.get(i2));
                        setFloor(i1, i2);
                    }
                }
            }
        }
        Array2DRowRealMatrix mat = getMat();
        EigenDecomposition eigen = new EigenDecomposition(mat);
        double det = eigen.getDeterminant();
        double[] eVals = eigen.getRealEigenvalues();
//        for(int i1=0;i1<eVals.length;i1++){
//            System.out.println("eVals["+i1+"] = "+eVals[i1]);
//        }
        if (eigen.hasComplexEigenvalues()) {
            System.out.println("Complex eigenvalues");
            for (int i1 = 0; i1 < forces.size(); i1++) {
                AthenaForce f = forces.get(i1);
                System.out.println(f.getName() + " has " + f.getForceSize() + " forces remaining");
            }
        }
        double[] initialNums = getInitialNumbers(forces);
        Array2DRowRealMatrix eVectors = (Array2DRowRealMatrix) eigen.getV();
        LUDecomposition lu = new LUDecomposition(eVectors);
        double det2 = lu.getDeterminant();
        double[] coeffs = new double[numFoes];
        for (int i1 = 0; i1 < numFoes; i1++) {
            Array2DRowRealMatrix tmpMat = (Array2DRowRealMatrix) eVectors.copy();
            tmpMat.setColumn(i1, initialNums);
            LUDecomposition tmpLU = new LUDecomposition(tmpMat);
            double tmpDet = tmpLU.getDeterminant();
            coeffs[i1] = tmpDet / det2;
        }
        aboveFloor = true;
        int cntr = 0;
        int numGone;
        do {
            MultiTimeStep currentStep = new MultiTimeStep(numFoes);
            currentTime += timeStep;
            currentCycle += timeStep;
            currentStep.setTime(currentTime);
            numGone = 0;
            for (int i1 = 0; i1 < numFoes; i1++) {
                double updatedForce = 0.;
                if (forces.get(i1).getForceSize() > lb) {
                    for (int i2 = 0; i2 < numFoes; i2++) {
//                    updatedForce += coeffs[i2] * eVectors.getEntry(i1, i2) * Math.exp(eVals[i2] * timeStep);
                        updatedForce += coeffs[i2] * eVectors.getEntry(i1, i2) * Math.exp(eVals[i2] * currentCycle);
                        if (updatedForce < 1.) {
                            updatedForce = 0.;
                            numGone++;
                        }
//                updatedForce+=coeffs[i2]*eVectors.getEntry(i2, i1)*Math.exp(eVals[i2]*timeStep);
//                updatedForce+=coeffs[i1]*eVectors.getEntry(i2, i1)*Math.exp(eVals[i1]*timeStep);
                    }
                } else {
                    updatedForce = lb / 2.;
                    numGone++;
                }
                forces.get(i1).updateForce(updatedForce);
                currentStep.setForceNumber(updatedForce, i1);
            }
            history.add(currentStep);
            aboveFloor = checkAboveFloors();
            cntr++;
        } while (aboveFloor && cntr < 2000 && (numFoes - numGone) > 1);
        for (int i1 = 0; i1 < numFoes; i1++) {
            for (int i2 = 0; i2 < numFoes; i2++) {
                if (i1 == i2) {
                    stanceArray[i1][i2] = AthenaConstants.ALLIED_POSTURE;
                    currentFloor[i1][i2] = 0.;
                } else {
                    stanceArray[i1][i2] = initializeStance(forces.get(i1), forces.get(i2));
                    setFloor(i1, i2);
                }
            }
        }
        
//        eVectors.
//        this.currentTime++;
//                Truncator truncator = new Truncator();
        if (numFoes-numGone == 1) {
            loneSurvivor=true;
//            System.out.println("time = " + time);
        }
    }
    public void setLoneSurvivor(boolean ls){
        this.loneSurvivor=ls;
    }
    public boolean getLoneSurvivor(){
        return this.loneSurvivor;
    }

    public boolean checkAboveFloors() {
        boolean ret = true;
        for (int i1 = 0; i1 < forces.size(); i1++) {
            AthenaForce f1 = forces.get(i1);
            for (int i2 = 0; i2 < forces.size(); i2++) {
                AthenaForce f2 = forces.get(i2);
                double rat = f1.getForceSize() / f2.getForceSize();
                if (rat <= currentFloor[i1][i2]) {
                    ret = false;
                    System.out.println(" fell through floor - " + i1 + " vs " + i2 + " with force size = " + f1.getForceSize());

                }
            }
        }
        return ret;
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

    public Array2DRowRealMatrix getMat() {
        int numFoes = forces.size();
        AthenaForce f1;
        AthenaForce f2;
        Array2DRowRealMatrix mat = new Array2DRowRealMatrix(numFoes, numFoes);
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
//                    int oldStance1=f1.getCurrentStance();
//                    int oldStance2=f2.getCurrentStance();
                    setStances(f1, f2);
//                    int newStance1=f1.getCurrentStance();
//                    int newStance2=f2.getCurrentStance();
//                    if(oldStance1!=newStance1||oldStance2!=newStance2){
//                        System.out.println("Stance change at time = "+currentTime);
//                        System.out.println("F1 - "+oldStance1+" to "+newStance1);
//                        System.out.println("F2 - "+oldStance2+" to "+newStance2);
//                    }
//                    double x0 = f1.getForceSize();
//                    double y0 = f2.getForceSize();
                    double a1 = f2.getForceMultiplier();
                    double b2 = f1.getForceMultiplier();
                    if (isBattle) {
                        a1 *= AthenaConstants.battle[stanceArray[i1][i2]][stanceArray[i2][i1]];
                        b2 *= AthenaConstants.battle[stanceArray[i2][i1]][stanceArray[i1][i2]];
                    } else {
                        a1 *= AthenaConstants.skirmish[stanceArray[i1][i2]][stanceArray[i2][i1]];
                        b2 *= AthenaConstants.skirmish[stanceArray[i2][i1]][stanceArray[i1][i2]];
                    }

                    mat.setEntry(i1, i2, -a1);
                    mat.setEntry(i2, i1, -b2);
                }
            }
        }
        return mat;
    }

    public void setStances(AthenaForce f1, AthenaForce f2) {
        double currentF1 = f1.getForceSize();
        double currentF2 = f2.getForceSize();
        double ratio1to2 = currentF1 / currentF2;
        int ind1 = forceMap.get(f1);
        int ind2 = forceMap.get(f2);
        boolean changed = false;
//        if (isMyTurn[ind1][ind2]) {
        changed = checkForce(f1, ratio1to2);
//            if (changed) {
//                toggle(ind1, ind2);
//                return;
//            }
//        }

        double ratio2to1 = currentF2 / currentF1;
//        changed = false;
//        if (isMyTurn[ind2][ind1]) {
        changed = checkForce(f2, ratio2to1);
//            if (changed) {
//                toggle(ind2, ind1);
//                return;
//            }
//        }
    }

    public int initializeStance(AthenaForce f1, AthenaForce f2) {
        double currentF1 = f1.getForceSize();
        double currentF2 = f2.getForceSize();
        double ratio1to2 = currentF1 / currentF2;
//        int ind1 = forceMap.get(f1);
//        int ind2 = forceMap.get(f2);
        return findStance(f1, ratio1to2);

    }

    public int findStance(AthenaForce f, double rat) {
        if (rat > f.attackDefendRatio) {
            return AthenaConstants.ATTACK_POSTURE;
        } else if (rat > f.defendWithdrawRatio) {
            return AthenaConstants.DEFEND_POSTURE;
        } else {
            return AthenaConstants.WITHDRAW_POSTURE;
        }

    }

    public void setFloor(int i, int j) {
        AthenaForce f = forces.get(i);
        if (stanceArray[i][j] == AthenaConstants.ATTACK_POSTURE) {
            currentFloor[i][j] = f.attackDefendRatio;
        } else if (stanceArray[i][j] == AthenaConstants.DEFEND_POSTURE) {
            currentFloor[i][j] = f.defendWithdrawRatio;
        } else if (stanceArray[i][j] == AthenaConstants.WITHDRAW_POSTURE) {
            currentFloor[i][j] = 0.;
        }

    }
//

    public void toggle(int a, int b) {
        isMyTurn[a][b] = false;
        isMyTurn[b][a] = true;
    }

    public boolean checkForce(AthenaForce f, double ratio) {
        boolean changed = false;
        int old = f.getCurrentStance();
        if (ratio > f.attackDefendRatio) {
            f.setStance(AthenaConstants.ATTACK_POSTURE);
        } else if (ratio > f.defendWithdrawRatio) {
            f.setStance(AthenaConstants.DEFEND_POSTURE);
        } else {
            f.setStance(AthenaConstants.WITHDRAW_POSTURE);
        }
        if (f.getCurrentStance() != old) {
            changed = true;
        }
        return changed;
    }

    public double[] getInitialNumbers(ArrayList<AthenaForce> forces) {
        double[] nums = new double[forces.size()];
        for (int i1 = 0; i1 < forces.size(); i1++) {
            nums[i1] = forces.get(i1).getForceSize();
        }
        return nums;
    }
}
