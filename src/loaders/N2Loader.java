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
import java.io.*;
import java.util.ArrayList;

/**
 *
 * @author Jeff
 */
public class N2Loader {

    N2DataSet data = null;
    N2ConfigData cData = null;

    public N2Loader() {
//        JsonReader reader = new JsonReader();
    }
    public void load(String fName) {
        ArrayList<String> sList = IOUtils.file2ArrayList(new File(fName));
        StringBuilder builder = new StringBuilder();
        for(String tmpS:sList){
            builder.append(tmpS);
        }
        
        N2DataSet inData = (N2DataSet) JsonReader.jsonToJava(builder.toString());    
        N2Nodes nodes = new N2Nodes(inData.cData.randomSeed);
        nodes.setData(inData);
    }
}
