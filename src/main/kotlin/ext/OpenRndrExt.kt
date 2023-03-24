package ext

import org.openrndr.Program
import org.openrndr.application

fun onFrame(userDraw: Program.() -> Unit) = application {
    configure { width = 500; height = 500 }
    program {
        extend(userDraw = userDraw)
    }
}

fun program(init: suspend Program.() -> Unit) = application { program(init = init) }