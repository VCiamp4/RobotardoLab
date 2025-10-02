// Sniper.kt
package PonceCiamparella

import robocode.JuniorRobot
import kotlin.math.atan2
import kotlin.math.hypot
import kotlin.math.abs
import java.lang.Math.toDegrees

class Sniper : Strategy {
    private lateinit var r: JuniorRobot
    private var esquina: Int = 0

    override fun init(self: JuniorRobot) {
        r = self
    }

    override fun arranque() {
        r.setColors(JuniorRobot.red, JuniorRobot.blue, JuniorRobot.white, JuniorRobot.yellow, JuniorRobot.black)
        irAEsquina()
        mirarCentro()
        while (true) {
            r.turnGunRight(360.0) // barrido completo
            r.doNothing(1.0)
        }
    }

    override fun botEscaneado() {
        val ang = norm(r.heading + r.scannedBearing)

        // Evitar choque/esquina ocupada
        val distEsquina = distEsquinaActual()
        val frenteYCerca = distEsquina > 100 && abs(r.scannedBearing) < 25 && r.scannedDistance < 180
        val esquinaOcup  = distEsquina < 120 && r.scannedDistance < 220
        if (frenteYCerca || esquinaOcup) {
            pasoLateral()
            esquina = sigEsquina()
            irAEsquina()
            mirarCentro()
            return
        }

        // Apuntar
        r.turnGunTo(ang.toDouble())

        // Disparar solo si está alineado ±2°
        val desvio = angDiff(ang, r.gunHeading.toInt())
        if (r.gunReady && abs(desvio) <= 2) {
            r.fire(1.8)
            r.doNothing(20.0) // espera unos ticks antes de volver a disparar
        }
    }

    override fun reciboDano() {
        esquina = sigEsquina()
        irAEsquina()
        mirarCentro()
    }

    override fun bajaVida() {
        // nada
    }

    override fun choqueRobot() {
        // nada
    }

    override fun choco() {
        r.back(40.0)
        r.turnRight(30.0)
        mirarCentro()
    }

    // ==== Helpers ====
    private fun irAEsquina() {
        val m = 60.0
        val x = if (esquina == 1 || esquina == 2) r.fieldWidth - m else m
        val y = if (esquina >= 2) r.fieldHeight - m else m
        val dx = x - r.robotX
        val dy = y - r.robotY
        val a = toDegrees(atan2(dx, dy)).toInt()
        r.turnTo(a.toDouble())
        r.ahead(hypot(dx, dy))
    }

    private fun mirarCentro() {
        val cx = r.fieldWidth / 2.0
        val cy = r.fieldHeight / 2.0
        val dx = cx - r.robotX
        val dy = cy - r.robotY
        r.turnTo(toDegrees(atan2(dx, dy)))
    }

    private fun pasoLateral() {
        r.back(40.0)
        r.turnRight(90.0)
        r.ahead(100.0)
    }

    private fun distEsquinaActual(): Int {
        val m = 60.0
        val x = if (esquina == 1 || esquina == 2) r.fieldWidth - m else m
        val y = if (esquina >= 2) r.fieldHeight - m else m
        return hypot(x - r.robotX, y - r.robotY).toInt()
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
