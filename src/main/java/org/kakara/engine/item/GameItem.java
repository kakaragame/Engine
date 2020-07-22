package org.kakara.engine.item;

import org.joml.Quaternionf;
import org.kakara.engine.item.features.Feature;
import org.kakara.engine.item.mesh.IMesh;
import org.kakara.engine.item.mesh.Mesh;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.physics.PhysicsItem;
import org.kakara.engine.physics.collision.Collidable;

import java.util.List;
import java.util.UUID;

/**
 * The main game item interface.
 */
public interface GameItem extends Tagable, Collidable, PhysicsItem {

    /**
     * Get the current position.
     *
     * @return The current position.
     */
    Vector3 getPosition();


    /**
     * Set the position of the game item.
     *
     * @param x x value
     * @param y y value
     * @param z z value
     * @return The instance of the game item.
     */
    GameItem setPosition(float x, float y, float z);

    /**
     * Set the position of the item
     *
     * @param position The position in vector form
     * @return The instance of the Game Item.
     */
    GameItem setPosition(Vector3 position);

    /**
     * Change the position of the game item by x, y, and z values.
     *
     * @param x Change in x
     * @param y Change in y
     * @param z Change in z
     * @return The instance of the game item.
     */
    GameItem translateBy(float x, float y, float z);

    /**
     * Change the position of the game item by a vector.
     *
     * @param position The vector to change by.
     * @return The instance of the game item.
     */
    GameItem translateBy(Vector3 position);

    /**
     * Get the scale of the item.
     *
     * @return The scale
     */
    float getScale();

    /**
     * Set the scale of the Game Item
     *
     * @param scale The scale value
     * @return The instance of the game item.
     */
    GameItem setScale(float scale);

    /**
     * Get the ID of the game item.
     * <p>This method is the same as getUUID()</p>
     *
     * @return The ID.
     */
    UUID getId();

    Quaternionf getRotation();

    /**
     * Set the rotation of the Object
     *
     * @param q The quaternion
     * @return The instance of the game item.
     */
    GameItem setRotation(Quaternionf q);

    /**
     * Change the rotation by the angle on the axis.
     *
     * @param angle The angle to change by. (In radians)
     * @param axis  The vector of the axis (without magnitude)
     * @return The instance of the game item.
     */
    GameItem rotateAboutAxis(float angle, Vector3 axis);

    /**
     * Set the rotation to the angle on the axis.
     *
     * @param angle The angle to set to. (In Radians).
     * @param axis  The vector of the axis (without magnitude)
     * @return The instance of the game item.
     */
    GameItem setRotationAboutAxis(float angle, Vector3 axis);


    void render();

    void cleanup();


    /**
     * A safe way to clone a gameobject.
     *
     * @param exact If you want it to be an exact copy.
     * @return The clone of the gameobject.
     */
    GameItem clone(boolean exact);

    /**
     * Get the mesh of the game item.
     *
     * @return The mesh
     */
    IMesh getMesh();

    /**
     * Get the texture position
     * <p>Mainly used by the particle system</p>
     *
     * @return The texture position
     */
    int getTextPos();

    /**
     * Set the texture position
     * <p>Mainly used by the particle system</p>
     *
     * @param pos The position
     */
    void setTextPos(int pos);

    List<Feature> getFeatures();

    void addFeature(Feature feature);
}
