package gravity

import org.openrndr.math.Vector2
import kotlin.math.atan2
import kotlin.math.cos
import kotlin.math.pow
import kotlin.math.sin

data class Point(
    var position: Vector2,
    var velocity: Vector2 = Vector2.ZERO,
    var acceleration: Vector2 = Vector2.ZERO,
    val mass: Double = 1.0
) {
    fun update() {
        val newVelocity = velocity + acceleration
        // Apply damping to the velocity
        val dampedVelocity = newVelocity - (velocity * damping)
        // Update the position using the new velocity
        position += dampedVelocity
        // Set the velocity to the new velocity
        velocity = dampedVelocity
        // Reset the acceleration
        acceleration = Vector2.ZERO
    }

    fun follow(other: Vector2) {
        position = other
    }

    fun follow(other: Point, springLength: Double) {
        if (other == this) return
        val distance = position.distanceTo(other.position)
        gravity(other, distance, equilibrium = distance > springLength)
    }

    fun connectTo(other: Point, dist: Double) {
        this.follow(other, dist)
        other.follow(this, dist)
    }

    private fun gravity(other: Point, distance: Double, equilibrium: Boolean) {
        val force = if (equilibrium) (other.mass * forceKoef) * (distance.pow(0.9))
        else (other.mass * forceKoef) / (distance.pow(0.9))

        val angle = atan2(other.position.y - position.y, other.position.x - position.x)
        val forceVector = Vector2(force * cos(angle), force * sin(angle))
        applyForce(if (equilibrium) forceVector else -forceVector)
    }

    private fun applyForce(force: Vector2) {
        acceleration += force / (mass * forceKoef)
    }
}