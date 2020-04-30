package org.kakara.engine.renderobjects.renderlayouts.types;

import java.util.List;

/**
 * Get the vertex positions
 * See the source code at {@link org.kakara.engine.renderobjects.renderlayouts.BlockLayout} for examples.
 */
public interface Vertex {
    List<Float> getFront();
    List<Float> getBack();
    List<Float> getTop();
    List<Float> getBottom();
    List<Float> getRight();
    List<Float> getLeft();
}
