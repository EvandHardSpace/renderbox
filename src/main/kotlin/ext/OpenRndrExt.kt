package ext

import org.openrndr.Program
import org.openrndr.application

fun onFrame(userDraw: Program.() -> Unit) = application {
    program {
        extend(userDraw = userDraw)
    }
}

fun program(init: suspend Program.() -> Unit) = application { program(init = init) }