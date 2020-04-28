package org.kakara.engine.renderobjects.renderlayouts.types;

/**
 * The indices values are stored here. The starting index is the index that is starts at.
 * So each one would be 0 - 6 for example. See the source code for {@link org.kakara.engine.renderobjects.renderlayouts.BlockLayout}
 */
public interface Indices {
    int[] getFront(int startingIndex);
    int[] getBack(int startingIndex);
    int[] getTop(int startingIndex);
    int[] getBottom(int startingIndex);
    int[] getRight(int startingIndex);
    int[] getLeft(int startingIndex);
}
