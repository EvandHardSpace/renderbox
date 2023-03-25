package circular

import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2

enum class CellType { XL, L, M, S, EMPTY }

class RawField(val value: Double, val position: Vector2)

fun CellType.getSize(minSize: Double) = when(this) {
    CellType.XL -> minSize * 8
    CellType.L -> minSize * 4
    CellType.M -> minSize * 2
    CellType.S -> minSize
    CellType.EMPTY -> error("DELETED instances must be filtered")
}

fun CellType.getColor() = when(this) {
    CellType.XL -> ColorRGBa.fromHex("#ffffff")
    CellType.L -> ColorRGBa.fromHex("#a1a1a1")
    CellType.M -> ColorRGBa.fromHex("#707070")
    CellType.S -> ColorRGBa.fromHex("#3a3a3a")
    CellType.EMPTY -> ColorRGBa.RED
}
fun Double.discreteToStep(): CellType = when (this) {
    in -1.0..-0.8 -> CellType.S
    in -0.8..-0.2 -> CellType.M
    in -0.2..0.3 -> CellType.L
    in 0.3..1.0 -> CellType.XL
    else -> CellType.XL
}