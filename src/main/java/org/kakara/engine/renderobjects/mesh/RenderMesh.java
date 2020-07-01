package org.kakara.engine.renderobjects.mesh;

public interface RenderMesh {
    int getVertexCount();
    void render();
    void cleanUp();
}
