/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package equip_droids_leader;
import robocode.*;
/**
 *
 * @author usuario
 */

public class Equip_Droids_Leader extends AdvancedRobot {

     @Override
    public void run() {
        turnLeft(getHeading());
        while(true) {
            setTurnRight(10000);
            setTurnGunRight(90);
            setAhead(2000);
            execute();
        }
    }
        /*
    public static void main(String[] args) {
        // TODO code application logic here
    }*/
    
}