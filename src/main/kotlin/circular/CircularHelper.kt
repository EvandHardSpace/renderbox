package circular

import org.openrndr.color.ColorRGBa
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2

enum class Step { XL, L, M, S, DELETED }

class Processing(var state: Step, val position: Vector2, val gridPosition: IntVector2)

fun Step.getSize(minSize: Double) = when(this) {
    Step.XL -> minSize * 8
    Step.L -> minSize * 4
    Step.M -> minSize * 2
    Step.S -> minSize
    Step.DELETED -> error("DELETED instances must be filtered")
}

fun Step.getColor() = when(this) {
    Step.XL -> ColorRGBa.fromHex("#ffffff")
    Step.L -> ColorRGBa.fromHex("#a1a1a1")
    Step.M -> ColorRGBa.fromHex("#707070")
    Step.S -> ColorRGBa.fromHex("#3a3a3a")
    Step.DELETED -> ColorRGBa.RED
}
fun Double.discreteToStep(): Step = when (this) {
    in -1.0..-0.8 -> Step.S
    in -0.8..-0.2 -> Step.M
    in -0.2..0.3 -> Step.L
    in 0.3..1.0 -> Step.XL
    else -> Step.XL
}