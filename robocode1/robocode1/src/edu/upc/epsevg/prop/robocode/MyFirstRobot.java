package edu.upc.epsevg.prop.robocode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Timer;
import java.util.TimerTask;
import robocode.*;


/**
 *
 * @author francesco
 */

public class MyFirstRobot extends TeamRobot{
    private final Map<String, List<Double>> enemies_distances;
    //contindrà <nom> <array de distàncies> de cada enemy
    //on array de dist. conté les distàncies de cada robot del team al robot per cada enemic
    private int stat;
    //conté l'estat/fase en el que es troben els robots del team
    private Integer radar_turn_count;
    //serveix per saber quan el radar ha acabat de donar una volta completa
    private String enemy_name;
    //emmagatzemna el nom de l'enemic
    private double enemy_distance;
    //és la distància del robot al robot enemy_name
    private Timer timer;
    //de la classe timer emmagatzema el temps que passa
    private Boolean two_sec_passed;
    //canviarà de false a true i de true a false cada 2 segons
    private Boolean control_timer;
    //diu si el robot és el que controla el temps per fer les voltes a la fase d'òrbita
    
    public MyFirstRobot() {
        //inicialitzem totes les variables
        this.enemies_distances = new HashMap<>();
        stat = 0;
        radar_turn_count = 0;
        enemy_name = "";
        enemy_distance = 0;
        two_sec_passed = false;
        control_timer = true;
    }
    
    @Override
    public void run(){
        //fem que el moviment de la gun, el radar i el robot siguin independents
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);
        //guardem en un string la posició del robot
        String teammate_pos = getX() + "," + getY();
        try {
            //aquesta part no serveix per res però a l'enunciat diu de passar la pos. inicial a tots els teammates
            broadcastMessage(teammate_pos);
        } catch (IOException ex){
            Logger.getLogger(MyFirstRobot.class.getName()).log(Level.SEVERE, null, ex);
        }
        while(true){
            setTurnRadarRight(360);
            execute();
        }
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent e){
        //
        if(!isTeammate(e.getName()) && stat == 0){
            //Per el primer estat, quan escaneja un robot que no és del team, crea un string amb un identificador
            //al principi "enemyDistance:" + el nom i la distància del robot escanejats, separats per comes.
            //Després crida a la funció addEnemyDistance() per afagir nom i distància al map enemies_distances.
            //Finalment envia l'string enemy_pos a tots els teammates via broadcastMessage()
            String enemy_pos = "enemyDistance:," + e.getName() + "," + e.getDistance();
            //out.println("enemy_pos: " + enemy_pos);
            try {
            addEnemyDistance(e.getName(), e.getDistance());
            //l'string conté: enemyName,enemyDistance
            broadcastMessage(enemy_pos);
            } catch (IOException ex) {
                Logger.getLogger(MyFirstRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        if(enemy_name.equals(e.getName()) && stat == 1) {
            //quan l'estat és 1, comprova tota l'estona quina és la distància amb el robot enemic, i en funció de 
            //com aprop es trobi fa una cosa o una altra
            enemy_distance = e.getDistance();
            if(enemy_distance < getBattleFieldWidth()*0.2){
                //Canvia a l'estat 2 i crida la funció timer si és el primer
                stat = 2;
                if(control_timer) timer();
            }
            else if(enemy_distance > getBattleFieldWidth()*0.2){
                //calcula l'angle amb l'enemic i l'utilitza per apuntar radar i gun cap a ell i dirigir's-hi
                double angle_to_enemy = getHeading() + e.getBearing();
                //gira la gun, el radar, i el cos per apuntar al robot enemy_name
                setTurnGunRight(normalizeBearing(angle_to_enemy - getGunHeading()));
                setTurnRadarRight(normalizeBearing(angle_to_enemy - getRadarHeading()));
                setTurnRight(normalizeBearing(angle_to_enemy - getHeading()));
                //el robot apunta cap a la mateixa direcció que el radar (amb un marge)
                if((getHeading() - getRadarHeading()) <= 25 && (getHeading() - getRadarHeading()) >= -25) setAhead(10);
                //en funció de la distància a la que es trobi dem l'enemic dispara amb una potència o una altra
                //com més a prop, més fort
                if(enemy_distance < getBattleFieldWidth()*0.5) fire(2);
                else if(enemy_distance < getBattleFieldWidth()*0.3) fire(3);
                else fire(1);
            }
        }
        if(enemy_name.equals(e.getName()) && stat == 2){
            //comprova que l'energia de l'enemic sigui més gran que el nº de robots de l'equip * 1´2
            //aquest valor correspon a l'energia que es perd per bearing
            if((int) e.getEnergy() < ((getTeammates().length + 1)*1)) stat = 3;
            double angle_to_enemy = getHeading() + e.getBearing();
            int distance = 10;
            //gira la gun i el radar per apuntar al robot enemy_name, el cos serà sempre prependicular a l'enemic
            setTurnGunRight(normalizeBearing(angle_to_enemy - getGunHeading()));
            setTurnRadarRight(normalizeBearing(angle_to_enemy - getRadarHeading()));
            setTurnRight(normalizeBearing(angle_to_enemy - getHeading()) + 90);
            //cada 2 segons canvia la direcció de moviment
            if(two_sec_passed){
                distance = distance * -1;
            }
            setAhead(distance);
            fire(3);
        }
        if(enemy_name.equals(e.getName()) && stat == 3){
            //els robots es llençen sobre l'enemic per provocar + puntuació per bearing
            double angle_to_enemy = getHeading() + e.getBearing();
            setTurnGunRight(normalizeBearing(angle_to_enemy - getGunHeading()));
            setTurnRadarRight(normalizeBearing(angle_to_enemy - getRadarHeading()));
            setTurnRight(normalizeBearing(angle_to_enemy - getHeading()));
            setAhead(100);
        }
    }
    
    @Override
    public void onMessageReceived(MessageEvent e){
        //desxifra cada missatge
        String message_string = (String)e.getMessage();//converteix del tipus Serializable a String
        String[] message = message_string.split(",");
        if("enemyDistance:".equals(message[0])){
            //afageix la distancia passada al enemic corresponent
            addEnemyDistance(message[1], Double.valueOf(message[2]));
        }
        if("enemyDead".equals(message[0])){
            stat = 0;
            radar_turn_count = 0;
            enemy_name = "";
            enemy_distance = 0;
            enemies_distances.clear();
            two_sec_passed = false;
            control_timer = true;
        }
        if("timer".equals(message[0])){
            control_timer = false;
            if(message[1] == "true"){
                two_sec_passed = true;
            }
            else two_sec_passed = false;
        }
    }
    
    @Override
    //Quan el robot enemic objectiu mor, tots els robots del team tornen a l'estat 0
    public void onRobotDeath(RobotDeathEvent e){
        //funciona, quan el robot enemy_name mor, tots els robots del team passen a l'estat 0
        if(enemy_name.equals(e.getName()) && stat != 0){
            String death_robot = "enemyDead";
            try {
            broadcastMessage(death_robot);
            } catch (IOException ex) {
                Logger.getLogger(MyFirstRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
            stat = 0;
            radar_turn_count = 0;
            enemy_name = "";
            enemy_distance = 0;
            enemies_distances.clear();
            two_sec_passed = false;
            control_timer = true;
        }   
    }
    
    public void addEnemyDistance(String enemyName, Double distance) {
        //comprova si l'enemic es troba al map
        if (enemies_distances.containsKey(enemyName)) {
            enemies_distances.get(enemyName).add(distance);
        } else {
            //crea una nova llsta amb el nom i la distància
            List<Double> distances = new ArrayList<>();
            distances.add(distance);
            enemies_distances.put(enemyName, distances);
        }
    }
    
    public void printEnemiesDistances() {
        //he creat aquesta funció per poder veure les distàncies de cada robot enemic més fàcilment
        for (Map.Entry<String, List<Double>> entry : enemies_distances.entrySet()) {
            String enemyName = entry.getKey();
            List<Double> distances = entry.getValue();
            System.out.println("Enemy: " + enemyName + ", Distances: " + distances);
        }
    }

    @Override
    public void onStatus(StatusEvent e){
        //en aquest mètode, esperem que el radar faci 10 girs i després calculem a quin robot atacar.
        //aquesta funció s'utilitza per esperar que el radar acabi de donar tota la volta i 
        //després seleccionar un objectiu.
        if (radar_turn_count == 10 && stat == 0) {
            //s'utilitza per esperar que el radar acabi de donar tota la volta i calcular a quin robot atacar
            //printEnemiesDistances();
            calculateCloserRobot();
            radar_turn_count = 0;
        }
        radar_turn_count++;
    }
    
    private void calculateCloserRobot() {
        //calcula el robot enemic més proper basant-se en les distàncies emmagatzemades i, com diu a l'enunciat
        //"Considerem la distància d’un robot enemic al nostre equip com la distància més llunyana a la que es
        //troba un dels nostres robots de l’objectiu"
        // Iterem a través de les distàncies dels enemics i trobem el més proper.
        double min_distance = Double.MAX_VALUE;
        for (Map.Entry<String, List<Double>> entry : enemies_distances.entrySet()) {
            List<Double> distances = entry.getValue();
            distances.sort((Double d1, Double d2) -> d2.compareTo(d1));
            if (distances.get(0) < min_distance) {
                min_distance = distances.get(0);
                enemy_name = entry.getKey();
            }
            //establim el nom i la distància de l'enemic més propers
        }
        enemy_distance = min_distance;
        //passem a l'estat 1
        stat = 1;
    }

    private double normalizeBearing(double bearing) {
        //aquesta funció normalitza un valor d'orientació perquè estigui dins l'interval -180 a 180 graus.
        while (bearing > 180) {
            bearing -= 360;
        }
        while (bearing < -180) {
            bearing += 360;
        }
        return bearing;
    }

    private void timer() {
        //cada 2 segons canvia la var. a true o false
        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                two_sec_passed = !two_sec_passed;
                try {
                    broadcastMessage(two_sec_passed?"timer,false":"timer,true");
                } catch (IOException ex) {
                    Logger.getLogger(MyFirstRobot.class.getName()).log(Level.SEVERE, null, ex);
                }
                
            }
        }, 0, 2000);
    }
}