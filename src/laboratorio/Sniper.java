package laboratorio;

import robocode.JuniorRobot;

import static robocode.JuniorRobot.*;

public class Sniper implements Strategy {
    private JuniorRobot r;
    private int esquina = 0;

    @Override public void init(JuniorRobot self) { r = self; }

    @Override
    public void arranque() {
        r.setColors(red, blue, white, yellow, black);
        irAEsquina();
        mirarCentro();
        while (true) {
            r.turnGunRight(360); // barrido completo
            r.doNothing(1);
        }
    }

    @Override
    public void botEscaneado() {
        int ang = norm(r.heading + r.scannedBearing);

        // Evitar choque/esquina ocupada
        int distEsquina = distEsquinaActual();
        if ((distEsquina > 100 && Math.abs(r.scannedBearing) < 25 && r.scannedDistance < 180)
                || (distEsquina < 120 && r.scannedDistance < 220)) {
            pasoLateral(); esquina = sigEsquina(); irAEsquina(); mirarCentro(); return;
        }

        // Apuntar
        r.turnGunTo(ang);

        // Disparar solo si está alineado ±2°
        int desvio = angDiff(ang, r.gunHeading);
        if (r.gunReady && Math.abs(desvio) <= 2) {
            r.fire(1.8);
            r.doNothing(20); // <-- espera unos ticks antes de volver a disparar
        }
    }

    @Override
    public void reciboDano() {
        esquina = sigEsquina(); irAEsquina(); mirarCentro();
    }

    @Override
    public void choco() { r.back(40); r.turnRight(30); mirarCentro(); }

    // ==== Helpers ====
    private void irAEsquina() {
        int m = 60;
        int x = (esquina == 1 || esquina == 2) ? r.fieldWidth - m : m;
        int y = (esquina >= 2) ? r.fieldHeight - m : m;
        int dx = x - r.robotX, dy = y - r.robotY;
        int a = (int)Math.toDegrees(Math.atan2(dx, dy));
        r.turnTo(a);
        r.ahead((int)Math.hypot(dx, dy));
    }

    private void mirarCentro() {
        int cx = r.fieldWidth/2, cy = r.fieldHeight/2;
        int dx = cx - r.robotX, dy = cy - r.robotY;
        r.turnTo((int)Math.toDegrees(Math.atan2(dx, dy)));
    }

    private void pasoLateral() { r.back(40); r.turnRight(90); r.ahead(100); }

    private int distEsquinaActual() {
        int m = 60;
        int x = (esquina == 1 || esquina == 2) ? r.fieldWidth - m : m;
        int y = (esquina >= 2) ? r.fieldHeight - m : m;
        return (int)Math.hypot(x - r.robotX, y - r.robotY);
    }

    private int sigEsquina() { return (esquina + 1) % 4; }

    private int norm(int a){ int x=a%360; return x<0?x+360:x; }

    private int angDiff(int a,int b){
        int d=(a-b)%360; if(d<-180)d+=360; if(d>180)d-=360; return d;
    }
}
