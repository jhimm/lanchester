/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

import java.awt.Color;
import java.awt.Graphics2D;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.OvalPortrayal2D;

/**
 *
 * @author Jeff
 */
public class N2OvalPortrayal2D extends OvalPortrayal2D {

    @Override
    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        N2Node village = (N2Node) object;
        double currStab = .5;
//        int agitationShade = (int) (currStab * 255);
//        if (agitationShade > 255) {
//            agitationShade = 255;
//        }
        info.draw.width = 25. * currStab;
        info.draw.height = 25. * currStab;
        if (village.getControllingFaction() == N2Constants.UNCONTROLLED) {
            paint = Color.GRAY;
        } else if (village.getControllingFaction() == N2Constants.GOOD_GUYS) {
            paint = Color.GREEN;
        } else if (village.getControllingFaction() == N2Constants.BAD_GUYS) {
            paint = Color.RED;
        } else if (village.getControllingFaction() == N2Constants.NEUTRAL_GUYS) {
            paint = Color.BLUE;
        }else{
            paint=Color.YELLOW;
        }
//        if (village.nodeType == N2Constants.POPULATION_CENTER_TYPE) {
        super.draw(object, graphics, info);
//        } else {
//        }
    }

}
