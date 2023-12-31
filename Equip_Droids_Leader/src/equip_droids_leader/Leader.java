/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equip_droids_leader;
import robocode.*;
import java.awt.*;
import java.io.IOException;
import robocode.MessageEvent;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.awt.Color;


/**
 *
 * @author usuario
 */
public class Leader extends TeamRobot{
    
    //inicialitzar coordenades enemic valors invalids
    double enemicX=-1;
    double enemicY=-1;
    double enemicA=-1;
    private Coordenada coo_enemic;
    
    protected String teamLeaderName="LIDER1";
    
            
    public void run() {
        
        turnLeft(getHeading());
       
        turnRadarRight(360);  //iniciar radar
        
        setBodyColor(Color.yellow);
        setGunColor(Color.yellow);
        setRadarColor(Color.green); 

        out.println("Lider preparat!"+teamLeaderName);

        llista_equip();
        
        while(true) {
            escanejarEnemics();
            
            setTurnRight(10000);
            setTurnGunRight(90);
            setAhead(2000);
            execute();
        }
               
    }
    
    //////////**EVENTS**///////////////
    
    
    private void escanejarEnemics(){
    //Escanejar amb el radar enemics 
    
     turnRadarRight(90);
    }
    
    /*
    Funcio quan un robot leader escaneja un robot
    @param ScannedRobotEvent e - escaneja un robot
    @ return envia un tipus de missatge segons on es trobi el 
    robot escanejat 
    */
    @Override
    public void onScannedRobot(ScannedRobotEvent e){
    //Funcio donada si sha escanejat un ROBOT
        String nom=e.getName();
        //mirar si es Enemic o Teammate
        
        if(nom.startsWith("equip_droids_leader.Droids")) {
         
          out.println("Droide detectat!"+e.getName());
          turnRadarLeft(45);
           
        }
        
        else{
            out.println("ENEMIC detectat!"+e.getName());

            double e_dis=e.getDistance();
            double e_bear=e.getBearing();

            double e_heading = getHeadingRadians() + Math.toRadians(e_bear);

                enemicX = getX() + Math.sin(e_heading) * e_dis;
                enemicY = getY() + Math.cos(e_heading) * e_bear;
                enemicA= e.getBearing();

            Coordenada enemic_disparar=new Coordenada("disparar_enemic",enemicX,enemicY,enemicA);

            try {
                out.println("Missatge enviat Droides-Coordenades");
                broadcastMessage(enemic_disparar);
                
            } catch (IOException ex) {
                out.println("Missatge no enviat a Droides!");
            }
        }
    }
    
    /*
    Funcio quan un robot leader es tocat per una bala
    @param HitByBulletEvent e - li toca una bala 
    */
    @Override
    public void onHitByBullet(HitByBulletEvent e){
        turnLeft(90+e.getBearing());
        ahead(1000);
    }
    
    /*
    Funcio quan un robot leader toca un altre robot amb una bala
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
    Funcio quan un robot lider xoca toca un altre robot amb una bala
    @param HitRobotEvent e - toca robot
    */
    @Override
    public void onHitRobot(HitRobotEvent e){
        String nom=e.getName();
        //mirar si es Enemic o Teammate
        if(nom.startsWith("equip_droids_leader.Droids")) {
         
          out.println("Xocat amb Droide!"+e.getName());
          turnRight(180+e.getBearing());    
        }
        else{
                enemicX = getX(); 
                enemicY = getY();
                enemicA = e.getBearing();
            Coordenada enemic_xoc=new Coordenada("xoc_enemic",enemicX,enemicY,enemicA);

            try {
                out.println("Missatge enviat Droides-Coordenades-XOCAR");
                broadcastMessage(enemic_xoc);
                
            } catch (IOException ex) {
                out.println("Missatge no enviat a Droides!");
            }

        }
        
    }
    
    /*
    Funcio quan un robot leader toca una paret
    @param HitWallEvent e - toca paret
    */
    @Override
    public void onHitWall(HitWallEvent e){
        turnRight(90);
        ahead(50);
        
        execute();        
    }
    
   
    /*
    Funcio llista del equip
    @param -
    @return posa per pantalla la llista dels robots del equip
    */
    public void llista_equip(){
        String[] t= getTeammates();

        if (t.length > 0) {
            out.println("List of Teammates:");
            for (String teammate : t) {
                out.println(teammate);
            }
        } else {
            out.println("No teammates detected.");
        }
    }
    
 
}