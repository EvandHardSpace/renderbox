package ext

import org.openrndr.Program
import org.openrndr.application
import org.openrndr.extra.gui.GUI

fun onFrame(userDraw: Program.() -> Unit) = application {
    configure { width = 500; height = 500 }
    program {
        extend(userDraw = userDraw)
    }
}

fun program(init: suspend Program.() -> Unit) = application { program(init = init) }

fun Program.gui(configure: GUI.() -> Unit) = GUI().apply(configure).run(::extend)