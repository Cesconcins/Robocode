/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equip_droids_leader;
import java.io.Serializable;

/**
 *
 * @author usuario
 */
public class Coordenada implements Serializable{
    
    private double x;
    private double y;
    
    public Coordenada(double x, double y){
        this.x =x;
        this.y =y;
    }
    
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
}
