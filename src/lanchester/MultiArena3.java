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
public class MultiArena3 {

    public AthenaForce f1 = null;
    public AthenaForce f2 = null;
    public double currentTime = 0.;
    public double maxTime = 10000.;
    public ArrayList<MultiTimeStep> history = new ArrayList();
    public ArrayList<DTimeStep> dHistory = new ArrayList();
    public ArrayList<MultiForce> forces = new ArrayList();
    public boolean isBattle = true;
    public double timeStep = 0.1;
    public HashMap<MultiForce, Integer> forceMap = new HashMap();
    public boolean[][] isMyTurn = null;
    public int[][] stanceArray = null;
    public double[][] currentFloor = null;
    public double[][] currentCeiling = null;
    public double lb = 1.e-5;
    public boolean loneSurvivor = false;
    public boolean[] stillAlive = null;

    public MultiArena3() {
        AthenaConstants.fillArrays();
    }

    public void addForce(MultiForce f) {
        int num = forces.size();
        forces.add(f);
        forceMap.put(f, num);
        f.setForceIndex(num);
        stillAlive = new boolean[forces.size()];
        for (int i1 = 0; i1 <= num; i1++) {
            stillAlive[i1] = true;
        }
    }

    public ArrayList<MultiForce> getForces() {
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
            currentCeiling = new double[numFoes][numFoes];
            for (int i1 = 0; i1 < numFoes; i1++) {
                int ind1 = forceMap.get(forces.get(i1));
                for (int i2 = 0; i2 < numFoes; i2++) {
                    int ind2 = forceMap.get(forces.get(i2));
                    isMyTurn[i1][i2] = true;
                    if (i1 == i2) {
                        stanceArray[i1][i2] = AthenaConstants.ALLIED_POSTURE;
                        currentFloor[i1][i2] = 0.;
                        currentCeiling[i1][i2] = 100.;
                    } else {
                        stanceArray[i1][i2] = initializeStance(forces.get(i1), forces.get(i2));
                        setFloor(i1, i2);
                        setCeiling(i1, i2);
                    }
                }
            }
        }
        Array2DRowRealMatrix mat = getMat();
        EigenDecomposition eigen = new EigenDecomposition(mat);
        double det = eigen.getDeterminant();
        double[] eVals = eigen.getRealEigenvalues();
        if (eigen.hasComplexEigenvalues()) {
            System.out.println("Complex eigenvalues");
            for (int i1 = 0; i1 < forces.size(); i1++) {
                MultiForce f = forces.get(i1);
                System.out.println(f.getName() + " has " + f.getNumber() + " forces remaining");
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
        boolean belowCeiling = true;
        int cntr = 0;
        int numGone;
        do {
            timeStep = determineTimeStep();
            MultiTimeStep currentStep = new MultiTimeStep(numFoes);
            currentTime += timeStep;
            currentCycle += timeStep;
            currentStep.setTime(currentTime);
            numGone = 0;
            for (int i1 = 0; i1 < numFoes; i1++) {
                double updatedForce = 0.;
                if (stillAlive[i1]) {
                    for (int i2 = 0; i2 < numFoes; i2++) {
                        updatedForce += coeffs[i2] * eVectors.getEntry(i1, i2) * Math.exp(eVals[i2] * currentCycle);
                    }
                    if (updatedForce < 1.) {
                        updatedForce = lb;
                        stillAlive[i1] = false;
                        numGone++;
                    }
                } else {
                    numGone++;
                    updatedForce = lb;
                }
                forces.get(i1).updateForce(updatedForce);
                currentStep.setForceNumber(updatedForce, i1);
            }
            history.add(currentStep);
            aboveFloor = checkAboveFloors();
            belowCeiling = checkBelowCeilings();
            cntr++;
        } while (aboveFloor && belowCeiling && cntr < 2000 && (numFoes - numGone) > 1);
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
        if (numFoes - numGone == 1) {
            loneSurvivor = true;
//            System.out.println("time = " + time);
        }
    }

    public double[] project(double deltaT, double[] coeffs, double[] eVals, Array2DRowRealMatrix eVects) {
        int numFoes = coeffs.length;
        double[] updated = new double[numFoes];
        for (int i1 = 0; i1 < numFoes; i1++) {
            double updatedForce = 0.;
            if (forces.get(i1).getNumber() > lb) {
                for (int i2 = 0; i2 < numFoes; i2++) {
//                    updatedForce += coeffs[i2] * eVectors.getEntry(i1, i2) * Math.exp(eVals[i2] * timeStep);
                    updatedForce += coeffs[i2] * eVects.getEntry(i1, i2) * Math.exp(eVals[i2] * deltaT);
                    if (updatedForce < 1.) {
                        updatedForce = 0.;
//                            numGone++;
                    }
//                updatedForce+=coeffs[i2]*eVectors.getEntry(i2, i1)*Math.exp(eVals[i2]*timeStep);
//                updatedForce+=coeffs[i1]*eVectors.getEntry(i2, i1)*Math.exp(eVals[i1]*timeStep);
                }
            } else {
                updatedForce = lb / 2.;
//                    numGone++;
            }
            updated[i1] = updatedForce;
        }
        return updated;
    }

    public void setLoneSurvivor(boolean ls) {
        this.loneSurvivor = ls;
    }

    public boolean getLoneSurvivor() {
        return this.loneSurvivor;
    }

    public boolean checkAboveFloors() {
        boolean ret = true;
        for (int i1 = 0; i1 < forces.size(); i1++) {
            MultiForce f1 = forces.get(i1);
            for (int i2 = 0; i2 < forces.size(); i2++) {
                MultiForce f2 = forces.get(i2);
                double rat = f1.getNumber() / f2.getNumber();
                if (rat <= currentFloor[i1][i2]) {
                    ret = false;
                    System.out.println(" fell through floor - " + i1 + " vs " + i2 + " with force size = " + f1.getNumber());
                }
            }
        }
        return ret;
    }

    public boolean checkBelowCeilings() {
        boolean ret = true;
        for (int i1 = 0; i1 < forces.size(); i1++) {
            MultiForce f1 = forces.get(i1);
            for (int i2 = 0; i2 < forces.size(); i2++) {
                MultiForce f2 = forces.get(i2);
                double rat = f1.getNumber() / f2.getNumber();
                if (rat >= currentCeiling[i1][i2]) {
                    ret = false;
                    System.out.println(" climbed through ceiling - " + i1 + " vs " + i2 + " with force size = " + f1.getNumber());
                }
            }
        }
        return ret;
    }

    public int checkAboveFloors(double[] vals) {
//        int ret = 1;
        int numLow = 0;
        for (int i1 = 0; i1 < vals.length; i1++) {
            for (int i2 = 0; i2 < vals.length; i2++) {
                double rat = vals[i1] / vals[i2];
                if (vals[i2] != 0. && rat < currentFloor[i1][i2]) {
//                    ret = -1;
                    numLow++;
//                    System.out.println(" fell through floor - " + i1 + " vs " + i2 + " with force size = " + f1.getForceSize());
                }
            }
        }
        return numLow;
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

    public double determineTimeStep() {
        if (true) {
            return 0.005;
        }
        double delT = 0.;
        double lowT = delT;
        double highT = 1.;
        double midT = 1.;
        boolean notThereYet = true;
        double[] vals = new double[forces.size()];

        do {
            midT = (highT + lowT) / 2.;

        } while (notThereYet);
        return delT;
    }

    public Array2DRowRealMatrix getMat() {
        int numFoes = forces.size();
        MultiForce f1;
        MultiForce f2;
        Array2DRowRealMatrix mat = new Array2DRowRealMatrix(numFoes, numFoes);
        for (int i1 = 0; i1 < numFoes - 1; i1++) {
            f1 = forces.get(i1);
            for (int i2 = i1 + 1; i2 < numFoes; i2++) {
                f2 = forces.get(i2);
                if (f1.isFriend(f2)) {
//                    f1.setStance(AthenaConstants.ALLIED_POSTURE);
//                    f2.setStance(AthenaConstants.ALLIED_POSTURE);
                    stanceArray[i1][i2] = (AthenaConstants.ALLIED_POSTURE);
                    stanceArray[i2][i1] = (AthenaConstants.ALLIED_POSTURE);
                    mat.setEntry(i1, i2, 0.);
                    mat.setEntry(i2, i1, 0.);
                } else {
//                    setStances(f1, f2);
                    setStances(i1, i2);
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

//    public void setStances(MultiForce f1, MultiForce f2) {
//        double currentF1 = f1.getNumber();
//        double currentF2 = f2.getNumber();
//        double ratio1to2 = currentF1 / currentF2;
////        int currentStance = 
//        int ind1 = forceMap.get(f1);
//        int ind2 = forceMap.get(f2);
//        boolean changed = false;
////        if (isMyTurn[ind1][ind2]) {
//        changed = checkForce(f1, ratio1to2);
//
//        double ratio2to1 = currentF2 / currentF1;
//        changed = checkForce(f2, ratio2to1);
//    }
    public void setStances(int i1, int i2) {
        MultiForce f1 = forces.get(i1);
        MultiForce f2 = forces.get(i2);
        double currentF1 = f1.getNumber();
        double currentF2 = f2.getNumber();
        double ratio1to2 = (currentF2 > 0. ? currentF1 / currentF2 : 10000.);
        int currentStance = stanceArray[i1][i2];
        if (currentStance == AthenaConstants.ATTACK_POSTURE) {
            if (ratio1to2 < f1.adFloorMap.get(f2)) {
                if (ratio1to2 < f1.dwFloorMap.get(f2)) {
                    stanceArray[i1][i2] = AthenaConstants.WITHDRAW_POSTURE;
                } else {
                    stanceArray[i1][i2] = AthenaConstants.DEFEND_POSTURE;
                }
            }
        } else if (currentStance == AthenaConstants.DEFEND_POSTURE) {
            if (ratio1to2 > f1.dwCeilingMap.get(f2)) {
                stanceArray[i1][i2] = AthenaConstants.ATTACK_POSTURE;
            }
            if (ratio1to2 < f1.dwFloorMap.get(f2)) {
                stanceArray[i1][i2] = AthenaConstants.WITHDRAW_POSTURE;
            }
        } else if (currentStance == AthenaConstants.WITHDRAW_POSTURE) {
            if (ratio1to2 > f1.adCeilingMap.get(f2)) {
                stanceArray[i1][i2] = AthenaConstants.ATTACK_POSTURE;
            } else if (ratio1to2 > f1.dwCeilingMap.get(f2)) {
                stanceArray[i1][i2] = AthenaConstants.DEFEND_POSTURE;
            }
        }
        double ratio2to1 = (currentF1 > 0. ? currentF2 / currentF1 : 10000.);;
        currentStance = stanceArray[i2][i1];
        if (currentStance == AthenaConstants.ATTACK_POSTURE) {
            if (ratio2to1 < f2.adFloorMap.get(f1)) {
                if (ratio2to1 < f2.dwFloorMap.get(f1)) {
                    stanceArray[i2][i1] = AthenaConstants.WITHDRAW_POSTURE;
                } else {
                    stanceArray[i2][i1] = AthenaConstants.DEFEND_POSTURE;
                }
            }
        } else if (currentStance == AthenaConstants.DEFEND_POSTURE) {
            if (ratio2to1 > f2.dwCeilingMap.get(f1)) {
                stanceArray[i2][i1] = AthenaConstants.ATTACK_POSTURE;
            }
            if (ratio2to1 < f2.dwFloorMap.get(f1)) {
                stanceArray[i2][i1] = AthenaConstants.WITHDRAW_POSTURE;
            }
        } else if (currentStance == AthenaConstants.WITHDRAW_POSTURE) {
            if (ratio2to1 > f2.adCeilingMap.get(f1)) {
                stanceArray[i2][i1] = AthenaConstants.ATTACK_POSTURE;
            } else if (ratio2to1 > f2.dwCeilingMap.get(f1)) {
                stanceArray[i2][i1] = AthenaConstants.DEFEND_POSTURE;
            }
        }
    }

    public int initializeStance(MultiForce f1, MultiForce f2) {
        double currentF1 = f1.getNumber();
        double currentF2 = f2.getNumber();
        double ratio1to2 = currentF1 / currentF2;
        if (ratio1to2 > f1.adFloorMap.get(f2)) {
            return AthenaConstants.ATTACK_POSTURE;
        } else if (ratio1to2 > f1.dwFloorMap.get(f2)) {
            return AthenaConstants.DEFEND_POSTURE;
        } else {
            return AthenaConstants.WITHDRAW_POSTURE;
        }
//        return findStance(f1, ratio1to2);

    }

    public void setFloor(int i, int j) {
        MultiForce f1 = forces.get(i);
        MultiForce f2 = forces.get(j);
        if (stanceArray[i][j] == AthenaConstants.ATTACK_POSTURE) {
            currentFloor[i][j] = f1.adFloorMap.get(f2);
        } else if (stanceArray[i][j] == AthenaConstants.DEFEND_POSTURE) {
            currentFloor[i][j] = f1.dwFloorMap.get(f2);
//            currentFloor[i][j] = f.defendWithdrawFloorRatio;
        } else if (stanceArray[i][j] == AthenaConstants.WITHDRAW_POSTURE) {
            currentFloor[i][j] = 0.;
        }

    }

    public void setCeiling(int i, int j) {
        MultiForce f1 = forces.get(i);
        MultiForce f2 = forces.get(j);
        if (stanceArray[i][j] == AthenaConstants.ATTACK_POSTURE) {
            currentCeiling[i][j] = 100000.;
        } else if (stanceArray[i][j] == AthenaConstants.DEFEND_POSTURE) {
            currentCeiling[i][j] = f1.adCeilingMap.get(f2);
//            currentCeiling[i][j] = 2 * currentFloor[i][j];
        } else if (stanceArray[i][j] == AthenaConstants.WITHDRAW_POSTURE) {
            currentCeiling[i][j] = f1.dwCeilingMap.get(f2);
//            currentCeiling[i][j] = f.defendWithdrawRatio;
//            currentCeiling[i][j] = 2 * currentFloor[i][j];
        }

    }

    public double[] getInitialNumbers(ArrayList<MultiForce> forces) {
        double[] nums = new double[forces.size()];
        for (int i1 = 0; i1 < forces.size(); i1++) {
            nums[i1] = forces.get(i1).getNumber();
        }
        return nums;
    }
}
