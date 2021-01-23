package org.kakara.engine.components;

import org.joml.Quaternionf;
import org.kakara.engine.Camera;
import org.kakara.engine.math.Vector3;

/**
 * The Transform component stores the position, rotation, and scale of the GameItem.
 *
 * <p>This component is automatically added to all GameItems and is required.</p>
 */
public class Transform extends Component{
    private final Vector3 position = new Vector3();
    private final Quaternionf rotation = new Quaternionf();
    private float scale = 1f;

    @Override
    public void start() {}

    @Override
    public void update() {}

    public void setPosition(float x, float y, float z){
        this.position.set(x, y, z);
    }

    public void setPosition(Vector3 position){
        this.position.set(position);
    }

    public Vector3 getPosition(){
        return position;
    }

    public void setRotation(Quaternionf rotation){
        this.rotation.set(rotation);
    }

    public Quaternionf getRotation(){
        return rotation;
    }

    public void rotateAboutAxis(float angle, Vector3 axis) {
        this.rotation.rotateAxis(angle, axis.toJoml());
    }

    public void setRotationAboutAxis(float angle, Vector3 axis) {
        this.rotation.rotationAxis(angle, axis.toJoml());
    }

    public void setScale(float scale){
        this.scale = scale;
    }

    public float getScale(){
        return scale;
    }

    public void translateBy(float x, float y, float z){
        this.position.addMut(x, y, z);
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

    /**
     * Move the rotation by an offset
     * <p>For use by particles only</p>
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
     * Move the rotation by an offset
     * <p>For use by particles only</p>
     *
     * @param offset The offset vector.
     */
    public void moveRotation(Vector3 offset) {
        rotation.x += offset.x;
        rotation.y += offset.y;
        rotation.z += offset.z;
    }
}
