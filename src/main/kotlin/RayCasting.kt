import org.openrndr.Program
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.LineCap
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.LIGHT_CORAL
import org.openrndr.math.Vector2
import org.openrndr.shape.LineSegment
import org.openrndr.shape.ShapeContour
import org.openrndr.shape.intersections
import kotlin.random.Random

private const val ITERATION = 60
private const val DEGREE = 360.0 / ITERATION

fun main() = application {
    configure { width = 800; height = 800 }
    program {
        var collector = randomCollector()
        extend {
            drawer.apply {
                clear(ColorRGBa.BLACK)
                stroke = ColorRGBa.LIGHT_CORAL
                strokeWeight = 0.7
            }
            mouse.buttonDown.listen { collector = randomCollector() }

            (0 until ITERATION).forEach { i ->
                val endPoint = (mouse.position + Vector2(1500.0, 1500.0)).rotate(i * DEGREE, mouse.position)
                val ray = LineSegment(
                    start = mouse.position,
                    end = (mouse.position + Vector2(1500.0, 1500.0)).rotate(i * DEGREE, mouse.position)
                ).contour
                drawer.contour(ray)
                collector.drawTransparentMasks(drawer, ray, endPoint)
            }

            collector.drawBarriers(drawer)

            drawer.isolated {
                fill = ColorRGBa.BLACK
                strokeWeight = 0.7
                stroke = ColorRGBa.LIGHT_CORAL
                circle(mouse.position, 20.0)
            }
        }
    }
}

private class LineCollector(private val barriers: List<ShapeContour>) {

    fun drawTransparentMasks(drawer: Drawer, ray: ShapeContour, endPoint: Vector2) = barriers.forEach { barrier ->
        ray.contour.intersections(barrier.contour).forEach {
            drawer.isolated {
                stroke = ColorRGBa.BLACK
                strokeWeight = 2.0
                lineSegment(start = it.position, end = endPoint)
            }
        }
    }

    fun drawBarriers(drawer: Drawer) = drawer.isolated {
        strokeWeight = 2.0
        lineCap = LineCap.ROUND
        barriers.forEach(drawer::contour)
    }
}

private fun collect(lines: List<ShapeContour>) = LineCollector(lines)

private fun Program.randomCollector() = collect(
    buildList {
        repeat(5) {
            LineSegment(
                start = Vector2(
                    x = Random.nextDouble(0.0, width.toDouble()),
                    y = Random.nextDouble(0.0, height.toDouble())
                ),
                end = Vector2(
                    x = Random.nextDouble(0.0, width.toDouble()),
                    y = Random.nextDouble(0.0, height.toDouble())
                )
            ).contour.run(::add)
        }
    }
)

