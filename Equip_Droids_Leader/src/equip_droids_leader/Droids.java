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
    
    @Override
    public void run(){
        out.println("Droid preparat!");

        setBodyColor(Color.red);
        setGunColor(Color.red);

    }
    
    //Droid nomes pot rebre missatges
    
    @Override
    public void onMessageReceived(MessageEvent e){
        
         out.println("mISSATGE REBUT DROID!");

        Serializable missatge=e.getMessage();
        
        //mirar si missatge es una coordenada
        if(missatge instanceof Coordenada){
            Coordenada coo_enemic=(Coordenada) missatge;
            double eX=coo_enemic.getX();
            double eY=coo_enemic.getY();
            
            out.println("Droid rep coordenades ENEMIC"+eX+","+eY);
           
        }
        
    }
}
