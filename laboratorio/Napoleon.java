package laboratorio;

import robocode.JuniorRobot;

public class Napoleon extends Estratega {
    // tuning simple
    private static final int LOW_HP = 18;

    protected Napoleon(JuniorRobot robot) {
        super(robot);
    }

    @Override public void arranque() {
        // al empezar: usar Sniper
        Estratega.Sniper.arranque(this);
    }

    @Override public void alEscanear() {
        // si tengo baja vida → usar LowHP; si no → Sniper
        if (r.energy < LOW_HP) Estratega.LowHP.alEscanear(this);
        else                   Estratega.Sniper.alEscanear(this);
    }

    @Override public void alChocar() {
        if (r.energy < LOW_HP) Estratega.LowHP.alChocar(this);
        else                   Estratega.Sniper.alChocar(this);
    }

    @Override public void alRecibirDano() {
        if (r.energy < LOW_HP) Estratega.LowHP.alRecibirDano(this);
        else                   Estratega.Sniper.alRecibirDano(this);
    }

    @Override public void bajaVida() {
        // si querés forzar low hp desde el robot
        Estratega.LowHP.bajaVida(this);
    }
}
