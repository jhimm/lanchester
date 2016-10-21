/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;
import java.util.Comparator;
/**
 *
 * @author Jeff
 */
public class N2NodeScoreComparator implements Comparator{
    @Override
    public int compare(Object o1,Object o2){
        N2NodeScore s1=(N2NodeScore)o1;
        N2NodeScore s2=(N2NodeScore)o2;
        if(s1.getScore()>s2.getScore()){
            return -1;
        }else if(s1.getScore()<s2.getScore()){
            return 1;
        }else{
            return 0;
        }
    }
}
