/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package lanchester;

import org.apache.commons.math3.linear.Array2DRowRealMatrix;
import org.apache.commons.math3.linear.EigenDecomposition;

/**
 *
 * @author Jeff
 */
public class TestEigen {

    static Array2DRowRealMatrix mat = null;
    static EigenDecomposition eigen = null;

    public static void main(String[] args) {
         mat = getMat(4);
//        fillMat(mat);
        eigen = new EigenDecomposition(mat);
        double det = eigen.getDeterminant();
        double[] eVals = eigen.getRealEigenvalues();

        if (eigen.hasComplexEigenvalues()) {
            System.out.println("Complex eigenvalues");
        }
        Array2DRowRealMatrix eVectors = (Array2DRowRealMatrix) eigen.getV();
        EigenDecomposition eigen2 = new EigenDecomposition(eVectors);
        double det2 = eigen2.getDeterminant();
        System.out.println("Det 1 = "+det + " and Det 2 = "+det2);
    }

    public static Array2DRowRealMatrix getMat(int num) {
        Array2DRowRealMatrix mat = new Array2DRowRealMatrix(num, num);
        for (int i1 = 0; i1 < num; i1++) {
            for (int i2 = 0; i2 < num; i2++) {
                mat.setEntry(i1, i2, Math.random());
            }
        }
        return mat;
    }
}
