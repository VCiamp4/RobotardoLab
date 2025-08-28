package laboratorio;

import robocode.util.Utils;

public class Sniper implements Strategy {
    @Override
    public void botEscaneado() {
            this.scannedAngle(fire);
    }

    @Override
    public void choco() {

    }

    @Override
    public void reciboDaño() {
        back(10);
    }

    @Override
    public void bajaVida(){

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
