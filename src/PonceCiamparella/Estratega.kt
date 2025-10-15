package PonceCiamparella

import robocode.JuniorRobot


interface Estratega{
    fun bind(self: JuniorRobot)
    fun arranque()
    fun alEscanear()
    fun alChocar()
    fun alRecibirDano()
    fun alChoqueRobot()
}