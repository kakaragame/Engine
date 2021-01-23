package org.kakara.engine.physics;

import org.kakara.engine.physics.collision.Collidable;
import org.kakara.engine.physics.collision.ColliderComponent;

/**
 * This handle the OnTriggerEnter event for {@link ColliderComponent}.
 *
 * @since 1.0-Pre2
 */
public interface OnTriggerEnter {
    /**
     * This is triggered when something enters a collision.
     *
     * @param other The collidable that is collided with.
     */
    void onTriggerEnter(ColliderComponent other);
}
