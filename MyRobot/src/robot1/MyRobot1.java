package robot1;


import java.awt.Graphics2D;
import robocode.*;

/**
 *
 * @author usuario
 */
public class MyRobot1 extends TeamRobot {

    @Override
    public void run() {
        gestionaRadar();
        gestionaGun();
        gestionaMoviment();
        updateState();
        finish();
    }

    private void gestionaRadar() {
        throw new UnsupportedOperationException("Not supported yet.");
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
