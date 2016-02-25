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
public class AthenaForce {

    public double attackDefendRatio = 1.5;
    public double defendWithdrawRatio = 0.6;
    public double forceSize = 1000.;
    public double previousForceSize=1000.;
    public double replenishment = 0.002;
    public String name="";
    public ArrayList<AthenaForce> friends = new ArrayList();
    public int currentStance = AthenaConstants.ATTACK_POSTURE;
    public Modifier modifier = new Modifier();
    public AthenaForce(String nm){
        this.name=nm;
    }
    public double getAttrition(){
        return this.modifier.getTotalMultiplier();
    }
    public double getForceMultiplier(){
        return this.modifier.getTotalMultiplier();
    }
    public String getName(){
        return this.name;
    }
    public double getForceSize(){
        return this.forceSize;
    }
    public double getPreviousForceSize(){
        return this.previousForceSize;
    }
    public double getAttackDefendRatio(){
        return this.attackDefendRatio;
    }
    public double getDefendWithdrawRatio(){
        return this.defendWithdrawRatio;
    }
    public double getReplenishment(){
        return this.replenishment;
    }
    public void setReplenishment(double r){
        this.replenishment=r;
    }
    public void setAttackDefendRatio(double atr){
        this.attackDefendRatio=atr;
    }
    public void setDefendWithdrawRatio(double dwr){
        this.defendWithdrawRatio=dwr;
    }
    public void setForceSize(double size){
        this.forceSize=size;
    }
    public void setStance(int s){
        this.currentStance =s; 
    }
    public void setFriend(AthenaForce force){
        this.friends.add(force);
    }
    public boolean isFriend(AthenaForce force){
        if(friends.contains(force))return true;
        return false;
    }
    public void removeFriend(AthenaForce force){
        if(friends.contains(force)){
            friends.remove(force);
        }
    }
    public int getCurrentStance(){
        return this.currentStance;
    }
    public String toString(){
        String tmpS=getName();
        tmpS+="\n force = "+getForceSize();
        tmpS+="\n stance = "+getCurrentStance();
        tmpS+="\n attrition = "+getAttrition();
        tmpS+="\n AD ratio = "+getAttackDefendRatio();
        tmpS+="\n DW ratio = "+getDefendWithdrawRatio();
        return tmpS;
    }
}
