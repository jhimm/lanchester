/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

import java.awt.Color;
import java.awt.Graphics2D;
import javax.swing.JFrame;
import sim.display.Console;
import sim.display.Controller;
import sim.display.GUIState;
import sim.engine.SimState;
//import sim.field.network.Edge;
import sim.portrayal.DrawInfo2D;
import sim.portrayal.Inspector;
import sim.portrayal.continuous.ContinuousPortrayal2D;
import sim.portrayal.network.NetworkPortrayal2D;
//import sim.portrayal.network.SimpleEdgePortrayal2D;
import sim.portrayal.network.SpatialNetwork2D;
import sim.portrayal.simple.CircledPortrayal2D;
import sim.portrayal.simple.LabelledPortrayal2D;
import sim.portrayal.simple.MovablePortrayal2D;
import sim.portrayal.simple.OvalPortrayal2D;
import sim.portrayal.simple.ShapePortrayal2D;
import sim.display.Display2D;

public class N2NodesWithUI extends GUIState {

    public Display2D display;
    public JFrame displayFrame;
    ContinuousPortrayal2D regionPortrayal = new ContinuousPortrayal2D();
    NetworkPortrayal2D neighborPortrayal = new NetworkPortrayal2D();
    public static Console c = null;

    public static void main(String[] args) {
        N2NodesWithUI vid = new N2NodesWithUI();
//        Console c = new Console(vid);
        c = new Console(vid);
        c.setVisible(true);
    }

    public N2NodesWithUI() {
        super(new N2Nodes(System.currentTimeMillis()));
    }

    public N2NodesWithUI(SimState state) {
        super(state);
    }

    public static String getName() {
        return "Actors";
    }

    @Override
    public Object getSimulationInspectedObject() {
        return state;
    }

    @Override
    public Inspector getInspector() {
        Inspector i = super.getInspector();
        i.setVolatile(true);
        return i;
    }

    @Override
    public void start() {
        super.start();
        setupPortrayals();
    }

    @Override
    public void load(SimState state) {
        super.load(state);
        N2Nodes tmpVs = (N2Nodes) state;
//        if (tmpVs.resetSeed) {
//            tmpVs.random.setSeed(System.currentTimeMillis());
//        }
        setupPortrayals();
    }

    public void setupPortrayals() {
        N2Nodes nodes = (N2Nodes) state;
//        if(nodes.asymmetricLinks){
//            LinkInfo.setIsAsymmetric(true);
//        }
// tell the portrayals what to portray and how to portray them
        regionPortrayal.setField(nodes.region);
        regionPortrayal.setPortrayalForAll(
                new MovablePortrayal2D(
                        new CircledPortrayal2D(
                                new LabelledPortrayal2D(new N2FacetedPortrayal2D() {
                                },
                                        2.0, 2.0, 0.0, 0.0, new java.awt.Font("SansSerif", java.awt.Font.PLAIN, 10), LabelledPortrayal2D.ALIGN_LEFT, null, Color.black, false),
                                0, 5.0, Color.green, true)));
        neighborPortrayal.setField(new SpatialNetwork2D(nodes.region, nodes.neighborhood));
        neighborPortrayal.setPortrayalForAll(new N2LinkPortrayal()
        );
// reschedule the displayer
//        System.out.println(" about to reset display");
        display.reset();
        display.setBackdrop(Color.white);
// redraw the display
        display.repaint();
    }

    @Override
    public void init(Controller c) {
//        System.out.println("About to initialize GuiState with Controller c");
        super.init(c);
//        System.out.println("Finished initializing GuiState with Controller c");
        display = new Display2D(600, 600, this);
//        System.out.println("Instantiated display");
        display.setClipping(false);
//        System.out.println("Created display");
        displayFrame = display.createFrame();
        displayFrame.setTitle("Actor map");
        c.registerFrame(displayFrame); // so the frame appears in the "Display" list
        displayFrame.setVisible(true);
        display.attach(neighborPortrayal, "Neighborhood");
        display.attach(regionPortrayal, "Region");

    }

    @Override
    public void quit() {
        super.quit();
        if (displayFrame != null) {
            displayFrame.dispose();
        }

        displayFrame = null;
        display = null;
    }
}
