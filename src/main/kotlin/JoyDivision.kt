import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.extra.noise.Random
import org.openrndr.extra.shapes.operators.roundCorners
import org.openrndr.math.Vector2
import org.openrndr.shape.contour
import kotlin.math.*

private const val ITERATIONS = 300
fun main() = application {
    configure { height = 500; width = 700 }
    program {
        val startPoint = Vector2(x = -4.0, y = (height / 3.3))
        //extend(ScreenRecorder()) { contentScale = 1.5 }
        extend {
            drawer.apply {
                stroke = ColorRGBa.BLACK
                strokeWeight = 1.1
                fill = ColorRGBa.WHITE
            }

            (0..59).forEach { i ->
                val line = line(
                    startPoint = startPoint.copy(y = startPoint.y + i * 6.0),
                    endPointX = width + 4.0
                )
                drawer.contour(line)
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