package laboratorio;

import robocode.JuniorRobot;

public interface Strategy {
    void init(JuniorRobot self);
    void arranque();
    void botEscaneado();
    void choco();
    void reciboDano();
    void bajaVida();
}
