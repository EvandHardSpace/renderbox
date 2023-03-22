import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.LineCap
import org.openrndr.math.Vector2
import org.openrndr.shape.Circle
import org.openrndr.shape.LineSegment
import kotlin.math.cos

fun main() = application {
    program {
        extend {
            drawer.clear(ColorRGBa.PINK)
            drawer.strokeWeight = 1.0
            drawer.lineCap = LineCap.ROUND
            drawer.fill = null

            val center = Vector2(width / 2.0, height / 2.0)

            val line = LineSegment(Vector2(0.0, 0.0), center)

            val count = 50
            val degree = 360.0 / count

            val circle = Circle(center, height / 2 * cos(seconds))

            (0 until count).forEach {
                val newLine = line.rotate(degree * it, 0.0)
                val tangent = circle.tangents(newLine.start).first

                drawer.lineSegment(newLine.start, tangent)
            }

            drawer.strokeWeight = 1.3
            drawer.circle(circle)
        }
    }
}