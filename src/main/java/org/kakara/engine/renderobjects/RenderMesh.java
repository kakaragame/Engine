package org.kakara.engine.renderobjects;

import org.jetbrains.annotations.Nullable;
import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.renderobjects.renderlayouts.MeshLayout;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.*;
import java.util.concurrent.CompletableFuture;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL30.*;

/**
 * THe mesh for the chunks.
 */
public class RenderMesh {

    protected int vaoId;
    protected final List<Integer> vboIdList;
    private int vertexCount;
    private boolean finished;

    /**
     * Create a render mesh
     *
     * @param renderBlocks The list of render blocks
     * @param textureAtlas The texture atlas to use
     * @param async        If this mesh is to be generated async.
     */
    public RenderMesh(List<RenderBlock> renderBlocks, TextureAtlas textureAtlas, boolean async) {
        this(renderBlocks, textureAtlas, async, null);
    }

    /**
     * Create a render mesh
     *
     * @param renderBlocks The list of render blocks
     * @param textureAtlas The texture atlas to use
     * @param async        If this mesh is to be generated async
     * @param future       The completable future that is to be completed once the generation is complete.
     */
    public RenderMesh(List<RenderBlock> renderBlocks, TextureAtlas textureAtlas, boolean async, @Nullable CompletableFuture<RenderMesh> future) {
        vboIdList = new ArrayList<>();
        if (!async)
            vaoId = glGenVertexArrays();
        if (async)
            GameHandler.getInstance().getGameEngine().addQueueItem(() -> vaoId = glGenVertexArrays());
        if (!async) {
            finished = true;
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
        } else {
            RenderMesh instance = this;

            ChunkHandler.EXECUTORS.submit(() -> {

                MeshLayout layout = null;
                try {
                    layout = setupLayout(renderBlocks, textureAtlas);
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
                        }
                        if (future != null)
                            future.complete(instance);
                    }
                });

            });
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
        if (!finished) return;
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
//    private MeshLayout setupLayout(List<RenderBlock> renderBlocks, TextureAtlas textureAtlas) {
//        float[] positions = {};
//        float[] texCoords = {};
//        float[] normals = {};
//
//        float[] copy;
//        float[] both;
//
//        int[] indicies = {};
//        int count = 0;
//        for (RenderBlock rb : renderBlocks) {
//            copy = rb.getVertexFromFaces();
//            both = Arrays.copyOf(positions, positions.length + copy.length);
//            System.arraycopy(copy, 0, both, positions.length, copy.length);
//            positions = both;
//
//            copy = rb.getTextureFromFaces(textureAtlas);
//            both = Arrays.copyOf(texCoords, texCoords.length + copy.length);
//            System.arraycopy(copy, 0, both, texCoords.length, copy.length);
//            texCoords = both;
//
//            copy = rb.getNormalsFromFaces();
//            both = Arrays.copyOf(normals, normals.length + copy.length);
//            System.arraycopy(copy, 0, both, normals.length, copy.length);
//            normals = both;
//
//            int[] indCopy = rb.getIndicesFromFaces(count);
//            int[] indBoth = Arrays.copyOf(indicies, indicies.length + indCopy.length);
//            System.arraycopy(indCopy, 0, indBoth, indicies.length, indCopy.length);
//            indicies = indBoth;
//            count += rb.getVisibleFaces().size() * 4;
//        }
//
//        final float[] finalPos = positions;
//        final float[] finalTexCord = texCoords;
//        final float[] finalNormals = normals;
//        final int[] finalIndices = indicies;
//        return new MeshLayout() {
//            @Override
//            public float[] getVertex() {
//                return finalPos;
//            }
//
//            @Override
//            public float[] getTextCoords() {
//                return finalTexCord;
//            }
//
//            @Override
//            public float[] getNormals() {
//                return finalNormals;
//            }
//
//            @Override
//            public int[] getIndices() {
//                return finalIndices;
//            }
//        };
//    }

    private MeshLayout setupLayout(List<RenderBlock> renderBlocks, TextureAtlas textureAtlas) {
        List<Float> positions = new ArrayList<>();
        List<Float> texCoords = new ArrayList<>();
        List<Float> normals = new ArrayList<>();
        List<Integer> indicies = new ArrayList<>();
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
        for(Integer f : indicies)
            indicesBuffer.put(f);
        indicesBuffer.flip();
        return new MeshLayout() {
            @Override
            public FloatBuffer getVertex() {
                return posBuffer;
            }

            @Override
            public FloatBuffer getTextCoords() {
                return texCoordsBuffer;
            }

            @Override
            public FloatBuffer getNormals() {
                return vecNormalsBuffer;
            }

            @Override
            public IntBuffer getIndices() {
                return indicesBuffer;
            }

            @Override
            public int getVertexLength() {
                return indicies.size();
            }
        };
    }

}