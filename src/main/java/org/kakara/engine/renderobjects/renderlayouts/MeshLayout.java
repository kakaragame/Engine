package org.kakara.engine.renderobjects.renderlayouts;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * This layout is used to store the processed layout information.
 * <p>This is only used Internally.</p>
 */
public interface MeshLayout {
    FloatBuffer getVertex();
    FloatBuffer getTextCoords();
    FloatBuffer getOverlayCoords();
    IntBuffer getHasOverlay();
    FloatBuffer getNormals();
    IntBuffer getIndices();
    int getVertexLength();
}
