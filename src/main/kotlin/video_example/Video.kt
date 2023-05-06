package video_example

import org.openrndr.application
import org.openrndr.color.ColorRGBa
import org.openrndr.draw.ColorBufferShadow
import org.openrndr.extra.shapes.grid
import org.openrndr.ffmpeg.VideoPlayerFFMPEG
import org.openrndr.ffmpeg.loadVideo
import org.openrndr.math.Vector2

fun main() = application {
    program {
        val video = VideoPlayerFFMPEG.fromFile("data/videos/clouds.mov")

        val grid = drawer.bounds.grid(10.0, 10.0).map { y ->
            y.map { x -> x.corner }
        }.flatten()

        var map: List<Pair<Vector2, ColorRGBa>> = emptyList()

        video.newFrame.listen { frame ->
            map = grid.map {
                val frameColor =  frame.frame.shadow[it.x.toInt(), it.y.toInt()]
                println(frameColor)
                it to frameColor
            }
        }
        video.play()

        extend {
            drawer.clear(ColorRGBa.WHITE)
            video.draw(drawer, blind = true)

            map.forEach {
                drawer.fill = it.second
                drawer.circle(it.first, 5.0)
            }
        }
    }
}