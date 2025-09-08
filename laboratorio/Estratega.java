package laboratorio;

import robocode.JuniorRobot;

public abstract class Estratega {
    protected final JuniorRobot r;
    protected int esquina = 0; // 0 SW, 1 SE, 2 NE, 3 NW

    protected Estratega(JuniorRobot robot) { this.r = robot; }

    // contrato común
    public abstract void arranque();
    public abstract void alEscanear();
    public abstract void alChocar();
    public abstract void alRecibirDano();
    public abstract void bajaVida();

    // helpers comunes (cortos)
    protected void irAEsquina() {
        int m=60;
        int x=(esquina==1||esquina==2)? r.fieldWidth-m : m;
        int y=(esquina>=2)? r.fieldHeight-m : m;
        int dx=x-r.robotX, dy=y-r.robotY;
        r.turnTo((int)Math.toDegrees(Math.atan2(dx,dy)));
        r.ahead((int)Math.hypot(dx,dy));
    }
    protected void mirarCentro() {
        int cx=r.fieldWidth/2, cy=r.fieldHeight/2;
        int dx=cx-r.robotX, dy=cy-r.robotY;
        r.turnTo((int)Math.toDegrees(Math.atan2(dx,dy)));
    }
    protected void pasoLateral(){ r.back(40); r.turnRight(90); r.ahead(100); }
    protected int  distEsquinaActual() {
        int m=60;
        int x=(esquina==1||esquina==2)? r.fieldWidth-m : m;
        int y=(esquina>=2)? r.fieldHeight-m : m;
        return (int)Math.hypot(x-r.robotX, y-r.robotY);
    }
    protected static int norm(int a){ int x=a%360; return x<0?x+360:x; }
    protected static int angDiff(int a,int b){
        int d=(a-b)%360; if(d<-180)d+=360; if(d>180)d-=360; return d;
    }

    /* ======== Estrategias anidadas (helpers, NO heredan de Estratega) ======== */
    public static class Sniper {
        public static void arranque(Estratega c){ c.irAEsquina(); c.mirarCentro(); }
        public static void alEscanear(Estratega c){
            int ang = norm(c.r.heading + c.r.scannedBearing);

            // evitar choque/esquina ocupada al ir a la esquina
            boolean frenteYCerca = (Math.abs(c.r.scannedBearing)<25 && c.r.scannedDistance<180 && c.distEsquinaActual()>100);
            boolean esquinaOcup  = (c.distEsquinaActual()<120 && c.r.scannedDistance<220);
            if (frenteYCerca || esquinaOcup) {
                c.pasoLateral(); c.esquina=(c.esquina+1)%4; c.irAEsquina(); c.mirarCentro(); return;
            }

            // barrido 360 si no hay lock
            c.r.turnGunRight(360);

            // apuntar y disparar solo alineado (±2°), potencia fija
            c.r.turnGunTo(ang);
            if (c.r.gunReady && Math.abs(angDiff(ang, c.r.gunHeading))<=2) {
                c.r.fire(1.8);
                c.r.doNothing(20); // esperar un poco tras el tiro
            }
        }
        public static void alChocar(Estratega c){ c.r.back(40); c.r.turnRight(30); c.mirarCentro(); }
        public static void alRecibirDano(Estratega c){ c.esquina=(c.esquina+1)%4; c.irAEsquina(); c.mirarCentro(); }
        public static void bajaVida(Estratega c){ /* nada */ }
    }

    public static class LowHP {
        public static void arranque(Estratega c){ /* nada */ }
        public static void alEscanear(Estratega c){
            // ir al centro una sola vez y dar círculos cortos (ahorra)
            if (c.esquina != -1) { irCentro(c); c.esquina = -1; } // usamos -1 como "flag ya en centro"
            c.r.turnRight(20);
            c.r.ahead(30);
            // sin disparar: prioridad sobrevivir
        }
        private static void irCentro(Estratega c){
            int cx=c.r.fieldWidth/2, cy=c.r.fieldHeight/2;
            int dx=cx-c.r.robotX, dy=cy-c.r.robotY;
            c.r.turnTo((int)Math.toDegrees(Math.atan2(dx,dy)));
            c.r.ahead((int)Math.hypot(dx,dy));
        }
        public static void alChocar(Estratega c){ c.r.back(40); c.r.turnRight(45); }
        public static void alRecibirDano(Estratega c){ /* seguimos circulando */ }
        public static void bajaVida(Estratega c){ /* ya está en low hp */ }
    }
}
