package PonceCiamparella

import robocode.JuniorRobot
import robocode.HitRobotEvent

open class ZulGurum : JuniorRobot() {
    private var estratega: Estratega? = null

    override open fun run() {
        estratega = Genghis.getInstance()
        estratega?.bind(this)
        estratega?.arranque()
        while (true) {
            doNothing(1)
        }
    }

    override open fun onScannedRobot() {
        estratega?.alEscanear()
    }

    override open fun onHitByBullet() {
        estratega?.alChocar()
    }

    override open fun onHitWall() {
        estratega?.alChocar()
    }

    override open fun onHitRobot() {
        estratega?.alChoqueRobot()
    }
}