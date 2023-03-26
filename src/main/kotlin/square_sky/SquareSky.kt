package square_sky

import org.openrndr.ApplicationBuilder
import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.BLUE_STEEL
import org.openrndr.extra.noise.perlinHermite
import org.openrndr.extra.shapes.grid
import org.openrndr.math.IntVector2
import org.openrndr.math.Vector3

private const val SEED = 100

fun main() = application {
    configure {
        position = IntVector2(Int.MAX_VALUE, 0)
        width = 640; height = 640
    }
    program()
}

fun ApplicationBuilder.program() = program {
    extend {
        drawer.clear(ColorRGBa.BLUE_STEEL)
        drawer.apply {
            stroke = ColorRGBa.WHITE
            strokeWeight = 0.7
            fill = null
        }

        val grid = drawer.bounds.grid(128, 128).map { y ->
            y.map { x ->
                RawField(
                    perlinHermite(SEED, Vector3(x.corner.x * 0.007, x.corner.y * 0.007, seconds * 0.1)),
                    x.corner
                )
            }
        }

        val chunks = chunks(grid)

        chunks.forEach { chunk ->
            chunk.draw { position, size ->
                drawer.isolated {
                    stroke = ColorRGBa.WHITE
                    fill = ColorRGBa.BLUE_STEEL
                    rectangle(position, size)
                }
            }
        }
    }
}