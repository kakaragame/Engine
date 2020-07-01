package org.kakara.engine.renderobjects.mesh;

import org.kakara.engine.GameEngine;
import org.kakara.engine.exceptions.InvalidThreadException;
import org.kakara.engine.renderobjects.RenderBlock;
import org.kakara.engine.renderobjects.RenderChunk;
import org.kakara.engine.renderobjects.TextureAtlas;
import org.kakara.engine.renderobjects.renderlayouts.BasicMeshLayout;
import org.kakara.engine.renderobjects.renderlayouts.MeshLayout;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

/**
 * The mesh for the chunks.
 * @since 1.0-Pre2
 */
public class SyncMesh implements RenderMesh {

    protected int vaoId;
    protected final List<Integer> vboIdList;
    private int vertexCount;

    /**
     * Create a render mesh
     *
     * @param blocks       The list of render blocks
     * @param renderChunk  renderchunk
     * @param textureAtlas The texture atlas to use
     */
    public SyncMesh(List<RenderBlock> blocks, RenderChunk renderChunk, TextureAtlas textureAtlas) {
        if(Thread.currentThread() != GameEngine.currentThread)
            throw new InvalidThreadException("This class must be constructed on the main tread!");
        vboIdList = new ArrayList<>();
        vaoId = glGenVertexArrays();
        List<RenderBlock> renderBlocks = renderChunk.calculateVisibleBlocks(blocks);
        MeshLayout layout = setupLayout(renderBlocks, textureAtlas);
        try {
            vertexCount = layout.getVertexLength();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, layout.getVertex(), GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture Coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, layout.getTextCoords(), GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            //Vertex Normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, layout.getNormals(), GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            // Indices VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
            glBufferData(GL_ELEMENT_ARRAY_BUFFER, layout.getIndices(), GL_STATIC_DRAW);

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);


        } finally {
            if (layout.getVertex() != null)
                MemoryUtil.memFree(layout.getVertex());
            if (layout.getTextCoords() != null)
                MemoryUtil.memFree(layout.getTextCoords());
            if (layout.getNormals() != null)
                MemoryUtil.memFree(layout.getNormals());
            if (layout.getIndices() != null)
                MemoryUtil.memFree(layout.getIndices());
        }

    }

    /**
     * Get the vertex count
     *
     * @return The vertex count
     */
    public int getVertexCount() {
        return vertexCount;
    }

    private void initRender() {
        glBindVertexArray(this.vaoId);
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
    }

    /**
     * Render the chunk
     * <p>Internal Use Only</p>
     */
    public void render() {
        initRender();
        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        closeRender();
    }

    private void closeRender() {
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glBindVertexArray(0);
    }

    /**
     * Cleanup the chunk.
     * <p>Internal Use Only</p>
     */
    public void cleanUp() {
        glDisableVertexAttribArray(0);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    /**
     * Combine all of the meshes
     *
     * @param renderBlocks The blocks to be rendered.
     * @return The layout.
     */
    private MeshLayout setupLayout(List<RenderBlock> renderBlocks, TextureAtlas textureAtlas) {
        List<Float> positions = new LinkedList<>();
        List<Float> texCoords = new LinkedList<>();
        List<Float> normals = new LinkedList<>();
        List<Integer> indicies = new LinkedList<>();
        int count = 0;
        for (RenderBlock rb : renderBlocks) {
            rb.getVertexFromFaces(positions);
            rb.getTextureFromFaces(texCoords, textureAtlas);
            rb.getNormalsFromFaces(normals);
            rb.getIndicesFromFaces(indicies, count);
            count += rb.getVisibleFaces().size() * 4;
        }

        final FloatBuffer posBuffer;
        final FloatBuffer texCoordsBuffer;
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

        vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.size());
        for (Float f : normals)
            vecNormalsBuffer.put(f);
        vecNormalsBuffer.flip();

        indicesBuffer = MemoryUtil.memAllocInt(indicies.size());
        for (Integer f : indicies)
            indicesBuffer.put(f);
        indicesBuffer.flip();
        return new BasicMeshLayout(indicies.size(), posBuffer, texCoordsBuffer, vecNormalsBuffer, indicesBuffer);
    }

}