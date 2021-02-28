package org.kakara.engine.physics.collision;

import org.kakara.engine.GameHandler;
import org.kakara.engine.components.Component;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.physics.OnTriggerEnter;

import java.util.Objects;
import java.util.function.Predicate;

/**
 * This is the parent components for colliders.
 *
 * <p>This class can be used to get any collider from a GameItem.</p>
 * <code>
 * ColliderComponent cc = gameItem.getComponent(ColliderComponent.class);
 * </code>
 */
public abstract class ColliderComponent extends Component {

    @Override
    public void afterInit() {
        Objects.requireNonNull(GameHandler.getInstance().getCurrentScene().getCollisionManager())
                .addCollidingItem(this);
    }

    @Override
    public void onRemove() {
        Objects.requireNonNull(GameHandler.getInstance().getCurrentScene().getCollisionManager())
                .removeCollidingItem(this);
    }

    /**
     * Get the position for the collider.
     * <p>For most colliders this is the same as gameItem.transform. (For an exception see
     * {@link VoxelCollider})</p>
     *
     * @return The position of the collider.
     */
    public Vector3 getPosition() {
        return getGameItem().transform.getPosition();
    }

    /**
     * Get the scale for the collider.
     * <p>For more colliders this is the same as gameItem.transfrom. (For an exception see
     * {@link VoxelCollider}</p>
     *
     * @return The scale of the collider.
     */
    public Vector3 getScale() {
        return getGameItem().transform.getScale();
    }

    /**
     * If the collider is a trigger.
     *
     * <p>A trigger means that the object will not trigger {@link PhysicsComponent} to resolve the collision</p>
     *
     * @return If the collider is a trigger.
     */
    public abstract boolean isTrigger();

    /**
     * Set if the collider is a trigger.
     * <p>A trigger means that other objects will not collide with it but it will still call {@link OnTriggerEnter}.
     * This behavior is implemented in the {@link PhysicsComponent} component.</p>
     *
     * @param value If it is a trigger.
     * @return The collider.
     */
    public abstract ColliderComponent setTrigger(boolean value);

    /**
     * Get the first relative point for a collider.
     * <p>This point is relative to the (0,0,0) point of the object. (Usually the top corner)</p>
     *
     * @return The Vector of the point.
     */
    public abstract Vector3 getRelativePoint1();

    /**
     * Get the first absolute point for a collider.
     * <p>Returns the point according to world space. Ex: (20, 54, 3)</p>
     *
     * @return The Vector of the point.
     */
    public abstract Vector3 getAbsolutePoint1();

    /**
     * Get the second relative point for a collider.
     * <p>This point is relative to the (0,0,0) point of the object. (Usually the top corner)</p>
     *
     * @return The Vector of the point.
     */
    public abstract Vector3 getRelativePoint2();

    /**
     * Get the second absolute point for a collider.
     * <p>Returns the point according to world space. Ex: (20, 54, 3)</p>
     *
     * @return The Vector of the point.
     */
    public abstract Vector3 getAbsolutePoint2();

    /**
     * @deprecated Currently unused
     */
    public abstract void updateX();

    /**
     * Handle the collision resolution for the y axis.
     * <p>This is used by the {@link PhysicsComponent}</p>
     *
     * @since 1.0-Pre2
     */
    public abstract void updateY();

    /**
     * Handle the collision resolution for the x and z axis.
     * <p>This is used by the {@link PhysicsComponent}</p>
     *
     * @since 1.0-Pre2
     */
    public abstract void updateZ();

    /**
     * Add an event to be triggered when this collidable comes in contact with a trigger.
     * <p>Important note: This triggers every physics update that the object is colliding with a trigger.</p>
     *
     * <p>This functionality has been replaced by the {@link Component#onCollision(ColliderComponent)}.
     * Consider using that instead.</p>
     *
     * @param enter The event to be triggered.
     * @since 1.0-Pre2
     */
    public abstract void addOnTriggerEnter(OnTriggerEnter enter);

    /**
     * Get this collider's predicate.
     *
     * @return The predicate.
     * @since 1.0-Pre2
     */
    public abstract Predicate<ColliderComponent> getPredicate();

    /**
     * Add a condition for collision.
     * <p>This can be used to prevent a collision; for example, if a gameitem has a certain tag.</p>
     *
     * @param gameItemPredicate The predicate to use.
     * @since 1.0-Pre2
     */
    public abstract void setPredicate(Predicate<ColliderComponent> gameItemPredicate);
}
