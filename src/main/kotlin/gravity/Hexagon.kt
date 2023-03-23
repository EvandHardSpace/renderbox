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
        val point7 = Point(Vector2(0.0, 150.0), mass = mass)
        val collector = collect(
            point1,
            point2,
            point3,
            point4,
            point5,
            point6,
            point7,
        )

        extend {
            drawer.stroke = ColorRGBa.WHITE
            collector.updateAll()
            //collector.connectAll()

            point1.connectTo(point2)
            point2.connectTo(point3)
            point3.connectTo(point4)
            point4.connectTo(point5)
            point5.connectTo(point6)
            point6.connectTo(point7)

            drawer.lineSegment(point1.position, point2.position)
            drawer.lineSegment(point2.position, point3.position)
            drawer.lineSegment(point3.position, point4.position)
            drawer.lineSegment(point4.position, point5.position)
            drawer.lineSegment(point5.position, point6.position)
            drawer.lineSegment(point6.position, point7.position)

            point1.follow(mouse.position)
            collector.draw(drawer, 5.0)
        }
    }
}