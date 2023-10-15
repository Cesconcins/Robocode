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
                double eX=coo_enemic.getX();
                double eY=coo_enemic.getY();

                out.println("Droid rep coordenades ENEMIC Disparar"+eX+","+eY); 
            }
            
            else if(tipus_atac.contains("xoc_enemic")){
                double eX=coo_enemic.getX();
                double eY=coo_enemic.getY();
                
                out.println("Droid rep coordenades ENEMIC XOCAR"+eX+","+eY); 
                
            }
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
        
    }
    
    @Override
    public void onHitWall(HitWallEvent e){
        
    }
    
}
