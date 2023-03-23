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
        val distance = position.distanceTo(other)
        val force = (distance.pow(2)) / forceKoef
        val angle = atan2(other.y - position.y, other.x - position.x)
        applyForce(Vector2(force * cos(angle), force * sin(angle)))
    }

    fun follow(other: Point) {
        if (other == this) return
        val distance = position.distanceTo(other.position)
        gravity(other, distance, positive = distance >= nodeDistance)
    }

    fun connectTo(other: Point) {
        this.follow(other)
        other.follow(this)
    }

    private fun gravity(other: Point, distance: Double, positive: Boolean = true) {
        val force = if (distance <= nodeDistance) other.mass * forceKoef / (distance.pow(2))
        else ((distance - nodeDistance).pow(2)) / (other.mass * forceKoef)

        val angle = atan2(other.position.y - position.y, other.position.x - position.x)
        val forceVector = Vector2(force * cos(angle), force * sin(angle))
        applyForce(if (positive) forceVector else -forceVector)
    }

    private fun applyForce(force: Vector2) {
        acceleration += force / mass
    }
}