package org.kakara.engine.gameitems.features;

import org.kakara.engine.gameitems.GameItem;

/**
 * Features are a way to control game items every update.
 * <p>Features can be applied to more than one GameItem.</p>
 * @since 1.0-Pre2
 */
public interface Feature {
    /**
     * The code to run each update.
     * @param gameItem The gameItem that is being updated.
     */
    void update(GameItem gameItem);

    /**
     * TBD
     * @param gameItem The gameItem that is being updated.
     */
    default void updateValues(GameItem gameItem) {}
}
