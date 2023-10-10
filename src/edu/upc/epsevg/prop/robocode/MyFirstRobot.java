package edu.upc.epsevg.prop.robocode;

import robocode.*;

/**
 *
 * @author francesco
 */
public class MyFirstRobot extends TeamRobot
{
    @Override
    public void run() {
        turnLeft(getHeading());
        while(true) {
            setTurnRight(10000);
            setTurnGunRight(90);
            setAhead(2000);
            execute();
        }
    }
    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
        fire(1);
    }
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
        turnLeft(180);
    }
}
