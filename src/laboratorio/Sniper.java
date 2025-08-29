package laboratorio;

import robocode.AdvancedRobot;
import robocode.TurnCompleteCondition;
import robocode.util.Utils;

public class Sniper extends AdvancedRobot implements Strategy {
    @Override
    public void botEscaneado() {
            this.scannedAngle(fire);
    }

    @Override
    public void choco() {
        back(10);
    }

    @Override
    public void reciboDano() {
        turnAheadLeft(5,5);
        this.bajaVida();
    }

    @Override
    public void bajaVida(){
        if (energia < 40){
            while (others > 1){
                this.setAhead((double)40000.0F);
                this.movingForward = true;
                this.setTurnRight((double)90.0F);
                this.waitFor(new TurnCompleteCondition(this));
                this.setTurnLeft((double)180.0F);
                this.waitFor(new TurnCompleteCondition(this));
                this.setTurnRight((double)180.0F);
                this.waitFor(new TurnCompleteCondition(this));
            }
        }
    }

    @Override
    public void arranque(){
        this.stopWhenSeeRobot = false;
        this.turnRight(Utils.normalRelativeAngleDegrees((double)corner - this.getHeading()));
        this.stopWhenSeeRobot = true;
        this.ahead((double)5000.0F);
        this.turnLeft((double)90.0F);
        this.ahead((double)5000.0F);
        this.turnGunLeft((double)90.0F);
    }
}
