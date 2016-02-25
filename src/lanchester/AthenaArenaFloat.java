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
public class AthenaArenaFloat {

    public AthenaForce f1 = null;
    public AthenaForce f2 = null;
    public float currentTime = 0;
    public float maxTime = 100;
    public ArrayList<TimeStep> history = new ArrayList();
    public ArrayList<DTimeStep> dHistory = new ArrayList();
    public boolean isBattle = true;

    public AthenaArenaFloat(AthenaForce f1, AthenaForce f2) {
        this.f1 = f1;
        this.f2 = f2;
        history.add(new TimeStep((int) (0.5 + this.currentTime), (int) (0.5 + f1.forceSize), (int) (0.5 + f2.forceSize)));
        dHistory.add(new DTimeStep( (float)this.currentTime, (float) f1.forceSize,(float) f2.forceSize));
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
//        Truncator truncator = new Truncator();
        float currentF1 = (float)f1.getForceSize();
        float currentF2 = (float)f2.getForceSize();
        float ratio1to2 = currentF1 / currentF2;
        float minRx = 1;
        float minRy = 1;
        if (ratio1to2 >(float) f1.attackDefendRatio) {
            f1.setStance(AthenaConstants.ATTACK_POSTURE);
            minRx *=(float) f1.attackDefendRatio;
        } else if (ratio1to2 > (float)f1.defendWithdrawRatio) {
            f1.setStance(AthenaConstants.DEFEND_POSTURE);
            minRx *= (float)f1.defendWithdrawRatio;
        } else {
            f1.setStance(AthenaConstants.WITHDRAW_POSTURE);
            minRx *= 0.;
        }
        float ratio2to1 = currentF2 / currentF1;
        if (ratio2to1 >(float) f2.attackDefendRatio) {
            f2.setStance(AthenaConstants.ATTACK_POSTURE);
            minRy *= (float)f2.attackDefendRatio;
        } else if (ratio2to1 > f2.defendWithdrawRatio) {
            f2.setStance(AthenaConstants.DEFEND_POSTURE);
            minRy *=(float) f2.defendWithdrawRatio;
        } else {
            f2.setStance(AthenaConstants.WITHDRAW_POSTURE);
            minRy *= 0.;
        }
        float x0 = (float)f1.getForceSize();
        float y0 = (float)f2.getForceSize();
        float a1 = (float)f1.getAttrition();
        float b2 =(float) f2.getAttrition();
        if (isBattle) {
            a1 *= AthenaConstants.battle[f1.getCurrentStance()][f2.getCurrentStance()];
            b2 *= AthenaConstants.battle[f2.getCurrentStance()][f1.getCurrentStance()];
        } else {
            a1 *= AthenaConstants.skirmish[f1.getCurrentStance()][f2.getCurrentStance()];
            b2 *= AthenaConstants.skirmish[f2.getCurrentStance()][f1.getCurrentStance()];
        }
        float rootA = (float)Math.sqrt(a1);
        float rootB = (float)Math.sqrt(b2);
        float c1 = (float)0.5 * ((x0 / rootA) - (y0 / rootB));
        float c2 = (float)0.5 * ((x0 / rootA) + (y0 / rootB));
        float time = 1.f;
        if (c1 < 0.) {
            time *= (0.5 / (rootA * rootB)) * Math.log((c2 / c1) * ((minRx * rootB - rootA) / (minRx * rootB + rootA)));
        } else {
            time *= (0.5 / (rootA * rootB)) * Math.log((c2 / c1) * ((rootB - minRy * rootA) / (rootB + minRy * rootA)));
        }
//        if (true) {
////            System.out.println("time = " + time);
//        }
//        float remainingTime = this.maxTime - time - this.currentTime;
//        if (remainingTime < 0) {
//            time = this.maxTime - this.currentTime;
//            remainingTime = 0.;
//        }
        float nextD1 = getX(time, c1, c2, rootA, rootB);
        float nextD2 = getY(time, c1, c2, rootA, rootB);
        System.out.println("time = "+time+"  C1 = "+c1+"  C2 = "+c2+" nextD1 = "+nextD1+" nextD2 = "+nextD2);
        int nextN1 = (int) (0.5f + nextD1);
        if (nextN1 < 1) {
            nextN1 = 0;
        }
        int nextN2 = (int) (0.5f + nextD2);
        if (nextN2 < 1) {
            nextN2 = 0;
        }

        this.currentTime += time;
//        nextD1=truncator.truncate(nextD1,1.e-15);
//        nextD2=truncator.truncate(nextD2,1.e-15);
        TimeStep step = new TimeStep((int) (0.5 + this.currentTime), nextN1, nextN2);
        DTimeStep dStep = new DTimeStep( (float)this.currentTime, (float)nextD1, (float)nextD2);
        history.add(step);
        dHistory.add(dStep);
        f1.previousForceSize = (float)f1.getForceSize();
        f1.setForceSize((float)nextD1);
        f2.previousForceSize = (float)f2.getForceSize();
        f2.setForceSize((float)nextD2);
//        System.out.println("del 1 = "+(f1.previousForceSize-f1.forceSize)+"  del 2 = "+(f2.previousForceSize-f2.forceSize));
    }

    public float getX(float t, float c11, float c22, float rAA, float rBB) {
        float res = 1;
        res *= c11 * rAA * Math.exp(t * rAA * rBB) + c22 * rAA * Math.exp(-1 * t * rAA * rBB);
        return res;
    }

    public float getY(float t, float c11, float c22, float rAA, float rBB) {
        float res = -1;
        res *= c11 * rBB * Math.exp(t * rAA * rBB) - c22 * rBB *Math.exp(-1 * t * rAA * rBB);
        return res;
    }
}
