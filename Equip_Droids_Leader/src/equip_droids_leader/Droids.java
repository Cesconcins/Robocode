/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equip_droids_leader;
import robocode.*;
import java.awt.*;
import java.io.IOException;
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
    private long   temps=0;

    
    public void run(){
        out.println("Droid preparat!");

        setBodyColor(Color.red);
        setGunColor(Color.red);
        while(true){
            ahead(2250);
            turnRight(45);
            ahead(2250);
            turnLeft(45);
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
        
        double angle = Math.toDegrees(Math.atan2(destiX - getX(), destiY - getY()));
        turnGunRight(angle-getGunHeading());
        
        fire(3);
        fire(3);

        
        destiX=-1;
        destiY=-1;
        
    }
    
    
    public void atac_raming(){
        
        if(destiX!=-1 && destiY!=-1){
            destiA= Math.toDegrees(Math.atan2(destiX - getX(), destiY - getY()));
            double distanciadesti = Math.hypot(destiX - getX(), destiY - getY());

            turnRight(destiA - getHeading());

            ahead(distanciadesti);
          
            if(distanciadesti>3){ //si lluny ram- fer voltes voltant d'un punt
                ram();
            }
            
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
        
        }
        else{
            Coordenada enemic_disparar=new Coordenada("disparar_enemic",getX(),getY(),e.getBearing());
            turnLeft(180);
            ahead(1000);
            try {
                out.println("Missatge enviat Droides-Coordenades");
                broadcastMessage(enemic_disparar);
                
            } catch (IOException ex) {
                out.println("Missatge no enviat a Droides!");
            }
            ram();
        }
    }
    
    @Override
    public void onHitWall(HitWallEvent e){
        turnLeft(180);
        ahead(1000);
        
        execute();   
    }
    
}
