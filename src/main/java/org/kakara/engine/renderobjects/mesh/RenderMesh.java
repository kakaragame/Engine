package org.kakara.engine.renderobjects.mesh;

import org.kakara.engine.renderobjects.RenderBlock;
import org.kakara.engine.renderobjects.TextureAtlas;

import java.util.List;

/**
 * The general interface for the different types of render meshes.
 * @since 1.0-Pre2
 */
public interface RenderMesh {
    /**
     * The number of vertexes.
     * @return The number of vertexes.
     */
    int getVertexCount();

    /**
     * Render the mesh.
     * <p>Internal Use Only</p>
     */
    void render();

    /**
     * Cleanup the mesh.
     * <p>Internal Use Only</p>
     */
    void cleanUp();

    /**
     * Update the overlay textures.
     * <p>The list of blocks cannot be different from the current mesh.</p>
     * @param blocks The list of blocks. (Much be the same as the current render).
     * @param textureAtlas The texture atlas to use.
     */
    void updateOverlay(List<RenderBlock> blocks, TextureAtlas textureAtlas);
}
