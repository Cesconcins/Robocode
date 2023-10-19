/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package robot1;

import robocode.*;
import static robocode.util.Utils.*;
import java.io.*;
import java.util.logging.Level;
import java.util.logging.Logger;
/**
 *
 * @author usuario
 */
public class robot_p extends TeamRobot{
      
  double d = 150.0, dir = 1;
  boolean picaParet = false;
  
  String objectiu = "cap";
  double vidaobjeciu, distobjeciu;
  
  boolean pacific = false;
  boolean orbita = false;
  
  boolean escollint = false;
  String enemics[];
  double disenemics[];
  
  // provar que l array nomes sigui de 5, en comptes de que sigui getOthers();
  // que al final ens estalviem moltes linies
  
  @Override
  public void run() {
    setAdjustGunForRobotTurn(true);
    setAdjustRadarForRobotTurn(true);
    setAdjustRadarForGunTurn(true);
    prepObjectiu();
    while (true) {
      setAhead((distobjeciu / 4 + 25) * dir);
      
      if (!picaParet && anemAxocar(3) && vidaobjeciu >= 45) {
        dir *= -1;
        picaParet = true;
      } else if (!anemAxocar(3.2) && vidaobjeciu >= 45) {
        picaParet = false;
      }
      execute();
    }
  }
  
  @Override
  public void onScannedRobot(ScannedRobotEvent e) {
    if (getOthers() == 1 && !isTeammate(e.getName())) {
      objectiu = e.getName();
    } else {
      if (escollint) {
        for (int i = 0; i < enemics.length; i++) {
          if (enemics[i].equals(e.getName())) return;
          if (enemics[i].equals("")) {
            enemics[i] = e.getName();
            disenemics[i] = e.getDistance();
            if (i == enemics.length-1) {
              escollirObjectiuInd();
              String envia = "objectiu ,"+objectiu+","+distobjeciu;
              out.println(envia);
              try {
                out.println(envia);
                broadcastMessage(envia);
              } catch (IOException ex) {
                Logger.getLogger(Droid.class.getName()).log(Level.SEVERE, null, ex);
              }
            }
            i = enemics.length;
          }
        }
        return;
      }
    }
    if (!e.getName().equals(objectiu)) return;
    if (!anemAxocar(0.6)) {
      setTurnRight(e.getBearing() + 90 - (e.getDistance() > getHeight()*3 ? 40 : 10) * dir);
    }
    double absBearing = e.getBearing() + getHeading();
    setTurnRadarRight(ajustarRadar(absBearing));
    setTurnGunRightRadians(aimDef(e)*0.85);
    if(e.getEnergy() > 45){
        setFire(piupiu(e.getDistance()));
    }
    vidaobjeciu = e.getEnergy();
    distobjeciu = e.getDistance();
  }
  
  @Override
  public void onHitRobot(HitRobotEvent e) {
    if(isTeammate(e.getName())){
        dir *= -1;
    }
  }
  
  @Override
  public void onHitWall(HitWallEvent e) {
    picaParet = true;
    if (dir == -1 && Math.abs(e.getBearing()) >= 160.0) {
      dir = 1;
    } else if (dir == 1 && Math.abs(e.getBearing()) <= 20.0) {
      dir = -1;
    } else {
      if (dir == 1) {
        setTurnRight(normalRelativeAngleDegrees(e.getBearing()));
        dir = -1;
      } else {
        setTurnRight(normalRelativeAngleDegrees(e.getBearing()+180));
        dir = 1;
      }
    }
  }
  
  @Override
  public void onRobotDeath(RobotDeathEvent e) {
    if (e.getName().equals(objectiu)) {
      if (getOthers() > 1) {
        prepObjectiu();
      } else {
        setTurnRadarRight(Double.POSITIVE_INFINITY);
      }
    } else if (escollint) {
      prepObjectiu();
    }
  }
  
  void prepObjectiu() {
    enemics = new String[getOthers()];
    disenemics = new double[getOthers()];
    for (int i = 0; i < enemics.length; i++) {
      enemics[i] = "";
    }
    escollint = true;
    setTurnRadarRight(Double.POSITIVE_INFINITY);
  }

  void escollirObjectiuInd() {
    escollint = false;
    int victima = -1;
    double menorDis = Double.MAX_VALUE;
    for (int i = 0; i < enemics.length; i++) {
      if (disenemics[i] < menorDis && !isTeammate(enemics[i])) {
        victima = i;
        menorDis = disenemics[i];
      }
    }
    objectiu = enemics[victima];
    distobjeciu = disenemics[victima];
  }
  
  
  // en aquesta mateix fucnio es pot canviar l enemic, de manera que cada cop que rep
  // un missatge canvia o no l objectiu, i aixi esta sempre en constant actualitxacio
  @Override
  public void onMessageReceived(MessageEvent e){
      String walkieTalkie = (String)e.getMessage().toString();
      String missatge[] = walkieTalkie.split(",");
      String victima = missatge[1];
      Double distancia = Double.valueOf(missatge[2]);
      if(distancia < distobjeciu){
            objectiu = victima;
      }
  }
  
  double ajustarRadar(double absBearing) {
    if (getOthers() > 1) {
      double modR;
      modR = normalRelativeAngleDegrees((absBearing - getRadarHeading()));
      modR += 22.5*Math.signum(modR);
      if (modR > 45.0) {
        modR = 45.0;
      } else if (modR < -45.0) {
        modR = -45.0;
      } else if (modR > 0.0 && modR < 20.0) {
        modR = 20.0;
      } else if (modR > -20.0 && modR <= 0.0) {
        modR = -20.0;
      }
      return modR;
    } else {
      return normalRelativeAngleDegrees((absBearing - getRadarHeading())*2);
    }
  }
  
  double aimDef(ScannedRobotEvent e) {
    if (e.getEnergy() != 0.0) {
      double absBearingRad = getHeadingRadians() + e.getBearingRadians();
      double prediccio = e.getVelocity() * Math.sin(e.getHeadingRadians() - absBearingRad) / Rules.getBulletSpeed(piupiu(e.getDistance()));
      if (e.getDistance() <= 12*10) {
        prediccio *= 0.5;
      } else if (e.getDistance() <= 12*5) {
        prediccio *= 0.3;
      }
      return normalRelativeAngle(absBearingRad - getGunHeadingRadians() + prediccio);
    } else {
      return normalRelativeAngle(getHeadingRadians()+ e.getBearingRadians()- getGunHeadingRadians());
    }
  }
  
  double piupiu(double dobjeciu) {
    if (vidaobjeciu != 0.0) {
      if (dobjeciu < getHeight()*1.5) {
        return Rules.MAX_BULLET_POWER;
      } else if (getEnergy() > 4*Rules.MAX_BULLET_POWER + 2*(Rules.MAX_BULLET_POWER - 1) || (getOthers() == 1 && getEnergy() > 3*Rules.MAX_BULLET_POWER)) {
        if (dobjeciu <= d*2) {
          return Rules.MAX_BULLET_POWER;
        } else {
          return Math.min(1.1 + (d*2) / dobjeciu, Rules.MAX_BULLET_POWER);
        }
      } else if (getEnergy() > 2.2) {
        return 1.1;
      } else {
        return Math.max(0.1, getEnergy()/3);
      }
    } else {
      return 0.1;
    }
  }
  
  boolean anemAxocar(double r) {
    return getX() + getHeight()*r >= getBattleFieldWidth() || getX() - getHeight()*r <= 0.0 || getY() + getHeight()*r >= getBattleFieldHeight() || getY() - getHeight()*r <= 0.0;
  }
}