package laboratorio;

import robocode.JuniorRobot;

public class Sniper implements Strategy {
    private JuniorRobot r;
    private int esquina = 0;

    @Override
    public void init(JuniorRobot self) {
        this.r = self;
    }

    @Override
    public void arranque() {
        moverAEsquina();
        while (true) {
            r.turnGunLeft(10);   // barrido simple con el cañón
            r.turnGunRight(20);
            r.turnGunLeft(10);
            r.doNothing(1);      // pasa un turno
        }
    }

    @Override
    public void botEscaneado() {
        int angEnemigo = r.heading + r.scannedBearing;
        r.turnGunTo(angEnemigo);
        if (r.gunReady) {
            double potencia = (r.scannedDistance < 200) ? 3 : 1.5;
            r.fire(potencia);
        }
        if (r.scannedDistance < 150) cambiarEsquina();
    }

    @Override
    public void choco() {
        r.back(50);
        r.turnRight(90);
    }

    @Override
    public void reciboDano() {
        cambiarEsquina();
    }

    @Override
    public void bajaVida() {
        // no hago nada especial acá
    }

    private void moverAEsquina() {
        int margen = 60;
        int x = (esquina == 1 || esquina == 2) ? r.fieldWidth - margen : margen;
        int y = (esquina >= 2) ? r.fieldHeight - margen : margen;
        int dx = x - r.robotX;
        int dy = y - r.robotY;
        int ang = (int)Math.toDegrees(Math.atan2(dx, dy));
        r.turnTo(ang);
        r.ahead((int)Math.hypot(dx, dy));
    }

    private void cambiarEsquina() {
        esquina = (esquina + 1) % 4;
        moverAEsquina();
    }
}
