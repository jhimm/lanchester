/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import java.awt.Color;
import java.awt.Graphics2D;
import net2.N2Constants;
import net2.N2ForceUnit;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.simple.ShapePortrayal2D;

/**
 *
 * @author Jeff
 */
public class N2ShapePortrayal2D extends ShapePortrayal2D {

    public N2ShapePortrayal2D(double[] xs, double[] ys) {
        super(xs, ys);
    }

    @Override
    public void draw(Object object, Graphics2D graphics, DrawInfo2D info) {
        N2ForceUnit unit = (N2ForceUnit) object;
        if (unit.getFaction() == N2Constants.GOOD_GUYS) {
            paint = Color.GREEN;
        } else if (unit.getFaction() == N2Constants.UNCONTROLLED) {
            paint = Color.GRAY;
        } else if (unit.getFaction() == N2Constants.BAD_GUYS) {
            paint = Color.RED;
        } else if (unit.getFaction() == N2Constants.NEUTRAL_GUYS) {
            paint = Color.BLUE;
        } else {
            paint = Color.YELLOW;
        }
        super.draw(object, graphics, info);
    }
}
