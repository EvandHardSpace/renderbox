package circular

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

    //if it is root element we should not divide chunks by 2
    private val halfSize = size / 2

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
            0 -> -1.0..-0.75
            1 -> -0.75..-0.5
            2 -> -0.5..0.0
            3 -> 0.0..0.5
            4 -> 0.5..1.0
            //else -> error("unsupported depth")
            else -> 0.5..1.0
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

        val startIndex2 = startIndex.copy(x = startIndex.x + halfSize)
        node2 = Chunk(
            chunkField = chunkField,
            startIndex = startIndex2,
            depth = depth + 1,
            maxDepth = maxDepth,
            lengthUnit = lengthUnit,
        )

        val startIndex3 = startIndex.copy(y = startIndex.y + halfSize)
        node3 = Chunk(
            chunkField = chunkField,
            startIndex = startIndex3,
            depth = depth + 1,
            maxDepth = maxDepth,
            lengthUnit = lengthUnit,
        )

        val startIndex4 = startIndex.copy(x = startIndex.x + halfSize, y = startIndex.y + halfSize)
        node4 = Chunk(
            chunkField = chunkField,
            startIndex = startIndex4,
            depth = depth + 1,
            maxDepth = maxDepth,
            lengthUnit = lengthUnit,
        )
    }

    fun draw(onDraw: (position: Vector2, size: Double) -> Unit, isValid: (Vector2) -> Boolean) {
        if (isValid(chunkField[startIndex.y][startIndex.x].position).not()) return
        if (depth != 5 && hasChild) onDraw(chunkField[startIndex.y][startIndex.x].position, lengthUnit)
        if (hasChild.not()) {
            onDraw(chunkField[startIndex.y][startIndex.x].position, size * lengthUnit)
            return
        }
        node1?.draw(onDraw, isValid)
        node2?.draw(onDraw, isValid)
        node3?.draw(onDraw, isValid)
        node4?.draw(onDraw, isValid)
    }
}

fun chunks(initField: List<List<RawField>>, maxDepth: Int = 5): List<Chunk> = buildList {
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