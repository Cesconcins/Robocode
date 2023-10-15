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

            Coordenada enemic_trobat=new Coordenada(enemicX,enemicY);

            try {
                 out.println("Missatge enviat Droides-Coordenades");

                broadcastMessage(enemic_trobat);
            } catch (IOException ex) {
                out.println("Missatge no enviat a Droides!");
            }
        }
    }
    
    private void escanejarEnemics(){
    //Escanejar amb el radar enemics 
    
     turnRadarRight(45);
    }
    
    
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
