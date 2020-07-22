package org.kakara.engine.item.features;

import org.joml.Quaternionf;
import org.kakara.engine.item.GameItem;

public class HorizontalRotation implements Feature {
    @Override
    public void update(GameItem gameItem) {
        Quaternionf rotation = gameItem.getRotation().rotateX(2);
        gameItem.setRotation(rotation);
    }
}
