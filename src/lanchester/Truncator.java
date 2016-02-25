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
public class Truncator {
    public double precision=0.1;
    public Truncator(){
        
    }
    public double truncate(double d1){
        double trunc;
        int tmpV= (int)(d1/this.precision);
        trunc = tmpV*this.precision;
        return trunc;
    }
    public double truncate(double d2,double prec){
        this.precision = prec;
        return truncate(d2);
    }
}
