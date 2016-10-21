/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package gui;

import sim.portrayal.network.SimpleEdgePortrayal2D;
import sim.portrayal.DrawInfo2D;
import sim.field.network.Edge;
import java.awt.Color;
import java.io.Serializable;
import net2.N2Constants;
import net2.N2Node;

/**
 *
 * @author jhimm
 */
public class N2LinkPortrayal extends SimpleEdgePortrayal2D implements Serializable {

    public N2LinkPortrayal() {
        super();
    }

    @Override
    public void draw(java.lang.Object object, java.awt.Graphics2D graphics, DrawInfo2D info) {
        Edge edge = (Edge) object;
//        N2LinkInfo edgeInfo = (N2LinkInfo) edge.getInfo();
//        if(edgeInfo.isAsymmetric){
//            this.shape=SHAPE_TRIANGLE;
//        }
        int side1 = ((N2Node) edge.getFrom()).getControllingFaction();
        int side2 = ((N2Node) edge.getTo()).getControllingFaction();
        fromPaint = N2Constants.LINK_COLORS[side1][side2];
        toPaint = N2Constants.LINK_COLORS[side2][side1];
//        fromPaint = Color.white;
//        toPaint = Color.white;
//        int wt = edgeInfo.getWeight();
//        if (wt == 1) {
////            eColor = Color.green;
//            fromPaint = Color.green;
//            toPaint = Color.green;
//        } else if (wt == -1) {
////            eColor = Color.red;
//            fromPaint = Color.orange;
//            toPaint = Color.orange;
//        }else if(wt==2){
//            fromPaint = Color.blue;
//            toPaint = Color.blue;
//        }else if(wt==-2){
//            fromPaint = Color.red;
//            toPaint = Color.red;
//        }
//        if(Math.abs(wt)>1){
//            this.baseWidth=25.;
//            setAdjustThickness(true);
//        }
        super.draw(object, graphics, info);
    }
}
