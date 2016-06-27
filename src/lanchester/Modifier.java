/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchester;

import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Jeff
 */
public class Modifier {

    public HashMap<String, Double> multMap = new HashMap();

    public Modifier() {
        this.multMap.put(AthenaConstants.CIVILIAN_CONCERN, 1.);
        this.multMap.put(AthenaConstants.DEMEANOR, 1.);
        this.multMap.put(AthenaConstants.EQUIPMENT, 1.);
        this.multMap.put(AthenaConstants.FORCE_TYPE, 1.);
        this.multMap.put(AthenaConstants.TRAINING, 1.);
        this.multMap.put(AthenaConstants.URBAN, 1.);
        this.multMap.put(AthenaConstants.INTEL, 1.);
    }

    public void setMultplier(String k, double v) {
        this.multMap.put(k, v);
    }

    public double getMultiplier(String k) {
        return this.multMap.get(k);
    }

    public double getTotalMultiplier() {
        double res = 1.;
        for (String k : this.multMap.keySet()) {
            res *= this.multMap.get(k);
        }
        return res;
    }
}
