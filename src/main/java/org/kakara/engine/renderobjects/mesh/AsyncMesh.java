package org.kakara.engine.renderobjects.mesh;

import org.jetbrains.annotations.Nullable;
import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.renderobjects.ChunkHandler;
import org.kakara.engine.renderobjects.RenderBlock;
import org.kakara.engine.renderobjects.RenderChunk;
import org.kakara.engine.renderobjects.TextureAtlas;
import org.kakara.engine.renderobjects.renderlayouts.MeshLayout;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL30.*;

/**
 * This calculates the data for the Mesh on a different thread.
 *
 * @since 1.0-Pre2
 */
public class AsyncMesh implements RenderMesh {

    protected final List<Integer> vboIdList;
    protected int vaoId;
    private int vertexCount;
    private boolean finished;

    /**
     * Create a render mesh
     *
     * @param renderChunk  renderchunk
     * @param textureAtlas The texture atlas to use
     * @param future       The completable future that is to be completed once the generation is complete.
     */
    public AsyncMesh(RenderChunk renderChunk, TextureAtlas textureAtlas, @Nullable CompletableFuture<AsyncMesh> future) {
        vboIdList = new ArrayList<>();
        AsyncMesh instance = this;

        GameHandler.getInstance().getGameEngine().addQueueItem(() -> vaoId = glGenVertexArrays());

        ChunkHandler.EXECUTORS.submit(() -> {
            List<RenderBlock> renderBlocks = renderChunk.calculateVisibleBlocks();
            MeshLayout layout = null;
            try {
                layout = MeshUtils.setupLayout(renderBlocks, textureAtlas);
                vertexCount = layout.getVertexLength();
            } catch (Exception e) {
                GameEngine.LOGGER.error("Error While Building RenderChunk", e);
            }
            if (layout == null) {
                return;
            }

            finished = true;

            MeshLayout finalLayout = layout;
            GameHandler.getInstance().getGameEngine().addQueueItem(new Runnable() {
                @Override
                public void run() {
                    System.out.println(Thread.currentThread().getName());
                    try {
                        glBindVertexArray(vaoId);

                        // Position VBO
                        int vboId = glGenBuffers();
                        vboIdList.add(vboId);
                        glBindBuffer(GL_ARRAY_BUFFER, vboId);
                        glBufferData(GL_ARRAY_BUFFER, finalLayout.getVertex(), GL_STATIC_DRAW);
                        glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

                        // Texture Coordinates VBO
                        vboId = glGenBuffers();
                        vboIdList.add(vboId);
                        glBindBuffer(GL_ARRAY_BUFFER, vboId);
                        glBufferData(GL_ARRAY_BUFFER, finalLayout.getTextCoords(), GL_STATIC_DRAW);
                        glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

                        //Vertex Normals VBO
                        vboId = glGenBuffers();
                        vboIdList.add(vboId);
                        glBindBuffer(GL_ARRAY_BUFFER, vboId);
                        glBufferData(GL_ARRAY_BUFFER, finalLayout.getNormals(), GL_STATIC_DRAW);
                        glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

                        //Overlay Texture VBO
                        vboId = glGenBuffers();
                        vboIdList.add(vboId);
                        glBindBuffer(GL_ARRAY_BUFFER, vboId);
                        glBufferData(GL_ARRAY_BUFFER, finalLayout.getOverlayCoords(), GL_STATIC_DRAW);
                        glVertexAttribPointer(3, 2, GL_FLOAT, false, 0, 0);

                        //Has Overlay Texture VBO
                        vboId = glGenBuffers();
                        vboIdList.add(vboId);
                        glBindBuffer(GL_ARRAY_BUFFER, vboId);
                        glBufferData(GL_ARRAY_BUFFER, finalLayout.getHasOverlay(), GL_STATIC_DRAW);
                        glVertexAttribPointer(4, 1, GL_INT, false, 0, 0);

                        // Indices VBO
                        vboId = glGenBuffers();
                        vboIdList.add(vboId);
                        glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
                        glBufferData(GL_ELEMENT_ARRAY_BUFFER, finalLayout.getIndices(), GL_STATIC_DRAW);

                        glBindBuffer(GL_ARRAY_BUFFER, 0);
                        glBindVertexArray(0);


                    } catch (Exception e) {
                        GameEngine.LOGGER.error("Error While Building RenderChunk", e);
                    } finally {
                        if (finalLayout.getVertex() != null)
                            MemoryUtil.memFree(finalLayout.getVertex());
                        if (finalLayout.getTextCoords() != null)
                            MemoryUtil.memFree(finalLayout.getTextCoords());
                        if (finalLayout.getNormals() != null)
                            MemoryUtil.memFree(finalLayout.getNormals());
                        if (finalLayout.getIndices() != null)
                            MemoryUtil.memFree(finalLayout.getIndices());
                        if (finalLayout.getOverlayCoords() != null)
                            MemoryUtil.memFree(finalLayout.getOverlayCoords());
                        if (finalLayout.getHasOverlay() != null)
                            MemoryUtil.memFree(finalLayout.getHasOverlay());
                    }
                    if (future != null)
                        future.complete(instance);
                }
            });

        });
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
        if (!finished) return;
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
    }

    @Override
    public void updateOverlay(List<RenderBlock> blocks, TextureAtlas textureAtlas) {
        List<Float> overlayCoords = new ArrayList<>();
        List<Integer> hasOverlay = new ArrayList<>();

        for (RenderBlock rb : blocks) {
            int initial = overlayCoords.size() / 2;
            rb.getOverlayFromFaces(overlayCoords, textureAtlas);
            hasOverlay.addAll(Collections.nCopies((overlayCoords.size() / 2 - initial), rb.getOverlay() == null ? 0 : 1));
        }

        FloatBuffer overlayCoordsBuffer = MemoryUtil.memAllocFloat(overlayCoords.size());
        for (Float f : overlayCoords)
            overlayCoordsBuffer.put(f);
        overlayCoordsBuffer.flip();

        IntBuffer hasOverlayBuffer = MemoryUtil.memAllocInt(hasOverlay.size());
        for (Integer f : hasOverlay)
            hasOverlayBuffer.put(f);
        hasOverlayBuffer.flip();

        if (Thread.currentThread() == GameEngine.currentThread) {
            try {
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
            } finally {
                MemoryUtil.memFree(overlayCoordsBuffer);
                MemoryUtil.memFree(hasOverlayBuffer);
            }
        } else {
            GameHandler.getInstance().getGameEngine().addQueueItem(() -> {
                try {
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
                } finally {
                    MemoryUtil.memFree(overlayCoordsBuffer);
                    MemoryUtil.memFree(hasOverlayBuffer);
                }
            });
        }
    }


}