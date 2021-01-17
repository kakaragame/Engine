package org.kakara.engine.physics;

import org.kakara.engine.Camera;
import org.kakara.engine.math.Vector3;

/**
 * An item that can be interacted with by the Physics engine.
 * <p>It should be noted that the SI units are used here.</p>
 * <p>Velocity units are m/s, and Acceleration units are m/s^2.</p>
 *
 * @since 1.0-Pre2
 */
public interface PhysicsItem {
    /**
     * Set the velocity in the x direction.
     *
     * @param x The x velocity.
     */
    void setVelocityX(float x);

    /**
     * Set the velocity in the y direction.
     *
     * @param y The y velocity.
     */
    void setVelocityY(float y);

    /**
     * Set the velocity in the z direction.
     *
     * @param z The z velocity.
     */
    void setVelocityZ(float z);

    /**
     * Set the velocity according to the direction of the camera.
     * <p>A velocity of &#60;0, 0, 1&#62; will move the object in the forward direction from the point of view of the camera.</p>
     *
     * @param velocity The velocity.
     * @param camera   The camera.
     */
    void setVelocityByCamera(Vector3 velocity, Camera camera);

    /**
     * Get the velocity of the item.
     *
     * @return The velocity.
     */
    Vector3 getVelocity();

    /**
     * Set the overall velocity for the item.
     *
     * @param velocity The velocity.
     */
    void setVelocity(Vector3 velocity);

    /**
     * Add to the current acceleration.
     *
     * @param acceleration The vector to add to the acceleration.
     */
    void applyAcceleration(Vector3 acceleration);

    /**
     * Get the acceleration.
     *
     * @return The acceleration.
     */
    Vector3 getAcceleration();

    /**
     * Set the acceleration of the item.
     *
     * @param acceleration The acceleration.
     */
    void setAcceleration(Vector3 acceleration);
}
