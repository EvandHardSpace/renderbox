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
//        val point5 = Point(Vector2(50.0, 50.0))
//        val point6 = Point(Vector2(100.0, 50.0), mass = mass)
//        val point7 = Point(Vector2(0.0, 100.0), mass = mass)
        val collector = PointCollector(
            listOf(
                point1,
                point2,
                point3,
                point4,
//                point5,
//                point6,
//                point7,
            )
        )



        extend {
            drawer.stroke = ColorRGBa.WHITE
            repeat(1) {
                collector.updateAll()
//                point1.follow(point2, 50.0)
//                point2.follow(point3, 50.0)
//                point2.follow(point1, 50.0)
//                point3.follow(point1, 50.0)
                point1.follow(mouse.position)

                point1.connectTo(point2, 50.0)
                point1.connectTo(point3, 50.0)
                point2.connectTo(point4, 50.0)
                point3.connectTo(point4, 50.0)

//                collector.connectAll()
            }

            collector.drawAll(drawer, 5.0)
        }
    }
}