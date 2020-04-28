package org.kakara.engine.renderobjects.renderlayouts.types;

/**
 * Get the vertex positions
 * See the source code at {@link org.kakara.engine.renderobjects.renderlayouts.BlockLayout} for examples.
 */
public interface Vertex {
    float[] getFront();
    float[] getBack();
    float[] getTop();
    float[] getBottom();
    float[] getRight();
    float[] getLeft();
}
