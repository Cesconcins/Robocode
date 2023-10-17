package robot1;


import java.awt.Graphics2D;
import robocode.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import robocode.*;

/**
 *
 * @author usuario
 */
public class MyRobot1 extends TeamRobot {

    
    private double distanceorbit=100;
    private double directionorbit=1; // 1 rellotge -1 contrarellotge 
    private long timeorbit=0; //temps orbita
    
    
    private double enemicX=-1;
    private double enemicY=-1;
    private double enemicA=-1;
    
    @Override
    public void run() {
        turnLeft(getHeading());
        while(true) {
           
            execute();
        }
               /*
        gestionaRadar();
        gestionaGun();
        gestionaMoviment();
        updateState();
        finish();
        */
    }


    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        if(!isTeammate(e.getName())){
            double e_dis=e.getDistance();
            double e_bear=e.getBearing();

            double e_heading = getHeadingRadians() + Math.toRadians(e_bear);

            enemicX = getX() + Math.sin(e_heading) * e_dis;
            enemicY = getY() + Math.cos(e_heading) * e_bear;
            enemicA= e.getBearing();
           
            out.println("Enemic"+enemicX+enemicY+enemicA);

            orbit_fire(e.getName());
        }
    }
 
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
    turnLeft(180);
    } 
    
    
    
    private void orbit_fire(String name_enemy){
        
        if(!isTeammate(name_enemy)) {
            out.println("Començar orbitar!");
            
            if(getTime() - timeorbit >=2){
                directionorbit *=-1;
                timeorbit=getTime();
                fire(2);
            }
        }
        double angle_enemy= Math.toDegrees(Math.atan2(enemicX- getX(), enemicY - getY()));
        turnRight(angle_enemy +90*directionorbit);
        
        ahead(distanceorbit*directionorbit);
    }
    
    private void gestionaRadar() {
    }

    
    private void gestionaGun() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void gestionaMoviment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void updateState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void finish() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}