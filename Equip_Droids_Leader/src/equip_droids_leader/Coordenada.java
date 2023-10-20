/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package equip_droids_leader;
import java.io.Serializable;

/**
 *
 * @author usuario
 * Serialitzable per guardar les coordenades d'un robot
 * I el diferents tipus d'atac que es volen fer a aquest robot 
 */
public class Coordenada implements Serializable{
    private String tipus; //Tipus d'atac
    private double x;
    private double y;
    private double a; //angle si important 
    
    
     /*
    Constructora 
    @param - String t, double x,  double y,double a
    @return retorna la construccio d'una coordenada igual
    */
    public Coordenada(String t,double x, double y,double a){
        this.tipus=t;
        this.x =x;
        this.y =y;
        this.a=a;
    }
    
     /*
    Funcio get el tipus de missatge d'atac
    @param -
    @return retorna el tipus de missatge
    */
    public String getTipus(){
        return tipus;
    }
    
     /*
    Funcio get la coordenada x
    @param -
    @return retorna la coordenada X
    */
    public double getX(){
        return x;
    }
    
     /*
    Funcio get la coordenada Y
    @param -
    @return retorna la coordenada Y
    */
    public double getY(){
        return y;
    }
    
     /*
    Funcio get el tipus de l'angle del robot
    @param -
    @return retorna l'angle
    */
    public double getA(){
        return a;
    }
}
