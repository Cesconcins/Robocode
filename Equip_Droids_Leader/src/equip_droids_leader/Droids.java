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
 *
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

    /*
    Funcio nom droid
    @param -
    @return el nom del droid- noms iguals per cada droid del equip
    */
    @Override
    public String getName(){
        return "DroidEquip";
    }
    
    //////////**EVENTS**///////////////

    
    /*
    Funcio quan un robot droid rep un missatge
    executar diferents funcions depenen del missatge
    @param MessageEvent e - rep un missatge 
    */
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
     /*
    Funcio per disparar un enemic
    @param -
    */
    public void disparar_enemic(){
        
        double angle = Math.toDegrees(Math.atan2(destiX - getX(), destiY - getY()));
        turnGunRight(angle-getGunHeading());
        
        fire(3);
        fire(3);

        
        destiX=-1;
        destiY=-1;
        
    }
    
    /*
    Funcio d'atacar un enemic de forma raming 
    @param -
    */
    public void atac_raming(){
        
        if(destiX!=-1 && destiY!=-1){
            double distanciadesti = Math.hypot(destiX - getX(), destiY - getY());

            turnRight(destiA - getHeading());

            ahead(distanciadesti);
          
            if(distanciadesti>3){ //si lluny ram- fer voltes voltant d'un punt
                ram();
            }
            
        }
    }
    
    /*
    Funcio per atacar de forma ramming
    @param -
    */
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
    
    /*
    Funcio quan un robot droid es tocat per una bala
    @param HitByBulletEvent e - li toca una bala 
    */
    @Override
    public void onHitByBullet(HitByBulletEvent e){
        turnLeft(90+e.getBearing());
        ahead(1000);
    }
    
    /*
    Funcio quan un robot droid toca un altre robot amb una bala
    @param BulletHitEvent e - toca una la seva bala a un robot
    */
    @Override
    public void onBulletHit(BulletHitEvent e){
        String nom = e.getName();
    
        if (nom.startsWith("equip_droids_leader.Droids")) {
        out.println("Teammate " + nom + " disparat.");
        } 
        else {
        out.println("Enemic " + nom + " disparat.");
        }
    }
    
    /*
    Funcio quan un robot droid xoca toca un altre robot amb una bala
    @param HitRobotEvent e - toca robot
    */
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
                out.println("Missatge enviat TOTS-Coordenades");
                broadcastMessage(enemic_disparar);
                
            } catch (IOException ex) {
                out.println("Missatge no enviat a tots!");
            }
            ram();
        }
    }
    
    /*
    Funcio quan un robot droid toca una paret
    @param HitWallEvent e - toca paret
    */
    @Override
    public void onHitWall(HitWallEvent e){
        turnLeft(180);
        ahead(1000);
        
        execute();   
    }
    
}