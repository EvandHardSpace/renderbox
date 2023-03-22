import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.math.Vector2
import org.openrndr.shape.ShapeContour
import org.openrndr.shape.contour
import kotlin.math.cos

/**
 *  This is a template for a live program.
 *
 *  It uses oliveProgram {} instead of program {}. All code inside the
 *  oliveProgram {} can be changed while the program is running.
 */
const val unit = 25.0
const val iteration = 15

fun main() = application {
    configure {
        width = 800
        height = 800
    }
    program {
        extend {
            drawer.clear(ColorRGBa.PINK)

            (0 until iteration).forEach { j ->
                (0 until iteration).forEach { i ->
                    val startPoint = Vector2(
                        x = i * 3 * unit + j * unit - iteration * unit,
                        y = -i * unit + j * 3 * unit
                    )

                    val degree = cos(seconds + startPoint.x / 1500) * 90
                    drawer.rotatedContour(startPoint = startPoint, degree = degree, unit = unit)
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

fun Drawer.rotatedContour(startPoint: Vector2, degree: Double, unit: Double) {
    isolated {
        translate(startPoint + 1.5 * unit)
        rotate(degree)
        translate(Vector2.ZERO - 1.5 * unit)
        contour(plus(Vector2.ZERO, unit))
    }
}