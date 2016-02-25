/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchester;

import java.util.ArrayList;
import org.apache.commons.math3.linear.*;

/**
 *
 * @author Jeff
 */
public class AthenaArena {

    public AthenaForce f1 = null;
    public AthenaForce f2 = null;
    public double currentTime = 0;
    public double maxTime = 10000.;
    public ArrayList<TimeStep> history = new ArrayList();
    public ArrayList<DTimeStep> dHistory = new ArrayList();
    public boolean isBattle = true;

    public AthenaArena(AthenaForce f1, AthenaForce f2) {
        this.f1 = f1;
        this.f2 = f2;
        history.add(new TimeStep((int) (0.5 + this.currentTime), (int) (0.5 + f1.forceSize), (int) (0.5 + f2.forceSize)));
        dHistory.add(new DTimeStep( this.currentTime,  f1.forceSize, f2.forceSize));
        AthenaConstants.fillArrays();
    }

    public void addChange(TimeStep timeStep) {
        history.add(timeStep);
    }

    public ArrayList<TimeStep> getHistory() {
        return this.history;
    }

    public ArrayList<String> getHistoryCSV() {
        String tmpS = "Time Step," + f1.getName() + "'" + f2.getName();
        ArrayList<String> tmpList = new ArrayList();
        tmpList.add(tmpS);
        TimeStep tmpH;
        for (int i1 = 0; i1 < history.size(); i1++) {
            tmpH = history.get(i1);
            tmpS = tmpH.timeStep + "," + tmpH.f1Number + "," + tmpH.f2Number;
            tmpList.add(tmpS);
        }
        return tmpList;
    }

    public void step() {
//        this.currentTime++;
        Truncator truncator = new Truncator();
        double currentF1 = f1.getForceSize();
        double currentF2 = f2.getForceSize();
        double ratio1to2 = currentF1 / currentF2;
        double minRx = 1.;
        double minRy = 1.;
        if (ratio1to2 > f1.attackDefendRatio) {
            f1.setStance(AthenaConstants.ATTACK_POSTURE);
            minRx *= f1.attackDefendRatio;
        } else if (ratio1to2 > f1.defendWithdrawRatio) {
            f1.setStance(AthenaConstants.DEFEND_POSTURE);
            minRx *= f1.defendWithdrawRatio;
        } else {
            f1.setStance(AthenaConstants.WITHDRAW_POSTURE);
            minRx *= 0.;
        }
        double ratio2to1 = currentF2 / currentF1;
        if (ratio2to1 > f2.attackDefendRatio) {
            f2.setStance(AthenaConstants.ATTACK_POSTURE);
            minRy *= f2.attackDefendRatio;
        } else if (ratio2to1 > f2.defendWithdrawRatio) {
            f2.setStance(AthenaConstants.DEFEND_POSTURE);
            minRy *= f2.defendWithdrawRatio;
        } else {
            f2.setStance(AthenaConstants.WITHDRAW_POSTURE);
            minRy *= 0.;
        }
        double x0 = f1.getForceSize();
        double y0 = f2.getForceSize();
        double a1 = f1.getAttrition();
        double b2 = f2.getAttrition();
        if (isBattle) {
            a1 *= AthenaConstants.battle[f1.getCurrentStance()][f2.getCurrentStance()];
            b2 *= AthenaConstants.battle[f2.getCurrentStance()][f1.getCurrentStance()];
        } else {
            a1 *= AthenaConstants.skirmish[f1.getCurrentStance()][f2.getCurrentStance()];
            b2 *= AthenaConstants.skirmish[f2.getCurrentStance()][f1.getCurrentStance()];
        }
        double rootA = Math.sqrt(a1);
        double rootB = Math.sqrt(b2);
        double c1 = 0.5 * ((x0 / rootA) - (y0 / rootB));
        double c2 = 0.5 * ((x0 / rootA) + (y0 / rootB));
        double time = 1.;
        if (c1 < 0.) {
            time *= (0.5 / (rootA * rootB)) * Math.log((c2 / c1) * ((minRx * rootB - rootA) / (minRx * rootB + rootA)));
        } else {
            time *= (0.5 / (rootA * rootB)) * Math.log((c2 / c1) * ((rootB - minRy * rootA) / (rootB + minRy * rootA)));
        }
        if (true) {
//            System.out.println("time = " + time);
        }
        double remainingTime = this.maxTime - time - this.currentTime;
//        if (remainingTime < 0) {
//            time = this.maxTime - this.currentTime;
//            remainingTime = 0.;
//        }
        double nextD1 = getX(time, c1, c2, rootA, rootB);
        double nextD2 = getY(time, c1, c2, rootA, rootB);
        System.out.println("time = "+time+"  C1 = "+c1+"  C2 = "+c2);
        int nextN1 = (int) (0.5 + nextD1);
        if (nextN1 < 1) {
            nextN1 = 0;
        }
        int nextN2 = (int) (0.5 + nextD2);
        if (nextN2 < 1) {
            nextN2 = 0;
        }

        this.currentTime += time;
        nextD1=truncator.truncate(nextD1,1.e-15);
        nextD2=truncator.truncate(nextD2,1.e-15);
        TimeStep step = new TimeStep((int) (0.5 + this.currentTime), nextN1, nextN2);
        DTimeStep dStep = new DTimeStep( this.currentTime, nextD1, nextD2);
        history.add(step);
        dHistory.add(dStep);
        f1.previousForceSize = f1.getForceSize();
        f1.setForceSize(nextD1);
        f2.previousForceSize = f2.getForceSize();
        f2.setForceSize(nextD2);
//        System.out.println("del 1 = "+(f1.previousForceSize-f1.forceSize)+"  del 2 = "+(f2.previousForceSize-f2.forceSize));
    }

    public double getX(double t, double c11, double c22, double rAA, double rBB) {
        double res = 1.;
        res *= c11 * rAA * Math.exp(t * rAA * rBB) + c22 * rAA * Math.exp(-1 * t * rAA * rBB);
        return res;
    }

    public double getY(double t, double c11, double c22, double rAA, double rBB) {
        double res = -1.;
        res *= c11 * rBB * Math.exp(t * rAA * rBB) - c22 * rBB *Math.exp(-1 * t * rAA * rBB);
        return res;
    }
}
