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
public class DTimeStep {
    public double timeStep=0;
    public double f1Number=-1;
    public double f2Number=-1;
    public DTimeStep(double t,double n1,double n2){
        this.timeStep=t;
        this.f1Number=n1;
        this.f2Number=n2;
    }
    public String getCSV(){
        return this.timeStep+","+this.f1Number+","+this.f2Number;
    }
    public double[] getValues(){
        double[] ret = new double[3];
        ret[0]=timeStep;
        ret[1]=f1Number;
        ret[2]=f2Number;
        return ret;
    }
    
}
