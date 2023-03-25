package circular

import ext.gui
import org.openrndr.ApplicationBuilder
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.noise.perlin
import org.openrndr.extra.noise.valueHermite
import org.openrndr.extra.parameters.IntParameter
import org.openrndr.extra.shapes.grid
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
import org.openrndr.shape.*

private const val MIN_SIZE = 7.0

fun composableFigure(corner: Vector2 = Vector2.ZERO): Shape = Circle(corner, 200.0).shape
fun main() = application {
    configure {
        position = IntVector2(Int.MAX_VALUE, 0)
        width = 500; height = 500
        windowAlwaysOnTop = true
    }
    program()
}

fun ApplicationBuilder.program() = program {
    val settings = object {
        @IntParameter("random seed", 1, 1000)
        var seed = 0
    }
    //gui { add(settings, "settings") }
    val grid = drawer.bounds.grid(MIN_SIZE, MIN_SIZE).mapIndexed { y, rectangles ->
        rectangles.mapIndexed { x, rectangle ->
            IntVector2(x, y) to rectangle.corner
        }
    }.flatten()
    val figure = composableFigure(drawer.bounds.center)

    extend {
        drawer.apply {
            stroke = ColorRGBa.WHITE
            strokeWeight = 0.7
            fill = null
        }

        val mappedGrid: List<Processing> = grid
            .filter { it.second in figure }
            .map {
                Processing(
                    state = valueHermite(settings.seed, it.second * 0.018).discreteToStep(),
                    position = it.second,
                    gridPosition = it.first,
                )
            }

        mappedGrid.forEach {
            when (it.state) {
                Step.XL -> mappedGrid.markZone(it.gridPosition, 8)
                Step.L -> mappedGrid.markZone(it.gridPosition, 4)
                Step.M -> mappedGrid.markZone(it.gridPosition, 2)
                Step.S -> {}
                Step.DELETED -> {}
            }
        }

        mappedGrid
            .filterNot { it.state == Step.DELETED }
            .forEach {
//                drawer.isolated {
//                    drawer.fill = it.state.getColor()
//                    drawer.point(it.position)
//                }
                drawer.startCircle(it.position, it.state.getSize(MIN_SIZE))
            }
    }
}

fun List<Processing>.markZone(currentPosition: IntVector2, size: Int) {
    (0 until size).forEach { y ->
        (0 until size).forEach { x ->
            if (x == 0 && y == 0) return@forEach
            find { it.gridPosition == currentPosition + IntVector2(x, y) }?.state = Step.DELETED
        }
    }
}

fun Drawer.startCircle(corner: Vector2, radius: Double) = isolated {
    translate(Vector2(radius, radius))
    circle(corner, radius / 2)
}