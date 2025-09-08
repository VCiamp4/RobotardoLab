package laboratorio;

import robocode.*;

public class ZulGurum extends JuniorRobot {
    private Estratega estratega;

    @Override
    public void run() {
        setColors(orange, blue, white, yellow, black);
        estratega = new EstrategaConcreto(this); // hereda de Estratega
        estratega.arranque();                    // arranca como Sniper
        while (true) { doNothing(1); }
    }

    @Override public void onScannedRobot() { estratega.alEscanear(); }
    @Override public void onHitByBullet()  { estratega.alRecibirDano(); }
    @Override public void onHitWall()      { estratega.alChocar(); }
}
