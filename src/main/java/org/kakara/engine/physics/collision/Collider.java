package org.kakara.engine.physics.collision;

import org.kakara.engine.item.GameItem;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.physics.OnTriggerEnter;
import org.kakara.engine.utils.Pair;

import java.util.function.Predicate;

public interface Collider {

    /**
     * This will fire the colliding events.
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
     * <p>A trigger means that other objects will not collide with it but it will still call {@link OnTriggerEnter}.</p>
     * <p>As of 1.0-Pre2 the old functionality has been replaced by {@link #setResolvable(boolean)}</p>
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
     * If the engine it to attempt and move this object to fix collisions.
     * <p>As of 1.0-Pre2, this replaces the old functionality of {@link #setTrigger(boolean)}</p>
     *
     * @param value If the engine is to move this object to fix collisions.
     */
    void setResolvable(boolean value);

    /**
     * Get if the engine should attempt to move this object to fix collisions.
     *
     * @return If the engine should attempt to move this object to fix collisions.
     */
    boolean isResolvable();

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

    void updateX();

    void updateY();

    void updateZ();

    /**
     * Add an event to be triggered when this collidable comes in contact with a trigger.
     * <p>Important note: This triggers every physics update that the object is colliding with a trigger.</p>
     *
     * @param enter The event to be triggered.
     */
    void addOnTriggerEnter(OnTriggerEnter enter);

    void setPredicate(Predicate<GameItem> gameItemPredicate);

    Predicate<GameItem> getPredicate();
}
