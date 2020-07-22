package org.kakara.engine.item.features;

import org.kakara.engine.item.GameItem;

@FunctionalInterface
public interface Feature {
    void update(GameItem gameItem);
}
