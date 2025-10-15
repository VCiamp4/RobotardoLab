package PonceCiamparella

import robocode.JuniorRobot


open interface Estrategia{
    fun arranque()
    fun alEscanear()
    fun alChocar()
    fun alRecibirDano()
    fun alChoqueRobot()
    fun bajaVida()
}