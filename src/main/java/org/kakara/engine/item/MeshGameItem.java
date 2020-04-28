package org.kakara.engine.item;

import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.kakara.engine.GameHandler;
import org.kakara.engine.collision.Collidable;
import org.kakara.engine.collision.Collider;
import org.kakara.engine.math.Vector3;

import java.util.UUID;

/**
 * The most basic implementation of the GameItem.
 * <p>
 * This is a Collidable GameItem. That uses meshes to create an item
 */
public class MeshGameItem implements GameItem, Collidable {

    private Mesh[] meshes;
    private float scale;
    private Quaternionf rotation;
    private Vector3 position;
    private final UUID uuid;
    private Collider collider;
    private int textPos;

    private boolean selected;

    public MeshGameItem() {
        this(new Mesh[0]);
    }

    public MeshGameItem(Mesh mesh) {
        this(new Mesh[]{mesh});
    }

    public MeshGameItem(Mesh[] meshes) {
        this.meshes = meshes;
        position = new Vector3(0, 0, 0);
        scale = 1;
        rotation = new Quaternionf();
        uuid = UUID.randomUUID();
        textPos = 0;
    }

    /**
     * Get the current position.
     *
     * @return The current position.
     */
    public Vector3 getPosition() {
        return position;
    }


    /**
     * Set the position of the game item.
     *
     * @param x x value
     * @param y y value
     * @param z z value
     * @return The instance of the game item.
     */
    public GameItem setPosition(float x, float y, float z) {
        this.position.x = x;
        this.position.y = y;
        this.position.z = z;
        return this;
    }

    /**
     * Set the position of the item
     *
     * @param position The position in vector form
     * @return The instance of the Game Item.
     */
    public GameItem setPosition(Vector3 position) {
        this.position = position;
        return this;
    }

    /**
     * Change the position of the game item by x, y, and z values.
     *
     * @param x Change in x
     * @param y Change in y
     * @param z Change in z
     * @return The instance of the game item.
     */
    public GameItem translateBy(float x, float y, float z) {
        this.position.x += x;
        this.position.y += y;
        this.position.z += z;
        return this;
    }

    /**
     * Change the position of the game item by a vector.
     *
     * @param position The vector to change by.
     * @return The instance of the game item.
     */
    public GameItem translateBy(Vector3 position) {
        this.position.x += position.x;
        this.position.y += position.y;
        this.position.z += position.z;
        return this;
    }

    /**
     * Get the scale of the item.
     *
     * @return The scale
     */
    public float getScale() {
        return scale;
    }

    /**
     * Set the scale of the Game Item
     *
     * @param scale The scale value
     * @return The instance of the game item.
     */
    public final GameItem setScale(float scale) {
        this.scale = scale;
        return this;
    }

    /**
     * Get the ID of the game item.
     * <p>This method is the same as getUUID()</p>
     *
     * @return The ID.
     */
    public UUID getId() {
        return uuid;
    }

    /**
     * Get the rotation of the game item
     * @return The rotation
     */
    public Quaternionf getRotation() {
        return rotation;
    }

    /**
     * Set the rotation of the Object
     *
     * @param q The quaternion
     * @return The instance of the game item.
     */
    public final GameItem setRotation(Quaternionf q) {
        this.rotation.set(q);
        return this;
    }

    /**
     * Change the rotation by the angle on the axis.
     *
     * @param angle The angle to change by. (In radians)
     * @param axis  The vector of the axis (without magnitude)
     * @return The instance of the game item.
     */
    public GameItem rotateAboutAxis(float angle, Vector3 axis) {
        this.rotation.rotateAxis(angle, axis.toJoml());
        return this;
    }

    /**
     * Set the rotation to the angle on the axis.
     *
     * @param angle The angle to set to. (In Radians).
     * @param axis  The vector of the axis (without magnitude)
     * @return The instance of the game item.
     */
    public GameItem setRotationAboutAxis(float angle, Vector3 axis) {
        this.rotation.rotationAxis(angle, axis.toJoml());
        return this;
    }

    /**
     * Get the mesh of the object
     * <p>If there is more than one mesh, than the first one is returned.</p>
     * @return The mesh
     */
    public Mesh getMesh() {
        return meshes[0];
    }

    @Override
    public int getTextPos() {
        return this.textPos;
    }

    @Override
    public void setTextPos(int pos) {
        this.textPos = pos;
    }

    /**
     * Get all of the meshes of the object.
     * @return The array of meshes.
     */
    public Mesh[] getMeshes() {
        return meshes;
    }

    /**
     * Set the array of meshes.
     * @param meshes The array of meshes
     */
    public void setMeshes(@NotNull Mesh[] meshes) {
        this.meshes = meshes;
    }

    public void setMesh(@NotNull Mesh mesh) {
        this.meshes = new Mesh[]{mesh};
    }

    @Override
    public final Vector3 getColPosition() {
        return getPosition();
    }

    @Override
    public float getColScale() {
        return getScale();
    }

    /**
     * Set the collider for a game item
     *
     * @param collider The instance of the collider.
     * @return The instance of the game item.
     */
    public void setCollider(Collider collider) {
        this.collider = collider;
        collider.onRegister(this);
        GameHandler.getInstance().getCollisionManager().addCollidingItem(this);
    }

    /**
     * Remove the currently active collider.
     */
    public void removeCollider() {
        this.collider = null;
        GameHandler.getInstance().getCollisionManager().removeCollidingItem(this);
    }

    @Override
    public void colTranslateBy(Vector3 vec) {
        translateBy(vec);
    }

    @Override
    public void setColPosition(Vector3 vec) {
        setPosition(vec.clone());
    }

    /**
     * Get the currently active collider
     *
     * @return The collider. (Null if none applied)
     */
    public Collider getCollider() {
        return this.collider;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    /**
     * Render the item.
     * <p>Internal Use Only</p>
     */
    public void render() {
        for (Mesh mesh : meshes) {
            mesh.render();
        }
    }

    /**
     * Cleanup the item.
     * <p>Internal Use Only</p>
     */
    public void cleanup() {
        int numMeshes = this.meshes != null ? this.meshes.length : 0;
        for (int i = 0; i < numMeshes; i++) {
            this.meshes[i].cleanUp();
        }
    }


    /**
     * A safe way to clone a gameobject.
     *
     * @param exact If you want it to be an exact copy.
     * @return The clone of the gameobject.
     */
    public GameItem clone(boolean exact) {
        GameItem clone = new MeshGameItem(this.meshes);
        if (exact) {
            clone.setPosition(this.position.x, this.position.y, this.position.z);
            clone.setRotation(this.rotation);
            clone.setScale(this.scale);
        }
        return clone;
    }

    /**
     * Move the position by an offset.
     * <p>For use by particle only</p>
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
     * Move the position by an offset.
     * <p>For use by particles only!</p>
     * @param offset The offset vector
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
     * Move the rotation by an offset
     * <p>For use by particles only</p>
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
     * @param offset The offset vector.
     */
    public void moveRotation(Vector3 offset) {
        rotation.x += offset.x;
        rotation.y += offset.y;
        rotation.z += offset.z;
    }
}
