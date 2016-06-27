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
public class MultiForce {

    public double number = 1000;
    public double previousNumber = 1000;
    public double combatLosses = 0.01;
    public double otherLosses = 0.01;
    public int replenishment = 25;
    public int forceIndex = -1;
    public String name = "force 1";
    public ArrayList<MultiForce> foes = new ArrayList();
    public HashMap<MultiForce, Double> adFloorMap = new HashMap();
    public HashMap<MultiForce, Double> adCeilingMap = new HashMap();
    public HashMap<MultiForce, Double> dwFloorMap = new HashMap();
    public HashMap<MultiForce, Double> dwCeilingMap = new HashMap();
    public ArrayList<MultiForce> friends = new ArrayList();
    public Modifier modifier = new Modifier();
    public double attackDefendFloorRatio = 1.5;
    public double defendWithdrawFloorRatio = 0.6;
    public double attackDefendCeilingRatio = 1.8;
    public double defendWithdrawCeilingRatio = 0.8;

    public MultiForce(String n, double num) {
        this.name = n;
        this.number = num;
        this.previousNumber = num;
    }

    public void setName(String n) {
        this.name = n;
    }

    public void setNumber(double num) {
        this.number = num;
    }

    public void setCombatLosses(double cl) {
        this.combatLosses = cl;
    }

    public void setOtherLosses(double ol) {
        this.otherLosses = ol;
    }

    public void setReplenishment(int r) {
        this.replenishment = r;
    }

    public void setFoes(ArrayList<MultiForce> fList) {
        this.foes.clear();
        this.foes.addAll(fList);
    }

    public void addFoe(MultiForce f) {
        this.foes.add(f);
        setADFloorRatio(f, this.attackDefendFloorRatio);
        setADCeilingRatio(f, this.attackDefendCeilingRatio);
        setDWFloorRatio(f, this.defendWithdrawFloorRatio);
        setDWCeilingRatio(f, this.defendWithdrawCeilingRatio);
    }

    public void setADFloorRatio(MultiForce f, double d) {
        if (this.adFloorMap.containsKey(f)) {
            Double val = adFloorMap.get(f);
            this.adFloorMap.remove(f, val);
        }
        this.adFloorMap.put(f, d);

    }

    public void setADCeilingRatio(MultiForce f, double d) {
        if (this.adCeilingMap.containsKey(f)) {
            Double val = adCeilingMap.get(f);
            this.adCeilingMap.remove(f, val);
        }
        this.adCeilingMap.put(f, d);

    }

    public void setDWFloorRatio(MultiForce f, double d) {
        if (this.dwFloorMap.containsKey(f)) {
            Double val = dwFloorMap.get(f);
            this.dwFloorMap.remove(f, val);
        }
        this.dwFloorMap.put(f, d);

    }

    public void setDWCeilingRatio(MultiForce f, double d) {
        if (this.dwCeilingMap.containsKey(f)) {
            Double val = dwCeilingMap.get(f);
            this.dwCeilingMap.remove(f, val);
        }
        this.dwCeilingMap.put(f, d);

    }
    public void setFriend(MultiForce force){
        if(!friends.contains(force)){
            this.friends.add(force);
        }
    }
    public void setForceIndex(int n){
        this.forceIndex=n;
    }
    public int getForceIndex(){
        return this.forceIndex;
    }
    
    public boolean isFriend(MultiForce force){
        if(friends.contains(force))return true;
        return false;
    }
    public void removeFriend(MultiForce force){
        if(friends.contains(force)){
            friends.remove(force);
        }
    }
    public double getAttrition(){
        return this.modifier.getTotalMultiplier();
    }
    public double getForceMultiplier(){
        return this.modifier.getTotalMultiplier();
    }
    public void setMultplier(String k,double v){
        this.modifier.multMap.put(k,v);
    }
    public double getMultiplier(String k){
        return this.modifier.multMap.get(k);
    }

    public String getName() {
        return this.name;
    }

    public double getNumber() {
        return this.number;
    }

    public double getCombatLosses() {
        return this.combatLosses;
    }

    public double getOtherLosses() {
        return this.otherLosses;
    }

    public int getReplenishment() {
        return this.replenishment;
    }

    public ArrayList<MultiForce> getFoes() {
        return this.foes;
    }

    public void removeFoe(MultiForce f) {
        if (this.foes.contains(f)) {
            this.foes.remove(f);
        }
    }
    public void updateForce(double f){
        this.previousNumber=this.number;
        this.number=f;
    }
}
