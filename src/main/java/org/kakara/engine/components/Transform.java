package org.kakara.engine.components;

import org.joml.Quaternionf;
import org.kakara.engine.Camera;
import org.kakara.engine.math.Vector3;

/**
 * The Transform component stores the position, rotation, and scale of the GameItem.
 *
 * <p>This component is required by all GameItems.</p>
 *
 * <p>Position and rotation default to (0, 0, 0) while scale
 * defaults to (1, 1, 1).</p>
 *
 * <code>
 * gameItem.transform.setPosition(0, 5, 0);<br>
 * gameItem.transform.getRotation().rotateX((float) Math.toRadians(10));<br>
 * </code>
 */
public class Transform extends Component {
    private final Vector3 position = new Vector3();
    private final Quaternionf rotation = new Quaternionf();
    private final Vector3 scale = new Vector3(1, 1, 1);

    @Override
    public void start() {
    }

    @Override
    public void update() {
    }

    /**
     * Set the position.
     *
     * @param x The x position.
     * @param y The y position.
     * @param z The z position.
     */
    public void setPosition(float x, float y, float z) {
        this.position.set(x, y, z);
    }

    /**
     * Set the position.
     *
     * @param position The position vector.
     */
    public void setPosition(Vector3 position) {
        this.position.set(position);
    }

    /**
     * Get the position vector.
     * <p>This is not a copy and is mutable.</p>
     *
     * @return The position vector.
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Set the rotation.
     *
     * @param rotation The Quaternion to set the rotation to.
     */
    public void setRotation(Quaternionf rotation) {
        this.rotation.set(rotation);
    }

    /**
     * Get the rotation.
     *
     * @return The rotation
     */
    public Quaternionf getRotation() {
        return rotation;
    }

    /**
     * Rotate about an axis.
     *
     * @param angle The angle.
     * @param axis  The axis.
     */
    public void rotateAboutAxis(float angle, Vector3 axis) {
        this.rotation.rotateAxis(angle, axis.toJoml());
    }

    /**
     * Set the rotation about an axis.
     *
     * @param angle The angle.
     * @param axis  The axis.
     */
    public void setRotationAboutAxis(float angle, Vector3 axis) {
        this.rotation.rotationAxis(angle, axis.toJoml());
    }

    /**
     * Set the scale.
     *
     * @param xyz The scale for x, y, and z.
     */
    public void setScale(float xyz) {
        scale.x = xyz;
        scale.y = xyz;
        scale.z = xyz;
    }

    /**
     * Set the scale.
     *
     * @param x The x scaling.
     * @param y The y scaling.
     * @param z The z scaling.
     */
    public void setScale(float x, float y, float z) {
        scale.x = x;
        scale.y = y;
        scale.z = z;
    }

    /**
     * Set the scale.
     *
     * @param scale The scale to set.
     */
    public void setScale(Vector3 scale) {
        this.scale.set(scale);
    }

    /**
     * Get the scale.
     *
     * @return The scale.
     */
    public Vector3 getScale() {
        return scale;
    }

    /**
     * Translate the position by a value.
     *
     * @param x The x value.
     * @param y The y value.
     * @param z The z value.
     */
    public void translateBy(float x, float y, float z) {
        this.position.addMut(x, y, z);
    }

    /**
     * Translate the position by a vector.
     *
     * @param vector The vector to translate by.
     */
    public void translateBy(Vector3 vector) {
        this.position.addMut(vector);
    }

    /**
     * Move the position by an offset.
     * <p>This is used when you want to move in the direction that the camera is facing.</p>
     *
     * @param offsetX x offset
     * @param offsetY y offset
     * @param offsetZ z offset
     * @param camera  The camera that it is to move by.
     */
    public void movePositionByCamera(float offsetX, float offsetY, float offsetZ, Camera camera) {
        if (offsetZ != 0) {
            position.x += (float) Math.sin(Math.toRadians(camera.getRotation().y)) * -1.0f * offsetZ;
            position.z += (float) Math.cos(Math.toRadians(camera.getRotation().y)) * offsetZ;
        }
        if (offsetX != 0) {
            position.x += (float) Math.sin(Math.toRadians(camera.getRotation().y - 90)) * -1.0f * offsetX;
            position.z += (float) Math.cos(Math.toRadians(camera.getRotation().y - 90)) * offsetX;
        }
        position.y += offsetY;
    }

    /**
     * Move the position by an offset.
     * <p>This is used when you want to move in the direction that the camera is facing.</p>
     *
     * @param offset The offset vector
     * @param camera The camera it is to move by.
     */
    public void movePositionByCamera(Vector3 offset, Camera camera) {
        if (offset.z != 0) {
            position.x += (float) Math.sin(Math.toRadians(camera.getRotation().y)) * -1.0f * offset.z;
            position.z += (float) Math.cos(Math.toRadians(camera.getRotation().y)) * offset.z;
        }
        if (offset.x != 0) {
            position.x += (float) Math.sin(Math.toRadians(camera.getRotation().y - 90)) * -1.0f * offset.x;
            position.z += (float) Math.cos(Math.toRadians(camera.getRotation().y - 90)) * offset.x;
        }
        position.y += offset.y;
    }

    @Override
    public String toString() {
        return "Transform{" +
                "position=" + position.toString() +
                ", rotation=" + rotation.toString() +
                ", scale=" + scale.toString() +
                '}';
    }
}
