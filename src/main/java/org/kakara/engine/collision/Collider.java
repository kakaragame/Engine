package org.kakara.engine.collision;

import org.kakara.engine.math.Vector3;

public interface Collider {

    /**
     * Every frame update.
     */
    void update();

    /**
     * When the collider is added to a game item.
     *
     * @param item
     */
    void onRegister(Collidable item);

    /**
     * Set if the collider is a trigger.
     * <p>A trigger means the other objects can collide with it but the trigger will never collide with anything.</p>
     * <p>TL;DR use this if the object will never move to save resources.</p>
     *
     * @param value If it is a trigger.
     * @return The collider.
     */
    Collider setTrigger(boolean value);

    /**
     * If the collider is a trigger.
     *
     * @return If the collider is a trigger.
     */
    boolean isTrigger();

    /**
     * Set gravity velocity
     * <p>Will be changed to acceleration in next update.</p>
     *
     * @param value The velocity
     */
    void setGravity(float value);

    /**
     * Get the acceleration for gravity.
     * <p>Will be acceleration in next update.</p>
     *
     * @return The acceleration.
     */
    float getGravity();

    /**
     * Get the velocity for gravity at a given time.
     *
     * @return The velocity.
     */
    float getGravityVelocity();

    /**
     * If gravity is enabled on the object.
     *
     * @return If gravity is enabled.
     */
    boolean usesGravity();

    /**
     * Set if gravity is enabled.
     *
     * @param value If gravity is enabled.
     * @return The collider.
     */
    Collider setUseGravity(boolean value);

    /**
     * Get the first relative point for a collider.
     * <p>This point is relative to the (0,0,0) point of the object. (Usually the top corner)</p>
     *
     * @return The Vector of the point.
     */
    Vector3 getRelativePoint1();

    /**
     * Get the first absolute point for a collider.
     * <p>Returns the point according to world space. Ex: (20, 54, 3)</p>
     *
     * @return The Vector of the point.
     */
    Vector3 getAbsolutePoint1();

    /**
     * Get the second relative point for a collider.
     * <p>This point is relative to the (0,0,0) point of the object. (Usually the top corner)</p>
     *
     * @return The Vector of the point.
     */
    Vector3 getRelativePoint2();

    /**
     * Get the second absolute point for a collider.
     * <p>Returns the point according to world space. Ex: (20, 54, 3)</p>
     *
     * @return The Vector of the point.
     */
    Vector3 getAbsolutePoint2();

}
