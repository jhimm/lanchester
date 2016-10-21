/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loaders;
import java.util.ArrayList;
import net2.N2ForceUnit;
import net2.N2LinkData;
import net2.N2Node;

/**
 *
 * @author Jeff
 */
public class N2DataSet {
    public ArrayList<N2ForceUnit> forces=null;
    public ArrayList<N2Node> nodes = null;
    public ArrayList<N2LinkData> links = null;
    public N2ConfigData cData=null;
}
