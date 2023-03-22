import org.openrndr.animatable.Animatable
import org.openrndr.animatable.easing.Easing
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.LIGHT_CORAL
import org.openrndr.math.Vector2
import org.openrndr.shape.ShapeContour
import org.openrndr.shape.contour

const val unit = 30.0
const val matrixSize = 12
const val duration = 800L

var switch = false

class Anim : Animatable() {
    var rotationDegree: Double = 0.0
}

fun main() = application {
    configure { width = 800; height = 800 }

    val animatrix: List<Triple<Int, Int, Anim>> = buildList {
        (0 until matrixSize).forEach { y ->
            buildList {
                (0 until matrixSize).forEach { x ->
                    Triple(x, y, Anim()).run(::add)
                }
            }.run(::add)
        }
    }.flatten()

    program {
        extend {
            if (switch) {
                drawer.clear(ColorRGBa.WHITE)
                drawer.fill = ColorRGBa.LIGHT_CORAL

                drawer.translate(Vector2(unit * 2, unit))
            } else drawer.clear(ColorRGBa.LIGHT_CORAL)

            (0 until matrixSize).forEach { j ->
                (0 until matrixSize).forEach { i ->
                    val startPoint = Vector2(
                        x = i * 3 * unit + j * unit - matrixSize * unit,
                        y = -i * unit + j * 3 * unit
                    )

                    animatrix.find { it.first == j && it.second == i }?.third?.let { anim ->
                        anim.updateAnimation()
                        if (anim.hasAnimations().not()) {
                            if (i == matrixSize - 1 && j == matrixSize - 1) switch = switch.not()
                            anim.apply {
                                if (switch.not()) {
                                    ::rotationDegree.animate(90.0, duration, Easing.CubicInOut)
                                    ::rotationDegree.complete()
                                } else {
                                    ::rotationDegree.animate(0.0, duration, Easing.CubicInOut)
                                    ::rotationDegree.complete()
                                }
                            }
                        }
                        drawer.rotatedContour(startPoint = startPoint, degree = anim.rotationDegree, unit = unit)
                    }
                }
            }
        }
    }
}

fun plus(corner: Vector2, unit: Double): ShapeContour = contour {
    val corner1 = corner.copy(x = corner.x + unit)
    moveTo(corner1)
    val corner2 = corner.copy(x = corner1.x + unit)
    lineTo(corner2)
    val corner3 = corner2.copy(y = corner2.y + unit)
    lineTo(corner3)
    val corner4 = corner3.copy(x = corner3.x + unit)
    lineTo(corner4)
    val corner5 = corner4.copy(y = corner4.y + unit)
    lineTo(corner5)
    val corner6 = corner5.copy(x = corner5.x - unit)
    lineTo(corner6)
    val corner7 = corner6.copy(y = corner6.y + unit)
    lineTo(corner7)
    val corner8 = corner7.copy(x = corner7.x - unit)
    lineTo(corner8)
    val corner9 = corner8.copy(y = corner8.y - unit)
    lineTo(corner9)
    val corner10 = corner9.copy(x = corner9.x - unit)
    lineTo(corner10)
    val corner11 = corner10.copy(y = corner10.y - unit)
    lineTo(corner11)
    val corner12 = corner11.copy(x = corner11.x + unit)
    lineTo(corner12)
    close()
}

fun Drawer.rotatedContour(startPoint: Vector2, degree: Double, unit: Double) = isolated {
    translate(startPoint + 1.5 * unit)
    rotate(degree)
    translate(Vector2.ZERO - 1.5 * unit)
    contour(plus(Vector2.ZERO, unit))
}