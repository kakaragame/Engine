package org.kakara.engine.item.features;

import org.kakara.engine.item.GameItem;

public interface Feature {
    void update(GameItem gameItem);

    default void updateValues(GameItem gameItem) {

    }
}
