package PonceCiamparella

import robocode.JuniorRobot

class TheBeast : JuniorRobot() {
    private val est: Strategy = Corredor()

    init {
        est.init(this)
    }

    override fun run() {
        setColors(orange, blue, white, yellow, black)
        est.arranque()
    }

    override fun onScannedRobot() {
        est.botEscaneado()
    }

    override fun onHitByBullet() {
        est.reciboDano()
    }

    override fun onHitWall() {
        est.choco()
    }
}
