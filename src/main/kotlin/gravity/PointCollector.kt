package gravity

import org.openrndr.draw.Drawer

class PointCollector(private val points: List<Point>) {
    fun updateAll() = points.forEach { it.update() }

    fun connectAll() = points.forEach { outer ->
        points.forEach { outer.follow(it, 50.0) }
    }

//    fun connectSequentially() {
//        (0..points.size - 2).forEach { i ->
//            points[i].connectTo(points[i + 1])
//        }
//    }

    fun drawAll(drawer: Drawer, radius: Double) {
        points.forEach { outer ->
            points.forEach { inner ->
                drawer.lineSegment(inner.position, outer.position)
            }
            drawer.circle(outer.position, radius)
        }
    }

    fun drawSequentially(drawer: Drawer, radius: Double) {

        (0..points.size - 2).forEach { i ->
            drawer.lineSegment(points[i].position, points[i + 1].position)
            drawer.circle(points[i].position, radius)
        }
        drawer.circle(points.last().position, radius)
    }
}