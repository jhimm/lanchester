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
public class Lanchester {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Arena arena=null;
        if (args == null
                || args.length == 0) {
            Force blue = new Force("Blue", 1000);
            blue.setNumber(10000);
            blue.setCombatLosses(0.03);
            blue.setOtherLosses(0.03);
            blue.setReplenishment(250);
            Force red = new Force("Red", 2000);
            red.setNumber(5000);
            red.setCombatLosses(0.01);
            red.setOtherLosses(0.01);
            red.setReplenishment(25);
            arena = new Arena(red, blue);
            for (int i1 = 0; i1 < 25; i1++) {
                arena.step();
            }
        } else if (args.length == 1) {
            String fName=args[0];
            if(fName.endsWith(".csv")){
                
            }else{
                
            }
        }else if(args.length>=2){
            String fPath =args[0];
            String fName=args[1];
            if(fName.endsWith(".csv")){
                
            }else{
                
            }
            
        }
            ArrayList<String> outList = arena.getHistoryCSV();
            for (int i1 = 0; i1 < outList.size(); i1++) {
                System.out.println(outList.get(i1));
            }
// TODO code application logic here
        }

    }
