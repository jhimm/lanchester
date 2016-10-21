/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

import java.io.Serializable;
import sim.engine.Steppable;
import sim.engine.Stoppable;
import sim.engine.SimState;
import sim.util.Bag;

/**
 *
 * @author Jeff
 */
public class N2BattleManager implements Steppable, Serializable, Stoppable {
    
    public Bag goodGuys = new Bag();
    public Bag badGuys = new Bag();
    public N2MultiArena arena = null;
    public Stoppable stopper = null;
    public boolean continuation = false;
    public boolean readyForBattle = false;
    public boolean battleProceeding = false;
    public int numGG = 0;
    public int numBG = 0;
    public N2Node targetNode = null;
    
    public N2BattleManager(N2Node target) {
        this.targetNode = target;
    }
    
    @Override
    public void step(SimState state) {
        if (numGG == 0 || numBG == 0) {
            getNewArena();
        }
        if (numGG != goodGuys.size() || numBG != badGuys.size()) {
            getNewArena();
        }
        updateBattleStatus();
        if (readyForBattle) {
            arena.step();
            
        }
    }
    
    public void updateBattleStatus() {
        this.readyForBattle = true;        
        for (Object tmpO : this.getGoodGuys()) {
            N2ForceUnit tmpF = (N2ForceUnit) tmpO;
            if (tmpF.getUnitStatus() == N2Constants.UNIT_STATUS_MANEUVERING) {
                readyForBattle = false;
            }
        }
    }
    
    public void formTeam(Bag guys) {
        int num = guys.size();
        if (num < 2) {
            return;
        }
        for (int i1 = 0; i1 < num - 1; i1++) {
            N2ForceUnit g1 = (N2ForceUnit) guys.get(i1);
            for (int i2 = 0; i2 < num; i2++) {
                N2ForceUnit g2 = (N2ForceUnit) guys.get(i2);
                g1.setFriend(g2);
                g2.setFriend(g1);
            }
        }
    }
    
    public void getNewArena() {
        numGG = goodGuys.size();
        numBG = badGuys.size();
        arena = new N2MultiArena();
        formTeam(goodGuys);
        formTeam(badGuys);
        for (Object tmpO : goodGuys) {
            N2ForceUnit unit = (N2ForceUnit) tmpO;
            arena.addForce(unit);
        }
        for (Object tmpO : badGuys) {
            N2ForceUnit unit = (N2ForceUnit) tmpO;
            arena.addForce(unit);
        }
        for (Object tmpO : goodGuys) {
            N2ForceUnit unit = (N2ForceUnit) tmpO;
            for (Object tmpO2 : badGuys) {
                N2ForceUnit unit2 = (N2ForceUnit) tmpO2;
                unit.addFoe(unit2);
                unit2.addFoe(unit);
            }
        }
        
    }
    
    public Bag getGoodGuys() {
        return this.goodGuys;
    }
    
    public Bag getBadGuys() {
        return this.badGuys;
    }
    
    public void addGoodGuy(N2ForceUnit unit) {
        this.goodGuys.add(unit);
    }
    
    public void addBadGuy(N2ForceUnit unit) {
        this.badGuys.add(unit);
    }
    
    @Override
    public void stop() {
        this.stopper.stop();
    }
    
}
