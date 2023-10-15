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
    private String tipus; //Tipus d'atac
    private double x;
    private double y;
    
    public Coordenada(String t,double x, double y){
        this.tipus=t;
        this.x =x;
        this.y =y;
    }
    
    public String getTipus(){
        return tipus;
    }
    public double getX(){
        return x;
    }
    
    public double getY(){
        return y;
    }
}
