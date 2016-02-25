/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchester;

/**
 *
 * @author Jeff
 */
public class TimeStep {
    public int timeStep=0;
    public int f1Number=-1;
    public int f2Number=-1;
    public TimeStep(int t,int n1,int n2){
        this.timeStep=t;
        this.f1Number=n1;
        this.f2Number=n2;
    }
    public String getCSV(){
        return this.timeStep+","+this.f1Number+","+this.f2Number;
    }
}
