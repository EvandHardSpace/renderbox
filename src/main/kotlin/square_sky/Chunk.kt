package square_sky

import org.openrndr.math.IntVector2
import org.openrndr.math.Vector2
import kotlin.math.pow

@Suppress("LABEL_NAME_CLASH")
class Chunk(
    private val chunkField: List<List<RawField>>,
    private val startIndex: IntVector2,
    private val depth: Int,
    private val maxDepth: Int = 2,
    private val lengthUnit: Double,
) {
    private val size = 2.0.pow(maxDepth - depth).toInt()

    private var node1: Chunk? = null
    private var node2: Chunk? = null
    private var node3: Chunk? = null
    private var node4: Chunk? = null

    init {
        init()
    }

    private val average: Double
        get() {
            var avg = 0.0
            chunkField.forEachIndexed { y, line ->
                if (y >= startIndex.y + size || y < startIndex.y) return@forEachIndexed
                line.forEachIndexed { x, element ->
                    if (x >= startIndex.x + size || x < startIndex.x) return@forEachIndexed
                    avg += element.value
                }
            }
            return avg / (size * size)
        }

    private var hasChild: Boolean = false

    private val range: ClosedFloatingPointRange<Double>
        get() = when (depth) {
            0 -> -1.0..-0.5
            1 -> -0.5..-0.2
            2 -> -0.2..0.0
            3 -> 0.0..0.3
            4 -> 0.3..1.0
            //else -> error("unsupported depth")
            else -> -1.0..-0.4
        }

    private fun init() {
        if (depth > maxDepth || average in range) return
        hasChild = true

        val startIndex1 = startIndex
        node1 = Chunk(
            chunkField = chunkField,
            startIndex = startIndex1,
            depth = depth + 1,
            maxDepth = maxDepth,
            lengthUnit = lengthUnit,
        )

        val startIndex2 = startIndex.copy(x = startIndex.x + size / 2)
        node2 = Chunk(
            chunkField = chunkField,
            startIndex = startIndex2,
            depth = depth + 1,
            maxDepth = maxDepth,
            lengthUnit = lengthUnit,
        )

        val startIndex3 = startIndex.copy(y = startIndex.y + size / 2)
        node3 = Chunk(
            chunkField = chunkField,
            startIndex = startIndex3,
            depth = depth + 1,
            maxDepth = maxDepth,
            lengthUnit = lengthUnit,
        )

        val startIndex4 = startIndex.copy(x = startIndex.x + size / 2, y = startIndex.y + size / 2)
        node4 = Chunk(
            chunkField = chunkField,
            startIndex = startIndex4,
            depth = depth + 1,
            maxDepth = maxDepth,
            lengthUnit = lengthUnit,
        )
    }

    fun draw(onDraw: (position: Vector2, size: Double) -> Unit) {
       // if (depth != 5 && hasChild) onDraw(chunkField[startIndex.y][startIndex.x].position, lengthUnit)
        if (hasChild.not()) {
            onDraw(chunkField[startIndex.y][startIndex.x].position, size * lengthUnit)
            return
        }
        node1?.draw(onDraw)
        node2?.draw(onDraw)
        node3?.draw(onDraw)
        node4?.draw(onDraw)
    }
}

fun chunks(initField: List<List<RawField>>, maxDepth: Int = 4): List<Chunk> = buildList {
    var countOfRootChunks = initField.size
    repeat(maxDepth) { countOfRootChunks /= 2 }
    repeat(countOfRootChunks) { y ->
        repeat(countOfRootChunks) { x ->
            Chunk(
                chunkField = initField,
                startIndex = IntVector2(initField.size * x, initField.size * y) / countOfRootChunks,
                depth = 0,
                maxDepth = maxDepth,
                lengthUnit = 30.0
            ).run(::add)
        }
    }
}