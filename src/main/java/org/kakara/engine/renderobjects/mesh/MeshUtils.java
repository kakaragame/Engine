package org.kakara.engine.renderobjects.mesh;

import org.kakara.engine.renderobjects.RenderBlock;
import org.kakara.engine.renderobjects.TextureAtlas;
import org.kakara.engine.renderobjects.renderlayouts.BasicMeshLayout;
import org.kakara.engine.renderobjects.renderlayouts.MeshLayout;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A utility class that handles methods that all render meshes need.
 *
 * @since 1.0-Pre2
 */
public class MeshUtils {
    /**
     * Combine all of the meshes
     *
     * @param renderBlocks The blocks to be rendered.
     * @param textureAtlas The texture atlas to use.
     * @return The layout.
     */
    protected static MeshLayout setupLayout(List<RenderBlock> renderBlocks, TextureAtlas textureAtlas) {
        List<Float> positions = new ArrayList<>();
        List<Float> texCoords = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indicies = new ArrayList<>();
        List<Float> overlayCoords = new ArrayList<>();
        List<Integer> hasOverlay = new ArrayList<>();
        int count = 0;
        for (RenderBlock rb : renderBlocks) {
            int initial = positions.size() / 3;
            rb.getVertexFromFaces(positions);
            rb.getTextureFromFaces(texCoords, textureAtlas);
            rb.getOverlayFromFaces(overlayCoords, textureAtlas);
            hasOverlay.addAll(Collections.nCopies((positions.size() / 3 - initial), rb.getOverlay() == null ? 0 : 1));
            rb.getNormalsFromFaces(normals);
            rb.getIndicesFromFaces(indicies, count);
            count += rb.getVisibleFaces().size() * 4;
        }

        final FloatBuffer posBuffer;
        final FloatBuffer texCoordsBuffer;
        final FloatBuffer overlayCoordsBuffer;
        final IntBuffer hasOverlayBuffer;
        final FloatBuffer vecNormalsBuffer;
        final IntBuffer indicesBuffer;

        posBuffer = MemoryUtil.memAllocFloat(positions.size());
        for (Float f : positions)
            posBuffer.put(f);
        posBuffer.flip();

        texCoordsBuffer = MemoryUtil.memAllocFloat(texCoords.size());
        for (Float f : texCoords)
            texCoordsBuffer.put(f);
        texCoordsBuffer.flip();

        overlayCoordsBuffer = MemoryUtil.memAllocFloat(overlayCoords.size());
        for (Float f : overlayCoords)
            overlayCoordsBuffer.put(f);
        overlayCoordsBuffer.flip();

        hasOverlayBuffer = MemoryUtil.memAllocInt(hasOverlay.size());
        for (Integer f : hasOverlay)
            hasOverlayBuffer.put(f);
        hasOverlayBuffer.flip();

        vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.size());
        for (Float f : normals)
            vecNormalsBuffer.put(f);
        vecNormalsBuffer.flip();

        indicesBuffer = MemoryUtil.memAllocInt(indicies.size());
        for (Integer f : indicies)
            indicesBuffer.put(f);
        indicesBuffer.flip();
        return new BasicMeshLayout(indicies.size(), posBuffer, texCoordsBuffer, overlayCoordsBuffer, hasOverlayBuffer, vecNormalsBuffer, indicesBuffer);
    }
}
