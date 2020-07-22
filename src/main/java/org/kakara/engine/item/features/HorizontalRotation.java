package org.kakara.engine.item.features;

import org.joml.Quaternionf;
import org.kakara.engine.item.GameItem;
import org.kakara.engine.renderobjects.RenderChunk;

public class HorizontalRotation implements Feature {
    @Override
    public void update(GameItem gameItem) {
        gameItem.getRotation().rotateY(0.01f);
    }
}
