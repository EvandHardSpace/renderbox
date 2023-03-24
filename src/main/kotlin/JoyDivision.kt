import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.LIGHT_CORAL
import org.openrndr.extra.noise.Random
import org.openrndr.extra.shapes.operators.roundCorners
import org.openrndr.math.Vector2
import org.openrndr.shape.contour
import kotlin.math.*

private const val ITERATIONS = 300
fun main() = application {
    configure { height = 700; width = 700 }
    program {
        val startPoint = Vector2(x = 80.0, y = (height / 4.0))
        extend {
            drawer.apply {
                stroke = ColorRGBa.LIGHT_CORAL
                fill = ColorRGBa.BLACK
            }

            (0..70).forEach { i ->
                val line = line(
                    startPoint = startPoint.copy(y = startPoint.y + i * 5.0),
                    endPointX = width - 5.0
                )
                drawer.contour(line)
            }

            drawer.isolated {
                stroke = ColorRGBa.BLACK
                rectangle(Vector2.ZERO, width = 82.5, height = 1000.0)
                rectangle(x = width.toDouble() - 82.5, y = 0.0, width = 82.5, height = 1000.0)
                rectangle(x = 0.0, y = height - 180.0, width = 1000.0, height = 30.0)
            }
        }
    }
}

fun Program.line(startPoint: Vector2, endPointX: Double) = contour {

    Random.isolated {
        val moveSize = (endPointX - startPoint.x) / ITERATIONS
        moveTo(startPoint)
        (1 until ITERATIONS).forEach { i ->
            val distToCenter = startPoint.copy(x = startPoint.x + moveSize * i).distanceTo(mouse.position)

            lineTo(
                startPoint.copy(
                    x = startPoint.x + moveSize * i,
                    y = startPoint.y + simplex(
                        position = startPoint.copy(x = (startPoint.x + moveSize * i) / 20),
                    ).absoluteValue.unaryMinus() * 2000 / (distToCenter.pow(1.4).coerceAtLeast(20.0)) - 10
                )
            )
        }
    }
    lineTo(
        startPoint.copy(
            x = endPointX,
            y = startPoint.y
        )
    )
    close()
}.roundCorners(0.7)