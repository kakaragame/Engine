package org.kakara.engine.renderobjects.mesh;

import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.exceptions.InvalidThreadException;
import org.kakara.engine.render.culling.RenderQuery;
import org.kakara.engine.renderobjects.RenderBlock;
import org.kakara.engine.renderobjects.RenderChunk;
import org.kakara.engine.renderobjects.TextureAtlas;
import org.kakara.engine.renderobjects.renderlayouts.MeshLayout;
import org.lwjgl.system.MemoryUtil;

import java.nio.ByteBuffer;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;
import static org.lwjgl.opengl.GL33.GL_ANY_SAMPLES_PASSED;
import static org.lwjgl.opengl.GL43.GL_ANY_SAMPLES_PASSED_CONSERVATIVE;

/**
 * The mesh for the chunks.
 * @since 1.0-Pre2
 */
public class SyncMesh implements RenderMesh {

    protected int vaoId;
    protected final List<Integer> vboIdList;
    private int vertexCount;

    private RenderQuery query;

    /**
     * Create a render mesh
     * @param blocks       The list of render blocks
     * @param renderChunk  renderchunk
     * @param textureAtlas The texture atlas to use
     */
    public SyncMesh(List<RenderBlock> blocks, RenderChunk renderChunk, TextureAtlas textureAtlas) {
        if(Thread.currentThread() != GameEngine.currentThread)
            throw new InvalidThreadException("This class must be constructed on the main tread!");

        query = new RenderQuery(GL_ANY_SAMPLES_PASSED);

        vboIdList = new ArrayList<>();
        vaoId = glGenVertexArrays();
        List<RenderBlock> renderBlocks = renderChunk.calculateVisibleBlocks(blocks);
        MeshLayout layout = MeshUtils.setupLayout(renderBlocks, textureAtlas);
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

            //Overlay Texture VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, layout.getOverlayCoords(), GL_STATIC_DRAW);
            glVertexAttribPointer(3, 2, GL_FLOAT, false, 0, 0);

            //Has Overlay Texture VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, layout.getHasOverlay(), GL_STATIC_DRAW);
            glVertexAttribPointer(4, 1, GL_INT, false, 0, 0);

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
            if(layout.getOverlayCoords() != null)
                MemoryUtil.memFree(layout.getOverlayCoords());
            if(layout.getHasOverlay() != null)
                MemoryUtil.memFree(layout.getHasOverlay());
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
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(4);
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
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);
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
        query.delete();
    }

    @Override
    public void updateOverlay(List<RenderBlock> blocks, TextureAtlas textureAtlas) {
        List<Float> overlayCoords = new ArrayList<>();
        List<Integer> hasOverlay = new ArrayList<>();

        for (RenderBlock rb : blocks) {
            int initial = overlayCoords.size()/2;
            rb.getOverlayFromFaces(overlayCoords, textureAtlas);
            hasOverlay.addAll(Collections.nCopies((overlayCoords.size()/2 - initial), rb.getOverlay() == null ? 0 : 1));
        }

        FloatBuffer overlayCoordsBuffer = MemoryUtil.memAllocFloat(overlayCoords.size());
        for (Float f : overlayCoords)
            overlayCoordsBuffer.put(f);
        overlayCoordsBuffer.flip();

        IntBuffer hasOverlayBuffer = MemoryUtil.memAllocInt(hasOverlay.size());
        for (Integer f : hasOverlay)
            hasOverlayBuffer.put(f);
        hasOverlayBuffer.flip();

        if(Thread.currentThread() == GameEngine.currentThread){
            try{
                glBindVertexArray(vaoId);
                int pid = vboIdList.get(3);
                glBindBuffer(GL_ARRAY_BUFFER, pid);
                glBufferData(GL_ARRAY_BUFFER, overlayCoordsBuffer, GL_STATIC_DRAW);
                glVertexAttribPointer(3, 2, GL_FLOAT, false, 0, 0);

                pid = vboIdList.get(4);
                glBindBuffer(GL_ARRAY_BUFFER, pid);
                glBufferData(GL_ARRAY_BUFFER, hasOverlayBuffer, GL_STATIC_DRAW);
                glVertexAttribPointer(4, 1, GL_INT, false, 0, 0);

                glBindBuffer(GL_ARRAY_BUFFER, 0);
                glBindVertexArray(0);
            }finally {
                MemoryUtil.memFree(overlayCoordsBuffer);
                MemoryUtil.memFree(hasOverlayBuffer);
            }
        }else{
            GameHandler.getInstance().getGameEngine().addQueueItem(() -> {
                try{
                    glBindVertexArray(vaoId);
                    int pid = vboIdList.get(3);
                    glBindBuffer(GL_ARRAY_BUFFER, pid);
                    glBufferData(GL_ARRAY_BUFFER, overlayCoordsBuffer, GL_STATIC_DRAW);
                    glVertexAttribPointer(3, 2, GL_FLOAT, false, 0, 0);

                    pid = vboIdList.get(4);
                    glBindBuffer(GL_ARRAY_BUFFER, pid);
                    glBufferData(GL_ARRAY_BUFFER, hasOverlayBuffer, GL_STATIC_DRAW);
                    glVertexAttribPointer(4, 1, GL_INT, false, 0, 0);

                    glBindBuffer(GL_ARRAY_BUFFER, 0);
                    glBindVertexArray(0);
                }finally {
                    MemoryUtil.memFree(overlayCoordsBuffer);
                    MemoryUtil.memFree(hasOverlayBuffer);
                }
            });
        }
    }

    @Override
    public RenderQuery getQuery() {
        return query;
    }

}