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
public class AthenaConstants {

    public static final int ATTACK_POSTURE = 0;
    public static final int DEFEND_POSTURE = 1;
    public static final int WITHDRAW_POSTURE = 2;
    public static final int ALLIED_POSTURE = 3;
    public static final double ATTACK_ATTACK_ATTRITION = 0.1;
    public static final double ATTACK_DEFEND_ATTRITION = 0.2;
    public static final double ATTACK_WITHDRAW_ATTRITION = 0.0125;
    public static final double ATTACK_ALLIED_ATTRITION = 0.0;
    public static final double DEFEND_ATTACK_ATTRITION = 0.05;
    public static final double DEFEND_DEFEND_ATTRITION = 0.01;
    public static final double DEFEND_WITHDRAW_ATTRITION = 0.01;
    public static final double DEFEND_ALLIED_ATTRITION = 0.0;
    public static final double WITHDRAW_ATTACK_ATTRITION = 0.2;
    public static final double WITHDRAW_DEFEND_ATTRITION = 0.01;
    public static final double WITHDRAW_WITHDRAW_ATTRITION = 0.001;
    public static final double WITHDRAW_ALLIED_ATTRITION = 0.0;
    public static final double ALLIED_ALLIED_ATTRITION = 0.00;
    public static final double ALLIED_ATTACK_ATTRITION = 0.00;
    public static final double ALLIED_DEFEND_ATTRITION = 0.00;
    public static final double ALLIED_WITHDRAW_ATTRITION = 0.00;
    public static final double SKIRMISH_ATTACK_ATTACK_ATTRITION = 0.05;
    public static final double SKIRMISH_ATTACK_DEFEND_ATTRITION = 0.1;
    public static final double SKIRMISH_ATTACK_WITHDRAW_ATTRITION = 0.05;
    public static final double SKIRMISH_ATTACK_ALLIED_ATTRITION = 0.0;
    public static final double SKIRMISH_DEFEND_ATTACK_ATTRITION = 0.025;
    public static final double SKIRMISH_DEFEND_DEFEND_ATTRITION = 0.001;
    public static final double SKIRMISH_DEFEND_WITHDRAW_ATTRITION = 0.001;
    public static final double SKIRMISH_DEFEND_ALLIED_ATTRITION = 0.0;
    public static final double SKIRMISH_WITHDRAW_ATTACK_ATTRITION = 0.01;
    public static final double SKIRMISH_WITHDRAW_DEFEND_ATTRITION = 0.001;
    public static final double SKIRMISH_WITHDRAW_WITHDRAW_ATTRITION = 0.001;
    public static final double SKIRMISH_WITHDRAW_ALLIED_ATTRITION = 0.0;
    public static final double SKIRMISH_ALLIED_ALLIED_ATTRITION = 0.00;
    public static final double SKIRMISH_ALLIED_ATTACK_ATTRITION = 0.00;
    public static final double SKIRMISH_ALLIED_DEFEND_ATTRITION = 0.00;
    public static final double SKIRMISH_ALLIED_WITHDRAW_ATTRITION = 0.00;
    public static final String EQUIPMENT = "EQUIPMENT";
    public static final String TRAINING = "TRAINING";
    public static final String DEMEANOR = "DEMEANOR";
    public static final String URBAN = "URBAN";
    public static final String CIVILIAN_CONCERN = "CIVILIAN_CONCERN";
    public static final String FORCE_TYPE = "FORCE_TYPE";
     public static final String INTEL = "INTEL";
    public static double[][] skirmish = new double[4][4];
    public static double[][] battle = new double[4][4];

    public AthenaConstants() {

    }

    public static void fillArrays() {
        battle[0][0] = ATTACK_ATTACK_ATTRITION;
        battle[0][1] = ATTACK_DEFEND_ATTRITION;
        battle[0][2] = ATTACK_WITHDRAW_ATTRITION;
        battle[0][3] = ATTACK_ALLIED_ATTRITION;
        battle[1][0] = DEFEND_ATTACK_ATTRITION;
        battle[1][1] = DEFEND_DEFEND_ATTRITION;
        battle[1][2] = DEFEND_WITHDRAW_ATTRITION;
        battle[1][3] = DEFEND_ALLIED_ATTRITION;
        battle[2][0] = WITHDRAW_ATTACK_ATTRITION;
        battle[2][1] = WITHDRAW_DEFEND_ATTRITION;
        battle[2][2] = WITHDRAW_WITHDRAW_ATTRITION;
        battle[2][3] = WITHDRAW_ALLIED_ATTRITION;
        battle[3][0] = ALLIED_ATTACK_ATTRITION;
        battle[3][1] = ALLIED_DEFEND_ATTRITION;
        battle[3][2] = ALLIED_WITHDRAW_ATTRITION;
        battle[3][3] = ALLIED_ALLIED_ATTRITION;
        skirmish[0][0] = SKIRMISH_ATTACK_ATTACK_ATTRITION;
        skirmish[0][1] = SKIRMISH_ATTACK_DEFEND_ATTRITION;
        skirmish[0][2] = SKIRMISH_ATTACK_WITHDRAW_ATTRITION;
        skirmish[0][3] = SKIRMISH_ATTACK_ALLIED_ATTRITION;
        skirmish[1][0] = SKIRMISH_DEFEND_ATTACK_ATTRITION;
        skirmish[1][1] = SKIRMISH_DEFEND_DEFEND_ATTRITION;
        skirmish[1][2] = SKIRMISH_DEFEND_WITHDRAW_ATTRITION;
        skirmish[1][3] = SKIRMISH_DEFEND_ALLIED_ATTRITION;
        skirmish[2][0] = SKIRMISH_WITHDRAW_ATTACK_ATTRITION;
        skirmish[2][1] = SKIRMISH_WITHDRAW_DEFEND_ATTRITION;
        skirmish[2][2] = SKIRMISH_WITHDRAW_WITHDRAW_ATTRITION;
        skirmish[2][3] = SKIRMISH_WITHDRAW_ALLIED_ATTRITION;
        skirmish[3][0] = SKIRMISH_ALLIED_ATTACK_ATTRITION;
        skirmish[3][1] = SKIRMISH_ALLIED_DEFEND_ATTRITION;
        skirmish[3][2] = SKIRMISH_ALLIED_WITHDRAW_ATTRITION;
        skirmish[3][3] = SKIRMISH_ALLIED_ALLIED_ATTRITION;
    }
}
