package robot1;


import java.awt.Graphics2D;
import robocode.*;

/**
 *
 * @author usuario
 */
public class MyRobot1 extends AdvancedRobot {

    @Override
    public void run() {
        turnLeft(getHeading());
        while(true) {
            setTurnRight(10000);
            setTurnGunRight(90);
            setAhead(2000);
            execute();
        }
               /*
        gestionaRadar();
        gestionaGun();
        gestionaMoviment();
        updateState();
        finish();
        */
    }


    @Override
    public void onScannedRobot(ScannedRobotEvent e) {
    fire(1);
    }
 
    @Override
    public void onHitByBullet(HitByBulletEvent e) {
    turnLeft(180);
    } 
    
    private void gestionaRadar() {
    }

    
    private void gestionaGun() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void gestionaMoviment() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void updateState() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private void finish() {
        throw new UnsupportedOperationException("Not supported yet."); 
    }
}
