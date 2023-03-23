package gravity

import org.openrndr.draw.Drawer

class PointCollector(private val points: List<Point>) {
    fun updateAll() = points.forEach { it.update() }

    fun connectAll() = points.forEach { outer ->
        points.forEach { inner ->
            outer.follow(inner)
        }
    }

    fun draw(drawer: Drawer, radius: Double) {
        points.forEach { outer ->
            points.forEach { inner ->
                drawer.lineSegment(inner.position, outer.position)
            }
            drawer.circle(outer.position, radius)
        }
    }
}

fun collect(vararg points: Point) = PointCollector(points.toList())