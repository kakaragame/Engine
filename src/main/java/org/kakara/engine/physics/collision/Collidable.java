package org.kakara.engine.physics.collision;

import org.kakara.engine.math.Vector3;

/**
 * Handles collision for the entire game.
 * Implemented by RenderBlock and MeshGameItem
 */
public interface Collidable {
    /**
     * Get the position of the collidable
     * @return The position
     */
    Vector3 getColPosition();

    /**
     * Get the scale of the collidable
     * @return The scale
     */
    float getColScale();

    /**
     * Set the collider
     * @param collider The collider
     */
    void setCollider(Collider collider);

    /**
     * Remove the collider
     */
    void removeCollider();

    /**
     * Translate the object by a value.
     * @param vec Translate the object
     */
    void colTranslateBy(Vector3 vec);

    /**
     * Set the position of the object
     * @param vec The position vector.
     */
    void setColPosition(Vector3 vec);

    /**
     * Get the collider associated with the object
     * @return The collider. (Null if there is none).
     */
    Collider getCollider();

    /**
     * If the collidable is selected.
     * @return If the collidable is selected.
     */
    boolean isSelected();

    /**
     * Set if the collidable is selected.
     * @param selected If the collidable is selected.
     */
    void setSelected(boolean selected);
}
