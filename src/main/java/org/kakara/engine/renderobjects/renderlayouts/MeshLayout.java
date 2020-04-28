package org.kakara.engine.renderobjects.renderlayouts;

/**
 * This layout is used to store the processed layout information.
 * <p>This is only used Internally.</p>
 */
public interface MeshLayout {
    float[] getVertex();
    float[] getTextCoords();
    float[] getNormals();
    int[] getIndices();
}
