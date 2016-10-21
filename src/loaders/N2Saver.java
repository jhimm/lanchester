/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package loaders;

import Utils.IOUtils;
import net2.N2ConfigData;
import com.cedarsoftware.util.io.*;
import net2.N2DataSet;
import net2.N2Nodes;
import net2.N2Node;
import net2.N2ForceUnit;
import net2.N2LinkData;
import net2.N2LinkInfo;
import java.io.*;
import java.util.ArrayList;
import sim.field.network.Edge;

/**
 *
 * @author Jeff
 */
public class N2Saver {

    public String fName = "";

    public N2Saver(String fName) {
        this.fName = fName;
    }

    public void saveNetwork(N2Nodes nodes) {
        N2DataSet data = new N2DataSet();
        for (Object tmpO : nodes.region.getAllObjects()) {
            if (tmpO.getClass().equals(N2Node.class)) {
                data.nodes.add((N2Node) tmpO);
            } else if (tmpO.getClass().equals(N2ForceUnit.class)) {
                data.forces.add((N2ForceUnit) tmpO);
            }

        }

        for (int i1 = 0; i1 < nodes.mgedges.length - 1; i1++) {
            for (int i2 = i1 + 1; i2 < nodes.mgedges[i1].length; i2++) {
                Edge tmpE = nodes.mgedges[i1][i2][0];
                if (tmpE != null) {
                    N2LinkData lData = new N2LinkData((N2LinkInfo) tmpE.getInfo());
                    data.links.add(lData);
                }
            }
        }
        data.cData.randomSeed=nodes.seed();
        String tmpJS = JsonWriter.objectToJson(data);
        IOUtils.string2file(new File(fName), tmpJS, Boolean.TRUE);
    }
}
