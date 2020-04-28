package org.kakara.engine.renderobjects.renderlayouts.types;

/**
 * The texture coords for the render layout
 * See the source code at {@link org.kakara.engine.renderobjects.renderlayouts.BlockLayout} for examples.
 */
public interface Texture {
    float[] getFront(float xOffset, float yOffset, int rows);
    float[] getBack(float xOffset, float yOffset, int rows);
    float[] getTop(float xOffset, float yOffset, int rows);
    float[] getBottom(float xOffset, float yOffset, int rows);
    float[] getRight(float xOffset, float yOffset, int rows);
    float[] getLeft(float xOffset, float yOffset, int rows);
}
