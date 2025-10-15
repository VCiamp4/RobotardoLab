package PonceCiamparella;

import robocode.JuniorRobot;

import static robocode.JuniorRobot.*;

public final class Genghis implements Estratega {

    private static final Genghis INSTANCE = new Genghis();

    public static Genghis getInstance() {
        return INSTANCE;
    }

    private Genghis() {}
        private JuniorRobot r;

        @Override public void bind(JuniorRobot self) {
            this.r = self;
        }

        private Estrategia resolver() {
            return (r.energy < 45) ? escopeta : corredor;
        }

        @Override public void arranque()
        { resolver().arranque();
        }
        @Override public void alEscanear() {
            resolver().alEscanear();
        }
        @Override public void alChocar(){
            resolver().alChocar();
        }
        @Override public void alRecibirDano(){
            resolver().alRecibirDano();
        }
        @Override public void alChoqueRobot(){
            resolver().alChoqueRobot();
        }
        @Override public void bajaVida(){
            resolver().bajaVida();
        }
        // Estrategias privadas
        // --- escopeta ---
        // --- Escopeta (agresiva: acercarse y pegar fuerte) ---
        private final Estrategia escopeta = new Estrategia() {
            @Override
            public void arranque() {
                r.setColors(black, black, black, black, black);
                while (true) {
                    r.turnGunLeft(25);
                    r.turnGunRight(50);
                    r.turnGunLeft(25);
                    r.doNothing(1);
                }
            }

            @Override
            public void alEscanear() {
                int angAbs = r.heading + r.scannedBearing;
                r.turnGunTo(angAbs);

                int d = r.scannedDistance;

                r.turnTo(angAbs);

                if (d > 220) {
                    r.ahead(Math.min(140, d - 200));
                    if (r.gunReady) r.fire(2.0);
                } else if (d > 140) {
                    r.ahead(Math.min(100, d - 120));
                    if (r.gunReady) r.fire(2.5);
                } else {
                    if (r.gunReady) r.fire(3.0);
                    r.ahead(60);

                }

            }

            @Override
            public void alChocar() {
                r.back(60);
                r.turnRight(90);
                r.ahead(100);
            }

            @Override
            public void alRecibirDano() {
                r.turnRight(25);
                r.ahead(80);
            }

            @Override
            public void alChoqueRobot() {
                if (r.gunReady) r.fire(3.0);
                r.back(40);
                r.turnRight(45);
                if (r.gunReady) r.fire(2.5);
                r.ahead(80);
            }

            @Override
            public void bajaVida() {
                r.turnRight(20);
                r.ahead(100);
            }

        };

    // --- Corredor ---
    private final Estrategia corredor = new Estrategia() {
        @Override public void arranque() {
            r.setColors(red, blue, white, yellow, black);
            while (true) {
                r.ahead(100);
                r.turnRight(45);
                r.back(50);
                r.turnLeft(90);
                r.turnGunLeft(35);
                r.turnGunRight(70);
                r.turnGunLeft(35);
                r.doNothing(1);
            }
        }

        @Override public void alEscanear() {
            int angEnemigo = r.heading + r.scannedBearing;
            r.turnGunTo(angEnemigo);
            if (r.gunReady) {
                double potencia = (r.energy > 30) ? 2.5 : 1.5;
                r.fire(potencia);
            }
        }

        @Override public void alChocar()      { r.back(40); r.turnRight(90); r.ahead(50); }
        @Override public void alRecibirDano() { r.ahead(100); r.turnGunRight(180); }
        @Override public void bajaVida()      { r.ahead(150); r.turnGunRight(90); }
        @Override public void alChoqueRobot() { r.back(50); r.turnRight(90); r.ahead(100); }
    };
}
