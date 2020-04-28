package org.kakara.engine.renderobjects.renderlayouts.types;

/**
 * The values for the normals of a blockface.
 * See the source code at {@link org.kakara.engine.renderobjects.renderlayouts.BlockLayout} for examples.
 */
public interface Normal {
    float[] getFront();
    float[] getBack();
    float[] getTop();
    float[] getBottom();
    float[] getRight();
    float[] getLeft();
}
