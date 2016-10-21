/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

import sim.portrayal.simple.FacetedPortrayal2D;
import sim.portrayal.SimplePortrayal2D;
import java.awt.*;
import sim.display.*;
import sim.util.*;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.ShapePortrayal2D;

/**
 *
 * @author Jeff
 */
public class N2FacetedPortrayal2D extends FacetedPortrayal2D {

    public boolean errorThrown = false;
//public SimplePortrayal2D[] kids = new SimplePortrayal2D[2];
    public SimplePortrayal2D[] kids = {new N2OvalPortrayal2D(), new N2ShapePortrayal2D(N2ShapePortrayal2D.X_POINTS_TRIANGLE_DOWN, N2ShapePortrayal2D.Y_POINTS_TRIANGLE_DOWN), new N2ShapePortrayal2D(N2ShapePortrayal2D.X_POINTS_TRIANGLE_UP, N2ShapePortrayal2D.Y_POINTS_TRIANGLE_UP), new N2ShapePortrayal2D(N2ShapePortrayal2D.X_POINTS_TRIANGLE_LEFT, N2ShapePortrayal2D.Y_POINTS_TRIANGLE_LEFT), new N2ShapePortrayal2D(N2ShapePortrayal2D.X_POINTS_TRIANGLE_RIGHT, N2ShapePortrayal2D.Y_POINTS_TRIANGLE_RIGHT)};

    public N2FacetedPortrayal2D() {

//    kids[0]=new OvalPortrayal2D();
//    kids[1]=new ShapePortrayal2D(ShapePortrayal2D.X_POINTS_TRIANGLE_DOWN ,ShapePortrayal2D.Y_POINTS_TRIANGLE_DOWN);
        super(null, false);
        this.children = kids;
    }

    public N2FacetedPortrayal2D(SimplePortrayal2D[] children, boolean portrayAllChildren) {
        super(children, portrayAllChildren);
    }

    /**
     * If child is null, then the underlying model object is presumed to be a
     * Portrayal2D and will be used.
     */
    public N2FacetedPortrayal2D(SimplePortrayal2D[] children) {
        super(children, false);
    }

    /**
     * Returns the child index to use for the given object. The value must be >=
     * 0 and < numIndices. The default implementation returns the value of the
     * object if it's a Number or is sim.util.Valuable and if the value is
     * within the given range (and is an integer). Otherwise 0 is returned.
     */
    @Override
    public int getChildIndex(Object object, int numIndices) {
        int element = 0;
//        System.out.println(" object is of type "+object.getClass().toString());
        if (object instanceof N2ForceUnit) {
            N2ForceUnit unit = (N2ForceUnit) object;
            element = unit.getUnitType();
            
//            System.out.println("Element = "+element+" for force unit "+unit.getName());
        } else if (object instanceof Number) {
//            System.out.println("Element = "+element+" for instanceof Number");
            
            element = (int) (((Number) object).doubleValue());
        } else if (object instanceof Valuable) {
//            System.out.println("Element = "+element+" for instanceof Valuable");
            element = (int) (((Valuable) object).doubleValue());
        }
        if (element < 0 || element >= children.length) {
            if (!errorThrown) {
                errorThrown = true;
                System.err.println("WARNING: FacetedPortrayal was given a value that doesn't correspond to any array element.");
            }
            element = 0;
        }
        return element;
    }

}
