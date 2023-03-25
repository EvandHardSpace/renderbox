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
    Step.L -> ColorRGBa.fromHex("#b6b6b6")
    Step.M -> ColorRGBa.fromHex("#545454")
    Step.S -> ColorRGBa.fromHex("#1c1c1c")
    Step.DELETED -> ColorRGBa.RED
}
fun Double.discreteToStep(): Step = when (this) {
    in -1.0..-0.5 -> Step.S
    in -0.5..0.0 -> Step.M
    in 0.0..0.5 -> Step.L
    in 0.5..1.0 -> Step.XL
    else -> Step.XL
}