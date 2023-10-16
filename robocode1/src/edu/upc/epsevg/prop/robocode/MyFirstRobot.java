package edu.upc.epsevg.prop.robocode;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import robocode.*;


/**
 *
 * @author francesco
 */

public class MyFirstRobot extends TeamRobot{
    private final Map<String, List<Double>> enemies_distances;
    //contindrà <nom> <array de dist.> per cada enemy
    private int stat;
    private Integer radar_turn_count;
    private String enemy_name;
    private double enemy_bearing;
    private double enemy_distance;
    
    public MyFirstRobot() {
        this.enemies_distances = new HashMap<>();
        stat = 0;
        radar_turn_count = 0;
        enemy_name = "";
        enemy_bearing = 0;
        enemy_distance = 0;
    }
    
    @Override
    public void run(){
        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);
        setAdjustRadarForRobotTurn(true);
        //Aquesta part no serveix per res però a l'enunciat diu de passar la pos. inicial
        /*String teammate_pos = getX() + "," + getY();
        try {
            broadcastMessage(teammate_pos);
        } catch (IOException ex){
            Logger.getLogger(MyFirstRobot.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        setTurnRadarRight(360);
        execute();
        while (true){
            if(stat == 1) aproximacio();
        }
    }
    
    @Override
    public void onScannedRobot(ScannedRobotEvent e){//funciona
        //String enemy_pos = "enemyName:" + e.getName() + ",teammateName:" + getName() + ",enemyDistance:" + e.getDistance();
        //només fer per l'estat 0, a la resta d'estats no
        if(!isTeammate(e.getName()) && stat == 0){
            String enemy_pos = "enemyDistance:," + e.getName() + "," + e.getDistance();
            try {
                //l'string conté: enemyName,enemyDistance
            addEnemyDistance(e.getName(), e.getDistance());
            broadcastMessage(enemy_pos);
            } catch (IOException ex) {
                Logger.getLogger(MyFirstRobot.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }
    
    @Override
    public void onMessageReceived(MessageEvent e){
        String message_string = (String)e.getMessage(); // Convert Serializable to String
        String[] message = message_string.split(",");
        if("enemyDistance:".equals(message[0])){
            //Afageix la distancia passada al enemic corresponent
            addEnemyDistance(message[1], Double.valueOf(message[2]));
        }
    }
    
    public void addEnemyDistance(String enemyName, Double distance) {
        // Comprova si l'enemic es troba al map
        if (enemies_distances.containsKey(enemyName)) {
            enemies_distances.get(enemyName).add(distance);
        } else {
            //crea una nova llsta amb el nom i la distancia
            List<Double> distances = new ArrayList<>();
            distances.add(distance);
            enemies_distances.put(enemyName, distances);
        }
    }
    
    public void printEnemiesDistances() {//funciona
        for (Map.Entry<String, List<Double>> entry : enemies_distances.entrySet()) {
            String enemyName = entry.getKey();
            List<Double> distances = entry.getValue();
            System.out.println("Enemy: " + enemyName + ", Distances: " + distances);
        }
    }

    @Override
    public void onStatus(StatusEvent e){
        if (radar_turn_count == 11 && stat == 0) {
            //s'utilitza per esperar a que acabi de donar tota la volta per calcular a quin robot atacar
            printEnemiesDistances();
            calculateCloserRobot();
            radar_turn_count = 0;
        }
        radar_turn_count++;
    }
    
    private void calculateCloserRobot() {
        double min_distance = Double.MAX_VALUE;
        for (Map.Entry<String, List<Double>> entry : enemies_distances.entrySet()) {
            List<Double> distances = entry.getValue();
            distances.sort((Double d1, Double d2) -> d2.compareTo(d1));
            if (distances.get(0) < min_distance) {
                min_distance = distances.get(0);
                enemy_name = entry.getKey();
            }
        }
        enemy_distance = min_distance;
        stat = 1;
    }

    private void aproximacio() {
        getRadarHeading();
    }
    
}