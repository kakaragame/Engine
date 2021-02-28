package org.kakara.engine.gameitems.features;

import org.kakara.engine.gameitems.GameItem;

/**
 * Features are a way to control game items every update.
 * <p>Features can be applied to more than one GameItem.</p>
 *
 * <p>As of Pre-5 {@link org.kakara.engine.components.Component} are recommended instead of Features.</p>
 *
 * @since 1.0-Pre2
 * @deprecated Replaced by the Component System. To be removed in Pre6
 */
@Deprecated
public interface Feature {
    /**
     * The code to run each update.
     *
     * @param gameItem The gameItem that is being updated.
     */
    void update(GameItem gameItem);

    /**
     * TBD
     *
     * @param gameItem The gameItem that is being updated.
     */
    default void updateValues(GameItem gameItem) {
    }
}
