package org.kakara.engine.physics.collision;

import org.kakara.engine.Camera;
import org.kakara.engine.components.Component;
import org.kakara.engine.math.Vector3;

/**
 * The PhysicsComponent handles velocity, acceleration, and collision resolution.
 */
public class PhysicsComponent extends Component {
    private Vector3 velocity;
    private Vector3 acceleration;

    private boolean resolve;

    @Override
    public void start() {
        this.velocity = new Vector3();
        this.acceleration = new Vector3();
        this.resolve = true;
    }

    @Override
    public void update() {
    }

    @Override
    public void physicsUpdate(float deltaTime) {
        velocity.addMut(acceleration.getX() * deltaTime, acceleration.getY() * deltaTime, acceleration.getZ() * deltaTime);
        getGameItem().transform.translateBy(velocity.getX() * deltaTime, 0, 0);
        if (resolve)
            getGameItem().getComponent(ColliderComponent.class).updateZ();
        getGameItem().transform.translateBy(0, 0, velocity.getZ() * deltaTime);
        if (resolve)
            getGameItem().getComponent(ColliderComponent.class).updateZ();
        getGameItem().transform.translateBy(0, velocity.getY() * deltaTime, 0);
        if (resolve)
            getGameItem().getComponent(ColliderComponent.class).updateY();
    }

    /**
     * Set the velocity in the x direction.
     *
     * @param x The x velocity.
     */
    public void setVelocityX(float x) {
        this.velocity.x = x;
    }

    /**
     * Set the velocity in the y direction.
     *
     * @param y The y velocity.
     */
    public void setVelocityY(float y) {
        this.velocity.y = y;
    }

    /**
     * Set the velocity in the z direction.
     *
     * @param z The z velocity.
     */
    public void setVelocityZ(float z) {
        this.velocity.z = z;
    }

    /**
     * Set the velocity according to the direction of the camera.
     * <p>A velocity of &#60;0, 0, 1&#62; will move the object in the forward direction from the point of view of the camera.</p>
     *
     * @param velocity The velocity.
     * @param camera   The camera.
     */
    public void setVelocityByCamera(Vector3 velocity, Camera camera) {
        if (velocity.z != 0) {
            this.velocity.x = (float) Math.sin(Math.toRadians(camera.getRotation().y)) * -1.0f * velocity.z;
            this.velocity.z = (float) Math.cos(Math.toRadians(camera.getRotation().y)) * velocity.z;
        }
        if (velocity.x != 0) {
            this.velocity.x = (float) Math.sin(Math.toRadians(camera.getRotation().y - 90)) * -1.0f * velocity.x;
            this.velocity.z = (float) Math.cos(Math.toRadians(camera.getRotation().y - 90)) * velocity.x;
        }
        this.velocity.y = velocity.y;
    }

    /**
     * Get the velocity of the item.
     *
     * @return The velocity.
     */
    public Vector3 getVelocity() {
        return velocity;
    }

    /**
     * Set the overall velocity for the item.
     *
     * @param velocity The velocity.
     */
    public void setVelocity(Vector3 velocity) {
        this.velocity.set(velocity);
    }

    /**
     * Add to the current acceleration.
     *
     * @param acceleration The vector to add to the acceleration.
     */
    public void applyAcceleration(Vector3 acceleration) {
        this.acceleration.add(acceleration);
    }

    /**
     * Get the acceleration.
     *
     * @return The acceleration.
     */
    public Vector3 getAcceleration() {
        return acceleration;
    }

    /**
     * Set the acceleration of the item.
     *
     * @param acceleration The acceleration.
     */
    public void setAcceleration(Vector3 acceleration) {
        this.acceleration.set(acceleration);
    }

    /**
     * Set if a collision conflict should be resolved.
     *
     * @param resolve if a collision conflict should be resolved.
     */
    public void setResolve(boolean resolve) {
        this.resolve = resolve;
    }

    /**
     * If collision conflicts should be resolved.
     *
     * @return If the collision conflict should be resolved.
     */
    public boolean shouldResolve() {
        return resolve;
    }

    @Override
    public String toString() {
        return "PhysicsComponent{" +
                "velocity=" + velocity.toString() +
                ", acceleration=" + acceleration.toString() +
                ", resolve=" + resolve +
                '}';
    }
}
