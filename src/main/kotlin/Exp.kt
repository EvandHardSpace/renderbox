import ext.gui
import ext.program
import org.openrndr.extra.parameters.IntParameter

fun main() = program {
    val settings = object {
        @IntParameter("x", 0, 10000)
        var x: Int = 0
    }
    gui { add(settings, "settings") }
    extend {

    }
}