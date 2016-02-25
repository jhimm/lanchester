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
public class Arena {

    public Force f1 = null;
    public Force f2 = null;
    public int currentTime = 0;
    public ArrayList<TimeStep> history = new ArrayList();

    public Arena(Force f1, Force f2) {
        this.f1 = f1;
        this.f2 = f2;
        history.add(new TimeStep(this.currentTime, (int)(0.5+f1.number), (int)(0.5+f2.number)));
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
        this.currentTime++;
        double currentF1 = f1.getNumber();
        double currentF2 = f2.getNumber();
        double nextD1 = currentF1 - f1.getOtherLosses() * currentF1 - f1.combatLosses * currentF2 + f1.getReplenishment();
        int nextN1 = (int) (0.5+nextD1);
        if (nextN1 < 1) {
            nextN1 = 0;
        }
        double nextD2 = currentF2 - f2.getOtherLosses() * currentF2 - f2.combatLosses * currentF1 + f2.getReplenishment();
        int nextN2 = (int) (0.5+nextD2);
        if (nextN2 < 1) {
            nextN2 = 0;
        }
        
        TimeStep step = new TimeStep(this.currentTime, nextN1, nextN2);
        history.add(step);
        f1.previousNumber = f1.number;
        f1.setNumber(nextD1);
        f2.previousNumber = f2.number;
        f2.setNumber(nextD2);
    }
}
