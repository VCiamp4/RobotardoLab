package PonceCiamparella

import robocode.JuniorRobot

interface Strategy {
    fun init(self: JuniorRobot)
    fun arranque()
    fun botEscaneado()
    fun choco()
    fun reciboDano()
    fun bajaVida()
    fun choqueRobot()
}
