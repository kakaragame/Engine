package org.kakara.engine.voxels.layouts.types;

import java.util.List;

/**
 * The texture coords for the render layout
 * See the source code at {@link org.kakara.engine.voxels.layouts.BlockLayout} for examples.
 */
public interface Texture {
    List<Float> getFront(float xOffset, float yOffset, int rows);

    List<Float> getBack(float xOffset, float yOffset, int rows);

    List<Float> getTop(float xOffset, float yOffset, int rows);

    List<Float> getBottom(float xOffset, float yOffset, int rows);

    List<Float> getRight(float xOffset, float yOffset, int rows);

    List<Float> getLeft(float xOffset, float yOffset, int rows);
}
