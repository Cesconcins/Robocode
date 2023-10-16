/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equip_droids_leader;
import robocode.*;
import java.awt.*;
import java.io.Serializable;


/**
 *
 * @author usuario
 * Mes vida  q leader no tenen radar
 */
public class Droids extends TeamRobot implements Droid{
    
    //variables globals
    private double destiX=-1;
    private double destiY=-1;
    private double destiA=-1;
    
    
    public void run(){
        out.println("Droid preparat!");

        setBodyColor(Color.red);
        setGunColor(Color.red);
        while(true){
        
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
                destiA=coo_enemic.getA();
                
                out.println("Droid rep coordenades ENEMIC Disparar"+destiX+","+destiY+", ANGLE: "+destiA); 
                disparar_enemic();
            }
            
            else if(tipus_atac.contains("xoc_enemic")){
                destiX=coo_enemic.getX();
                destiY=coo_enemic.getY();
                
                out.println("Droid rep coordenades ENEMIC XOCAR"+destiX+","+destiY); 
                atac_raming();             
            }
        }
        
        
    }
    
    public void disparar_enemic(){
        
        
    }
    
    
    public void atac_raming(){
        
        if(destiX!=-1 && destiY!=-1){
            destiA= Math.toDegrees(Math.atan2(destiX - getX(), destiY - getY()));
            double distanciadesti = Math.hypot(destiX - getX(), destiY - getY());

            turnRight(destiA - getHeading());

            ahead(distanciadesti);
          
            if(distanciadesti<2){
                ram();
            }
            
        }
    }
    
    //girar 5 vegades en el punt desti donat- intentar matar enemic
    public void ram(){
        
        
    }
    
    @Override
    public void onHitByBullet(HitByBulletEvent e){
        
    }
    
    @Override
    public void onBulletHit(BulletHitEvent e){
        
    }
    
    @Override
    public void onHitRobot(HitRobotEvent e){
        destiX=getX();
        destiY=getY();
        
        ram();
    }
    
    @Override
    public void onHitWall(HitWallEvent e){
        turnRight(90);
        ahead(100);
        
        execute();   
    }
    
}
