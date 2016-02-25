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
    public double timeStep = 0.001;

    public MultiArena() {
        AthenaConstants.fillArrays();
    }

    public void addForce(AthenaForce f) {
        forces.add(f);
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
        int numFoes = forces.size();
        Array2DRowRealMatrix mat = getMat();
        EigenDecomposition eigen = new EigenDecomposition(mat);
        double det = eigen.getDeterminant();
        double[] eVals = eigen.getRealEigenvalues();
//        for(int i1=0;i1<eVals.length;i1++){
//            System.out.println("eVals["+i1+"] = "+eVals[i1]);
//        }
        if (eigen.hasComplexEigenvalues()) {
            System.out.println("Complex eigenvalues");
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
        MultiTimeStep currentStep = new MultiTimeStep(numFoes);
        currentTime+=timeStep;
        currentStep.setTime(currentTime);
        for(int i1=0;i1<numFoes;i1++){
            double updatedForce=0.;
            for(int i2=0;i2<numFoes;i2++){
                updatedForce+=coeffs[i2]*eVectors.getEntry(i1, i2)*Math.exp(eVals[i2]*timeStep);
            }
            forces.get(i1).updateForce(updatedForce);
            currentStep.setForceNumber(updatedForce, i1);
        }
        history.add(currentStep);
//        eVectors.
//        this.currentTime++;
//                Truncator truncator = new Truncator();
        if (true) {
//            System.out.println("time = " + time);
        }
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

    public double[] getInitialNumbers(ArrayList<AthenaForce> forces) {
        double[] nums = new double[forces.size()];
        for (int i1 = 0; i1 < forces.size(); i1++) {
            nums[i1] = forces.get(i1).getForceSize();
        }
        return nums;
    }
}
