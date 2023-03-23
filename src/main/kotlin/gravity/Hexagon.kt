package gravity

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.math.Vector2

fun main() = application {
    configure { width = 800; height = 800 }
    program {
        val point1 = Point(Vector2.ZERO, mass = mass)
        val point2 = Point(Vector2(50.0, 0.0), mass = mass)
        val point3 = Point(Vector2(100.0, 0.0), mass = mass)

        val point4 = Point(Vector2(0.0, 50.0), mass = mass)
        val point5 = Point(Vector2(50.0, 50.0))
        val point6 = Point(Vector2(100.0, 50.0), mass = mass)
        val point7 = Point(Vector2(0.0, 100.0), mass = mass)
        val collector = PointCollector(
            listOf(
                point1,
                point2,
                point3,
                point4,
                point5,
                point6,
                point7,
            )
        )

        extend {
            drawer.stroke = ColorRGBa.WHITE
            collector.updateAll()
            collector.connectAll()

            point1.follow(mouse.position)
            collector.drawAll(drawer, 5.0)
        }
    }
}