/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package net2;

//import network.*;
import java.io.Serializable;

/**
 *
 * @author jhimm
 */
public class N2LinkInfo implements Serializable{

    public int numCategories = 1;
    public int[] wts = new int[numCategories];
    public int wt = 0;
    String[] categories = new String[numCategories];
    public N2Node from = null;
    public N2Node to = null;
    public static boolean isAsymmetric =false;
    public java.awt.Color linkColor = java.awt.Color.BLACK;
    public double[] transitCosts={1.0,3.0,8.0};

    public N2LinkInfo(int weight) {
        this.wt = weight;
    }

    public void setWeight(int weight) {
        this.wt = weight;
    }

    public int getWeight() {
        return this.wt;
    }

    public void setNumCategories(int num) {
        this.numCategories = num;
        this.wts = new int[numCategories];
        this.categories = new String[numCategories];
    }

    public int[] getWeights() {
        return wts;
    }

    public void setWeights(int[] wtz) {
        int num = wtz.length;
        if (num != numCategories) {
            numCategories = num;
            this.wts = new int[num];
        }
        for (int i1 = 0; i1 < num; i1++) {
            this.wts[i1] = wtz[i1];
        }

    }

    public boolean setWeight(int wt, int ind) {
        if (ind < this.numCategories && ind >= 0) {
            this.wts[ind] = wt;
            return true;
        } else {
            return false;
        }
    }

    public int getWeight(int ind) {
        if (ind >= numCategories || ind < 0) {
            System.out.println(" index requested is out of bounds for weight array");
            return -999;
        }
        return this.wts[ind];
    }

    public void addCategory(int wt, String catName) {
        int newNum = this.numCategories + 1;
        String[] newCats = new String[newNum];
        int[] newWts = new int[newNum];
        for (int i1 = 0; i1 < this.numCategories; i1++) {
            newCats[i1] = this.categories[i1];
            newWts[i1] = this.wts[i1];
        }
        newCats[this.numCategories] = catName;
        newWts[this.numCategories] = wt;
        this.numCategories = newNum;
        this.wts = newWts;
        this.categories = newCats;
    }

    public String getCategoryName(int ind) {
        if (ind > this.numCategories || ind < 0) {
            return null;
        }
        return this.categories[ind];
    }

    public String[] getCategoryNames() {
        return this.categories;
    }
    public static void setIsAsymmetric(boolean ia){
        isAsymmetric=ia;
    }
    public static boolean getIsAsymmetric(){
        return isAsymmetric;
    }
    public void setFrom(Object o){
        this.from = (N2Node)o;
    } 
    public void setTo(Object o){
        this.to = (N2Node)o;
    }
    public void setLinkColor(java.awt.Color col){
        this.linkColor=col;
    }
    public java.awt.Color getLinkColor(){
        return this.linkColor;
    }
}
