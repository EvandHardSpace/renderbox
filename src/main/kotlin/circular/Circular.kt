package circular

import org.openrndr.ApplicationBuilder
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.LIGHT_CORAL
import org.openrndr.extra.noise.valueHermite
import org.openrndr.extra.shapes.grid
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
import org.openrndr.shape.Circle
import org.openrndr.shape.Shape
import org.openrndr.shape.contains

private const val SEED = 100

fun composableFigure(corner: Vector2 = Vector2.ZERO): Shape = Circle(corner, 2500.0).shape
fun main() = application {
    configure {
        position = IntVector2(Int.MAX_VALUE, 0)
        width = 640; height = 640
    }
    program()
}

fun ApplicationBuilder.program() = program {
    val figure = composableFigure(drawer.bounds.center)
    val grid = drawer.bounds.grid(512, 512).map { y ->
        y.map { x ->
            RawField(
                valueHermite(SEED, x.corner * 0.025),
                x.corner
            )
        }
    }

    val chunks = chunks(grid)

    extend {
        drawer.apply {
            stroke = ColorRGBa.WHITE
            strokeWeight = 0.7
            fill = null
        }

        chunks.forEach { chunk ->
            chunk.draw({ position, size ->
                drawer.isolated {
                    stroke = ColorRGBa.LIGHT_CORAL
                    fill = ColorRGBa.BLACK
                    rectangle(position, size)
                }
            }) { it in figure }
        }
    }
}