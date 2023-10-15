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

        while(true) {
            escanejarEnemics();
            
            setTurnRight(10000);
            setTurnGunRight(90);
            setAhead(2000);
            execute();
        }
               
    }
    
    public void onScannedRobot(ScannedRobotEvent e){
    //Funcio donada si sha escanejat un ROBOT
        
        //mirar si es Enemic
        if(isTeammate(e.getName())){
          out.println("Droide detectat!");
          turnRadarLeft(45);
           
        }
        else{
             out.println("ENemic detectat!"+teamLeaderName);

            double e_dis=e.getDistance();
            double e_bear=e.getBearing();

            double e_heading = getHeadingRadians() + Math.toRadians(e_bear);

                enemicX = getX() + Math.sin(e_heading) * e_dis;
                enemicY = getY() + Math.cos(e_heading) * e_bear;

            Coordenada enemic_trobat=new Coordenada(enemicX,enemicY);

            try {
                 out.println("mISSATGE ENVIAT LEADER");

                broadcastMessage(enemic_trobat);
            } catch (IOException ex) {
                out.println("mISSATGE NO ENVIAT!");

                Logger.getLogger(Leader.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    private void escanejarEnemics(){
    //Escanejar amb el radar enemics 
    
     turnRadarRight(45);
    }
    
    
   
}
