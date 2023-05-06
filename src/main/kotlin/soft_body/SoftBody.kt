package soft_body

import org.openrndr.MouseButton
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.extra.color.presets.LIGHT_CORAL
import org.openrndr.extra.shapes.operators.roundCorners
import org.openrndr.math.Vector2
import org.openrndr.shape.*
import kotlin.math.PI
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.sin


data class PointMass(
    var position: Vector2,
    var velocity: Vector2 = Vector2.ZERO,
    val mass: Double = 1.0,
    var gravityAcceleration: Vector2 = Vector2.ZERO
)

data class Spring(val a: PointMass, val b: PointMass, val restLength: Double, val stiffness: Double = 0.5)

fun main() = application {
    configure {
        width = 800
        height = 800
    }

    program {
        val pointMasses = mutableListOf<PointMass>()
        val springs = mutableListOf<Spring>()

        val gravity = Vector2(0.0, 40.0)
        pointMasses.forEach { it.gravityAcceleration = gravity }

        // Create a sphere of point masses and connect them with springs
        val sphereRadius = 100.0
        val centerX = width / 2.0
        val centerY = height / 2.0
        val numLayers = 3
        val numPointsPerLayer = 7

        for (i in 0 until numLayers) {
            val layerRadius = sphereRadius * sin((PI / (numLayers + 1)) * (i + 1))
            for (j in 0 until numPointsPerLayer) {
                val angle = (2 * PI / numPointsPerLayer) * j
                val x = centerX + layerRadius * cos(angle)
                val y = centerY + layerRadius * sin(angle)
                val pointMass = PointMass(Vector2(x, y), gravityAcceleration = gravity)
                pointMasses.add(pointMass)

                // Connect with previous layer
                if (i > 0) {
                    val prevLayer = pointMasses.subList((i - 1) * numPointsPerLayer, i * numPointsPerLayer)
                    prevLayer.forEach { springs.add(Spring(pointMass, it, (it.position - pointMass.position).length)) }
                }

                // Connect with previous point in the same layer
                if (j > 0) {
                    val restLength = 2 * layerRadius * sin(PI / (2 * numPointsPerLayer))
                    val prevPointInLayer = pointMasses[pointMasses.size - 2]
                    springs.add(Spring(pointMass, prevPointInLayer, restLength))
                }
            }
            // Connect last point to the first in the same layer
            val firstPointMassInLayer = pointMasses[pointMasses.size - numPointsPerLayer]
            val restLength = 2 * layerRadius * sin(PI / (2 * numPointsPerLayer))
            springs.add(Spring(pointMasses.last(), firstPointMassInLayer, restLength))
        }

        ////////////

        var selectedPointMass: PointMass? = null
        val mouseRadius = 20.0

        mouse.buttonDown.listen {
            val mousePosition = Vector2(it.position.x, it.position.y)
            selectedPointMass = pointMasses.minByOrNull { selector -> (selector.position - mousePosition).squaredLength }
        }

        mouse.buttonUp.listen { event ->
            if (event.button == MouseButton.LEFT) {
                selectedPointMass?.let {
                    it.velocity = Vector2.ZERO
                    it.gravityAcceleration = gravity
                }
                selectedPointMass = null
            }
        }

        fun Drawer.updatePointMasses(deltaTime: Double) {
            val dragCoefficient = 0.2
            val borderBounceFactor = 0.2
            for (pointMass in pointMasses) {
                if (pointMass != selectedPointMass) {
                    pointMass.velocity += pointMass.gravityAcceleration * deltaTime
                    pointMass.velocity *= 1.0 - dragCoefficient
                    pointMass.position += pointMass.velocity * deltaTime

                    // Bounce off the borders
                    if (pointMass.position.x < 0.0 || pointMass.position.x > width.toDouble()) {
                        pointMass.velocity = Vector2(-pointMass.velocity.x * borderBounceFactor, pointMass.velocity.y)
                    }
                    if (pointMass.position.y < 0.0 || pointMass.position.y > height.toDouble()) {
                        pointMass.velocity = Vector2(pointMass.velocity.x, -pointMass.velocity.y * borderBounceFactor)
                    }

                    pointMass.position = pointMass.position.clamp(Rectangle(Vector2.ZERO, width.toDouble(), height.toDouble()))
                }
            }
        }

        fun updateSprings() {
            for (spring in springs) {
                val delta = spring.b.position - spring.a.position
                val distance = delta.length
                val restLengthRatio = (distance - spring.restLength) / distance
                val force = delta * (spring.stiffness * restLengthRatio)

                spring.a.velocity += force / spring.a.mass
                spring.b.velocity -= force / spring.b.mass
            }
        }

        fun Drawer.drawSprings() {
            stroke = ColorRGBa.WHITE
            for (spring in springs) {
                lineSegment(spring.a.position, spring.b.position)
            }
        }

        fun Drawer.drawPointMasses() {
//            fill = ColorRGBa.PINK
//            for (pointMass in pointMasses) {
//                circle(pointMass.position, mouseRadius / 2.0)
//            }
        }

        fun Drawer.renderSoftBody() {
            val convexHull = grahamScan(pointMasses.map(PointMass::position))
            val smoothedShape = createRoundedShape(convexHull, 20.0)
            drawOneColorShape(smoothedShape, ColorRGBa.LIGHT_CORAL)
        }

        extend {
            drawer.run {
                clear(ColorRGBa.WHITE)
                updatePointMasses(0.45)
                updateSprings()

                selectedPointMass?.position = mouse.position

                renderSoftBody()
            }
        }
    }
}

fun grahamScan(points: List<Vector2>): List<Vector2> {
    if (points.size <= 3) return points

    val sortedPoints = points.sortedWith(compareBy({ it.y }, { it.x }))
    val pivot = sortedPoints.first()

    val sortedByAngle = sortedPoints.drop(1).sortedBy {
        atan2((it.y - pivot.y), (it.x - pivot.x))
    }

    val hull = mutableListOf(pivot, sortedByAngle[0], sortedByAngle[1])

    for (i in 2 until sortedByAngle.size) {
        var top = hull.removeLast()
        while (hull.isNotEmpty() && crossProduct(hull.last(), top, sortedByAngle[i]) <= 0) {
            top = hull.removeLast()
        }
        hull.add(top)
        hull.add(sortedByAngle[i])
    }

    return hull
}

fun crossProduct(o: Vector2, a: Vector2, b: Vector2): Double {
    return (a.x - o.x) * (b.y - o.y) - (a.y - o.y) * (b.x - o.x)
}

fun Drawer.drawOneColorShape(contour: ShapeContour, color: ColorRGBa) {
    val compositionDrawer = CompositionDrawer()
    compositionDrawer.fill = color
    compositionDrawer.stroke = null
    compositionDrawer.contour(contour)
    composition(compositionDrawer.composition)
}

fun createRoundedShape(points: List<Vector2>, radius: Double): ShapeContour {
    val contour = ShapeContour.fromPoints(points, closed = true)
    return contour.roundCorners(radius)
}