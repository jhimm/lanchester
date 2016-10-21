/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

//import lanchester.*;
//import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Jeff
 */
public class N2Modifier {

    public HashMap<String, Double> offenseMap = new HashMap();
    public HashMap<String, Double> defenseMap = new HashMap();

    public N2Modifier() {
        this.offenseMap.put(N2Constants.CIVILIAN_CONCERN, 1.);
        this.offenseMap.put(N2Constants.DEMEANOR, 1.);
        this.offenseMap.put(N2Constants.EQUIPMENT, 1.);
        this.offenseMap.put(N2Constants.FORCE_TYPE, 1.);
        this.offenseMap.put(N2Constants.TRAINING, 1.);
        this.offenseMap.put(N2Constants.URBAN, 1.);
        this.offenseMap.put(N2Constants.INTEL, 1.);
        this.defenseMap.put(N2Constants.CIVILIAN_CONCERN, 1.);
        this.defenseMap.put(N2Constants.DEMEANOR, 1.);
        this.defenseMap.put(N2Constants.EQUIPMENT, 1.);
        this.defenseMap.put(N2Constants.FORCE_TYPE, 1.);
        this.defenseMap.put(N2Constants.TRAINING, 1.);
        this.defenseMap.put(N2Constants.URBAN, 1.);
        this.defenseMap.put(N2Constants.INTEL, 1.);
    }

    public void setOffenseMultplier(String k, double v) {
        this.offenseMap.put(k, v);
    }

    public double getOffenseMultiplier(String k) {
        return this.offenseMap.get(k);
    }

    public double getTotalOffenseMultiplier() {
        double res = 1.;
        for (String k : this.offenseMap.keySet()) {
            res *= this.offenseMap.get(k);
        }
        return res;
    }

    public void setDefenseMultplier(String k, double v) {
        this.defenseMap.put(k, v);
    }

    public double getDefenseMultiplier(String k) {
        return this.defenseMap.get(k);
    }

    public double getTotalDefenseMultiplier() {
        double res = 1.;
        for (String k : this.offenseMap.keySet()) {
            res *= this.defenseMap.get(k);
        }
        return res;
    }

    public double getOffensiveTrainingLevel() {
        return this.offenseMap.get(N2Constants.TRAINING);
    }

    public void setOffensiveTrainingLevel(double otl) {

        this.offenseMap.remove(N2Constants.TRAINING);
        this.offenseMap.put(N2Constants.TRAINING, otl);
    }

    public double getDefensiveTrainingLevel() {
        return this.defenseMap.get(N2Constants.TRAINING);
    }

    public void setDefensiveTrainingLevel(double dtl) {

        this.defenseMap.remove(N2Constants.TRAINING);
        this.defenseMap.put(N2Constants.TRAINING, dtl);
    }
}
