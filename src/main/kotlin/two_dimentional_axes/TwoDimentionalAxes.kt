package two_dimentional_axes

import ext.onFrame
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.Drawer
import org.openrndr.draw.isolated
import org.openrndr.extra.color.presets.LIGHT_CORAL
import org.openrndr.math.Vector2
import org.openrndr.math.Vector3
import org.openrndr.shape.contour

private const val FIELD_SIZE = 20
private const val HALF_SIZE = FIELD_SIZE / 2
private const val DIVISION_FACTOR = 10

fun main() = onFrame {
    drawer.stroke = ColorRGBa.WHITE
    drawer.translate(width / 2.0, height / 2.0)
    drawer.rotate(Vector3.UNIT_X, 180.0)
    drawer.drawField()
    fun Number.fitAxes(): Double = toDouble() * (width / FIELD_SIZE)

    val graph = contour {
        val halfSizeWithDivision = HALF_SIZE * DIVISION_FACTOR
        (-halfSizeWithDivision until halfSizeWithDivision).forEach { i ->
            val x = i / DIVISION_FACTOR.toDouble()
            val point = Vector2(x.fitAxes(), f(x).fitAxes())
            if (i == -halfSizeWithDivision) moveTo(point)
            else lineTo(point)
        }
    }

    drawer.isolated {
        stroke = ColorRGBa.LIGHT_CORAL
        contour(graph)
    }
}

fun Drawer.drawField() {
    //axes
    lineSegment(Vector2.ZERO, Vector2(-width / 2.0, 0.0))
    lineSegment(Vector2.ZERO, Vector2(width / 2.0, 0.0))
    lineSegment(Vector2.ZERO, Vector2(0.0, -height / 2.0))
    lineSegment(Vector2.ZERO, Vector2(0.0, height / 2.0))

    //divisions
    (-HALF_SIZE until HALF_SIZE).forEach {
        lineSegment(
            Vector2(it * (width.toDouble() / FIELD_SIZE), -3.0),
            Vector2(it * (width.toDouble() / FIELD_SIZE), 3.0)
        )
        lineSegment(
            Vector2(-3.0, it * (height.toDouble() / FIELD_SIZE)),
            Vector2(3.0, it * (height.toDouble() / FIELD_SIZE))
        )
    }

    //arrows
    val cornerY = Vector2(0.0, height / 2.0)
    val yArrow = contour {
        moveTo(cornerY)
        lineTo(cornerY + Vector2(-6.0, -8.0))
        lineTo(cornerY)
        lineTo(cornerY + Vector2(6.0, -8.0))
    }

    val cornerX = Vector2(width / 2.0, 0.0)
    val xArrow = contour {
        moveTo(cornerX)
        lineTo(cornerX + Vector2(-8.0, -6.0))
        lineTo(cornerX)
        lineTo(cornerX + Vector2(-8.0, 6.0))
    }
    contour(xArrow)
    contour(yArrow)
}
