package laboratorio;
import robocode.*;

public class LaboRobot extends JuniorRobot {
    private Strategy est;

    public LaboRobot() {
        this.est = new Sniper();
        this.est.init(this);
    }

    public LaboRobot(Strategy st){
        this.est = st;
        this.est.init(this);
    }

    @Override
    public void run(){
        setColors(orange, blue, white, yellow, black);
        est.arranque();
    }

    @Override
    public void onScannedRobot() { est.botEscaneado(); }

    @Override
    public void onHitByBullet() { est.reciboDano(); }

    @Override
    public void onHitWall(){ est.choco(); }
}
