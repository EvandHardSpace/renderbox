package two_dimentional_axes

import org.openrndr.Program
import kotlin.math.cos
import kotlin.math.sin

fun Program.f(x: Double) = sin(x * 2 * cos(seconds * 2))