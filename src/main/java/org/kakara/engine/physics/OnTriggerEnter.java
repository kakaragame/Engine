package org.kakara.engine.physics;

import org.kakara.engine.physics.collision.Collidable;

/**
 * This handle the OnTriggerEnter event for {@link org.kakara.engine.physics.collision.Collider}.
 * @since 1.0-Pre2
 */
public interface OnTriggerEnter {
    /**
     * This is triggered when something enters a collision.
     * @param other The collidable that is collided with.
     */
    void onTriggerEnter(Collidable other);
}
