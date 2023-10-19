/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equip_droids_leader;
import robocode.*;
import java.awt.*;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import static robocode.util.Utils.normalRelativeAngleDegrees;

/**
 *
 * @author usuario
 * Mes vida  q leader no tenen radar
 */
public class Droids extends TeamRobot implements Droid{
    
    //variables globals
    private final double fieldX=getBattleFieldWidth();
    private final double fieldY= getBattleFieldHeight();
    
    //Coordenades Enemics
    private double destiX=-1;
    private double destiY=-1;
    private double destiA=-1;
    double distance_enemy=-1;
    
    
    private long   temps=0;
    private double direction=1;   //direccio de la rotacio
    
    
    @Override
    public void run(){
        out.println("Droid preparat!");

        setBodyColor(Color.red);
        setGunColor(Color.red);
        
        setAdjustGunForRobotTurn(true);

        
        while(true){
            setAhead((distance_enemy /4 + 25) *direction);
          /*  if(xoc_paret(3)){
                direction*=-1;
            }
                  */      
            execute();
        }
    }
    
    //Droid nomes pot rebre missatges

    @Override
    public String getName(){
        return "DroidEquip";
    }
    
    //////////**EVENTS**///////////////

    
    @Override
    public void onMessageReceived(MessageEvent e){
        
        out.println("MISSATGE REBUT DROID!");

        Serializable missatge=e.getMessage();        
       
        //mirar si missatge es una coordenada
        if(missatge instanceof Coordenada){
           Coordenada coo_enemic=(Coordenada) missatge;
           
            String tipus_atac;
            
            tipus_atac = ((Coordenada) missatge).getTipus();
            
            if (tipus_atac.contains("disparar_enemic")){
                destiX=coo_enemic.getX();
                destiY=coo_enemic.getY();

                destiA= Math.toDegrees(Math.atan2(destiX - getX(), destiY - getY()));
                
                out.println("Droid rep coordenades ENEMIC Disparar"+destiX+","+destiY+", ANGLE: "+destiA); 
                disparar_enemic();
            }
            
            else if(tipus_atac.contains("xoc_enemic")){
                destiX=coo_enemic.getX();
                destiY=coo_enemic.getY();
                destiA= Math.toDegrees(Math.atan2(destiX - getX(), destiY - getY()));

                out.println("Droid rep coordenades ENEMIC XOCAR"+destiX+","+destiY); 
                atac_raming();             
            }
        }
        
        
    }
    
    public void disparar_enemic(){
        
        double angle = Math.toDegrees(Math.atan2(destiX - getX(), destiY - getY()));
        turnGunRight(angle-getGunHeading());
        
        fire(3);
        fire(3);

        
        destiX=-1;
        destiY=-1;
        
    }
    
    
    public void atac_raming(){
        
        if(destiX!=-1 && destiY!=-1){
            double distanciadesti = Math.hypot(destiX - getX(), destiY - getY());

            turnRight(destiA - getHeading());

            ahead(distanciadesti);
          
            /*if(distanciadesti>3){ //si lluny ram- fer voltes voltant d'un punt
                ram();
            }*/
            
        }
    }
    
    //girar 5 vegades en el punt desti donat- intentar matar enemic
    public void ram(){
        double angle = Math.toDegrees(Math.atan2(destiX - getX(), destiY - getY()));
        turnRight(angle-getHeading());

        if (temps==0){
            temps=getTime();
            }
        ahead(20);
        if(getTime()-temps >=3){
            destiX=-1;
            destiY=-1;
            temps=0;
        
        }
        
    }
    
    @Override
    public void onHitByBullet(HitByBulletEvent e){
        
    }
    
    @Override
    public void onBulletHit(BulletHitEvent e){
        
    }
    
    @Override
    public void onHitRobot(HitRobotEvent e){
        String nom=e.getName();
        if(nom.startsWith("equip_droids_leader.Droids") || nom.startsWith("equip_droids_leader.Leader")){
          turnRight(90);
          ahead(100);
          direction *=-1;
        }
        else{
            Coordenada enemic_disparar=new Coordenada("disparar_enemic",getX(),getY(),e.getBearing());
            turnLeft(180);
            ahead(1000);
            try {
                out.println("Missatge enviat TOTS-Coordenades");
                broadcastMessage(enemic_disparar);
                
            } catch (IOException ex) {
                out.println("Missatge no enviat a tots!");
            }
            ram();
        }
    }
    
    @Override
    public void onHitWall(HitWallEvent e){
    if (direction == -1 && Math.abs(e.getBearing()) >= 160.0) {
      direction = 1;
    } else if (direction == 1 && Math.abs(e.getBearing()) <= 20.0) {
      direction = -1;
    } else {
      if (direction == 1) {
        setTurnRight(normalRelativeAngleDegrees(e.getBearing()));
        direction = -1;
      } else {
        setTurnRight(normalRelativeAngleDegrees(e.getBearing()+180));
        direction = 1;
      }
    }
    }
    
    boolean xoc_paret(double r){
        double maxX=getX() + getHeight()*r;
        double minX=getX() - getHeight()*r;
        double maxY=getY() + getHeight()*r;
        double minY=getY() - getHeight()*r;
        
        boolean outX= maxX >= fieldX || minX <= 0.0;
        boolean outY= maxY >= fieldY || minY <= 0.0;

       return outX||outY;

    }
}
