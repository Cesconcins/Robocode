package robot1;


import java.awt.Graphics2D;
import robocode.AdvancedRobot;
import robocode.Robot;
import robocode.Droid;
import robocode.Rules;
import robocode.ScannedRobotEvent;
import robocode.TeamRobot;

/**
 *
 * @author usuario
 */
public class MyRobot1 extends TeamRobot {

    @Override

    public void run() {

        setAdjustGunForRobotTurn(true);
        setAdjustRadarForGunTurn(true);

        while (true) {
            setAhead(getBattleFieldWidth() * 0.3);
            setTurnLeft(90);
            setTurnRadarRight(90);
            execute();
            
            System.out.println(getHeading()); //mirar en terminal de batalla
            //SystemDebbugging();

        }
    }
    
    public void onPaint(Graphics2D g){
        g.drawLine(0,0,(int)this.getX(),(int)this.getY());
    }

}
