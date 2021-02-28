package org.kakara.engine.voxels.layouts.types;

import java.util.List;

/**
 * The values for the normals of a blockface.
 * See the source code at {@link org.kakara.engine.voxels.layouts.BlockLayout} for examples.
 */
public interface Normal {
    List<Float> getFront();

    List<Float> getBack();

    List<Float> getTop();

    List<Float> getBottom();

    List<Float> getRight();

    List<Float> getLeft();
}
