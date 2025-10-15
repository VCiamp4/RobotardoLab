package PonceCiamparella

import robocode.JuniorRobot
import robocode.JuniorRobot.*
import kotlin.math.abs
import kotlin.math.hypot
import kotlin.math.atan2
import java.lang.Math.toDegrees

class Napoleon private constructor() : Estratega {

    companion object {
        private val INSTANCE = Napoleon()
        @JvmStatic fun getInstance(): Napoleon = INSTANCE
    }

    private lateinit var r: JuniorRobot
    private var esquina: Int = 0
    private val LOW_HP = 18
    private val MARGEN = 60

    override fun bind(self: JuniorRobot) {
        this.r = self
        this.esquina = 0 // reset por cada batalla
    }

    private fun resolver(): Estrategia {
        return if (r.energy < LOW_HP.toDouble()) corredor else sniper
    }

    override fun arranque() { resolver().arranque() }
    override fun alEscanear() { resolver().alEscanear() }
    override fun alChocar() { resolver().alChocar() }
    override fun alRecibirDano() { resolver().alRecibirDano() }
    override fun alChoqueRobot() { resolver().alChoqueRobot() }
    override fun bajaVida() { resolver().bajaVida() }

    // Estrategias privadas

    // --- Sniper ---
    private val sniper: Estrategia = object : Estrategia {
        override fun arranque() { irAEsquina(); mirarCentro() }

        override fun alEscanear() {
            val ang = norm(r.heading + r.scannedBearing)

            val distEsquina = distEsquinaActual()
            val frenteYCerca = (distEsquina > 100 && abs(r.scannedBearing) < 25 && r.scannedDistance < 180)
            val esquinaOcup = (distEsquina < 120 && r.scannedDistance < 220)
            if (frenteYCerca || esquinaOcup) {
                pasoLateral(); esquina = sigEsquina(); irAEsquina(); mirarCentro(); return
            }

            r.turnGunTo(ang)
            val desvio = angDiff(ang, r.gunHeading)
            if (r.gunReady && abs(desvio) <= 2) {
                r.fire(1.8)
                r.doNothing(20)
            } else {
                r.turnGunRight(30); r.turnGunLeft(60); r.turnGunRight(30)
            }
        }

        override fun alRecibirDano() { esquina = sigEsquina(); irAEsquina(); mirarCentro() }
        override fun bajaVida() { /* opcional */ }
        override fun alChoqueRobot() { r.back(50); r.turnRight(90); r.ahead(100) }
        override fun alChocar() { r.back(40); r.turnRight(30); mirarCentro() }
    }

    // --- Corredor ---
    private val corredor: Estrategia = object : Estrategia {
        override fun arranque() {
            r.setColors(red, blue, white, yellow, black)
            while (true) {
                r.ahead(100)
                r.turnRight(45)
                r.back(50)
                r.turnLeft(90)
                r.turnGunLeft(35)
                r.turnGunRight(70)
                r.turnGunLeft(35)
                r.doNothing(1)
            }
        }

        override fun alEscanear() {
            val angEnemigo = r.heading + r.scannedBearing
            r.turnGunTo(angEnemigo)
            if (r.gunReady) {
                val potencia = if (r.energy > 30.0) 2.5 else 1.5
                r.fire(potencia)
            }
        }

        override fun alChocar() { r.back(40); r.turnRight(90); r.ahead(50) }
        override fun alRecibirDano() { r.ahead(100); r.turnGunRight(180) }
        override fun bajaVida() { r.ahead(150); r.turnGunRight(90) }
        override fun alChoqueRobot() { r.back(50); r.turnRight(90); r.ahead(100) }
    }

    private fun irAEsquina() {
        val x = if (esquina == 1 || esquina == 2) r.fieldWidth - MARGEN else MARGEN
        val y = if (esquina >= 2) r.fieldHeight - MARGEN else MARGEN
        val dx = x - r.robotX
        val dy = y - r.robotY
        val a = toDegrees(atan2(dx.toDouble(), dy.toDouble())).toInt()
        r.turnTo(a)
        r.ahead(hypot(dx.toDouble(), dy.toDouble()).toInt())
    }

    private fun mirarCentro() {
        val cx = r.fieldWidth / 2
        val cy = r.fieldHeight / 2
        val dx = cx - r.robotX
        val dy = cy - r.robotY
        r.turnTo(toDegrees(atan2(dx.toDouble(), dy.toDouble())).toInt())
    }

    private fun pasoLateral() { r.back(40); r.turnRight(90); r.ahead(100) }

    private fun distEsquinaActual(): Int {
        val x = if (esquina == 1 || esquina == 2) r.fieldWidth - MARGEN else MARGEN
        val y = if (esquina >= 2) r.fieldHeight - MARGEN else MARGEN
        return hypot((x - r.robotX).toDouble(), (y - r.robotY).toDouble()).toInt()
    }

    private fun sigEsquina(): Int = (esquina + 1) % 4

    private fun norm(a: Int): Int {
        val x = a % 360
        return if (x < 0) x + 360 else x
    }

    private fun angDiff(a: Int, b: Int): Int {
        var d = (a - b) % 360
        if (d < -180) d += 360
        if (d > 180) d -= 360
        return d
    }
}
