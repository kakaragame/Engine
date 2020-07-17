package org.kakara.engine.renderobjects.mesh;

import org.kakara.engine.renderobjects.RenderBlock;
import org.kakara.engine.renderobjects.TextureAtlas;

import java.util.List;

public interface RenderMesh {
    int getVertexCount();
    void render();
    void cleanUp();
    void updateOverlay(List<RenderBlock> blocks, TextureAtlas textureAtlas);
}
