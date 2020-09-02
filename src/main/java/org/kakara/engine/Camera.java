package org.kakara.engine;

import org.joml.Matrix4f;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.render.Transformation;

/**
 * Handles the movement/rotation of the viewport.
 */
public class Camera {
    private final Vector3 position;
    private final Vector3 rotation;

    private Matrix4f viewMatrix;

    public Camera() {
        position = new Vector3(0, 0, 0);
        rotation = new Vector3(0, 0, 0);
        viewMatrix = new Matrix4f();
    }

    public Camera(Vector3 position, Vector3 rotation) {
        this.position = position;
        this.rotation = rotation;
        viewMatrix = new Matrix4f();
    }

    /**
     * Get the position of the camera
     *
     * @return The position
     */
    public Vector3 getPosition() {
        return position.clone();
    }

    /**
     * Set the position of the camera
     *
     * @param position The position vector.
     */
    public void setPosition(Vector3 position) {
        this.position.x = position.x;
        this.position.y = position.y;
        this.position.z = position.z;
    }

    /**
     * Set the position of the camera
     *
     * @param x The x value
     * @param y The y value
     * @param z The z value
     */
    public void setPosition(float x, float y, float z) {
        position.x = x;
        position.y = y;
        position.z = z;
    }

    /**
     * Move the position of the camera
     *
     * @param offsetX x offset
     * @param offsetY y offset
     * @param offsetZ z offset
     */
    public void movePosition(float offsetX, float offsetY, float offsetZ) {
        if (offsetZ != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offsetZ;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offsetZ;
        }
        if (offsetX != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    /**
     * Move the position of the camera
     *
     * @param offset The vector offset
     */
    public void movePosition(Vector3 offset) {
        if (offset.z != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y)) * -1.0f * offset.z;
            position.z += (float) Math.cos(Math.toRadians(rotation.y)) * offset.z;
        }
        if (offset.x != 0) {
            position.x += (float) Math.sin(Math.toRadians(rotation.y - 90)) * -1.0f * offset.x;
            position.z += (float) Math.cos(Math.toRadians(rotation.y - 90)) * offset.x;
        }
        position.y += offset.y;
    }

    /**
     * Set the rotation of the camera.
     * <p>Eular rotations are used.</p>
     *
     * @param x X angle
     * @param y Y angle
     * @param z Z angle.
     */
    public void setRotation(float x, float y, float z) {
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    /**
     * Move the rotation around.
     *
     * @param offsetX The x offset
     * @param offsetY The y offset
     * @param offsetZ The z offset
     */
    public void moveRotation(float offsetX, float offsetY, float offsetZ) {
        rotation.x += offsetX;
        rotation.y += offsetY;
        rotation.z += offsetZ;
    }

    /**
     * Move the rotation around
     *
     * @param offset The offset vector
     */
    public void moveRotation(Vector3 offset) {
        rotation.x += offset.x;
        rotation.y += offset.y;
        rotation.z += offset.z;
    }

    /**
     * Get the view matrix.
     *
     * @return The view matrix
     */
    public Matrix4f getViewMatrix() {
        return viewMatrix;
    }

    /**
     * Update the view matrix.
     * <p>This is called in the render method {@link org.kakara.engine.render.Renderer}</p>
     *
     * @return The update matrix.
     */
    public Matrix4f updateViewMatrix() {
        return Transformation.updateGenericViewMatrix(position.toJoml(), rotation.toJoml(), viewMatrix);
    }

    /**
     * Get the rotation of the camera
     * <p>A clone of the vector is returned</p>
     *
     * @return The rotation.
     */
    public Vector3 getRotation() {
        return rotation.clone();
    }

    /**
     * Set the rotation of the camera.
     * <p>Eular rotations are used</p>
     *
     * @param rotation The vector rotation.
     */
    public void setRotation(Vector3 rotation) {
        this.rotation.x = rotation.x;
        this.rotation.y = rotation.y;
        this.rotation.z = rotation.z;
    }
}
