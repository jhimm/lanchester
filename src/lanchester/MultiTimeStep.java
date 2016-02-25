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
public class MultiTimeStep {
  public double[] numList = null;
  public double time=0.;
  public MultiTimeStep(int num){
      this.numList=new double[num];
  }
  public void setTime(double t){
      this.time=t;
  }
  public void setForceNumber(double num,int i){
      this.numList[i]=num;
  }
  public void setForceNumbers(double[] nums){
      this.numList=nums;
  }
  public String getTimeStep(){
      String tmpS="time = "+this.time;
      for(int i1=0;i1<this.numList.length;i1++){
          tmpS+=" "+this.numList[i1]+" ";
      }
      return tmpS;
  }
}
