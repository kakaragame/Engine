package org.kakara.engine.renderobjects.renderlayouts;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class BasicMeshLayout implements MeshLayout {
    private int vertexLength;
    private FloatBuffer vertex;
    private FloatBuffer textcoords;
    private FloatBuffer overlaycords;
    private IntBuffer hasOverlay;
    private FloatBuffer normals;
    private IntBuffer indices;

    public BasicMeshLayout(int vertexLength, FloatBuffer vertex, FloatBuffer textcoords, FloatBuffer overlaycords, IntBuffer hasOverlay, FloatBuffer normals, IntBuffer indices) {
        this.vertexLength = vertexLength;
        this.vertex = vertex;
        this.textcoords = textcoords;
        this.overlaycords = overlaycords;
        this.hasOverlay = hasOverlay;
        this.normals = normals;
        this.indices = indices;
    }

    @Override
    public FloatBuffer getVertex() {
        return vertex;
    }

    @Override
    public FloatBuffer getTextCoords() {
        return textcoords;
    }

    @Override
    public FloatBuffer getOverlayCoords() {
        return overlaycords;
    }

    @Override
    public IntBuffer getHasOverlay() {
        return hasOverlay;
    }

    @Override
    public FloatBuffer getNormals() {
        return normals;
    }

    @Override
    public IntBuffer getIndices() {
        return indices;
    }

    @Override
    public int getVertexLength() {
        return vertexLength;
    }
}
