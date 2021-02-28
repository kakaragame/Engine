package org.kakara.engine.voxels.layouts.types;

import java.util.List;

/**
 * Get the vertex positions
 * See the source code at {@link org.kakara.engine.voxels.layouts.BlockLayout} for examples.
 */
public interface Vertex {
    List<Float> getFront();

    List<Float> getBack();

    List<Float> getTop();

    List<Float> getBottom();

    List<Float> getRight();

    List<Float> getLeft();
}
