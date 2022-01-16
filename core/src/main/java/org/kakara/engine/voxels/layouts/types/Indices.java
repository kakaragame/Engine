package org.kakara.engine.voxels.layouts.types;

import java.util.List;

/**
 * The indices values are stored here. The starting index is the index that is starts at.
 * So each one would be 0 - 6 for example. See the source code for {@link org.kakara.engine.voxels.layouts.BlockLayout}
 */
public interface Indices {
    List<Integer> getFront(int startingIndex);

    List<Integer> getBack(int startingIndex);

    List<Integer> getTop(int startingIndex);

    List<Integer> getBottom(int startingIndex);

    List<Integer> getRight(int startingIndex);

    List<Integer> getLeft(int startingIndex);
}
