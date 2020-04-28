package org.kakara.engine.item;

import org.jetbrains.annotations.NotNull;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL30.*;

/**
 * The normal mesh for game items.
 * If a ton of objects use this mesh, than consider changing to {@link InstancedMesh}.
 * <p>This class is <b>not</b> thread safe.</p>
 */
public class Mesh {
    public static final int MAX_WEIGHTS = 4;

    protected final int vaoId;

    protected final List<Integer> vboIdList;

    private final int vertexCount;

    private Material material;

    private float boundingRadius;

    /**
     *
     * @param positions The vertex positions. See the code at {@link org.kakara.engine.engine.CubeData#vertex} for an example.
     * @param textCoords The texture positions. See the code at {@link org.kakara.engine.engine.CubeData#texture} for an example.
     * @param normals The normal positions.
     *                This is used to tell the code what direction the light should bounce off of the faces. See the code at {@link org.kakara.engine.engine.CubeData#normal} for an example.
     * @param indices The indices positions (An int array). This is used to shorten the three lists above.
     *                So indices one will indicate position # 1 and texture # 1 and normal # 1.
     *                See the code at {@link org.kakara.engine.engine.CubeData#texture} for an example.
     */
    public Mesh(@NotNull float[] positions, @NotNull float[] textCoords, @NotNull float[] normals, @NotNull int[] indices) {
        this(positions, textCoords, normals, indices, createEmptyIntArray(MAX_WEIGHTS * positions.length / 3, 0), createEmptyFloatArray(MAX_WEIGHTS * positions.length / 3, 0));
    }

    /**
     *
     * @param positions See above.
     * @param textCoords See above.
     * @param normals See above.
     * @param indices See above.
     * @param jointIndices Not implemented
     * @param weights Not implemented
     */
    public Mesh(@NotNull float[] positions, @NotNull float[] textCoords, @NotNull float[] normals, @NotNull int[] indices, @NotNull int[] jointIndices, @NotNull float[] weights) {
        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        FloatBuffer weightsBuffer = null;
        IntBuffer jointIndicesBuffer = null;
        IntBuffer indicesBuffer = null;
        try {
            calculateBoundingRadius(positions);

            if(indices != null)
                vertexCount = indices.length;
            else{
                vertexCount = positions.length/3;
            }
            vboIdList = new ArrayList();

            vaoId = glGenVertexArrays();
            glBindVertexArray(vaoId);

            // Position VBO
            int vboId = glGenBuffers();
            vboIdList.add(vboId);
            posBuffer = MemoryUtil.memAllocFloat(positions.length);
            posBuffer.put(positions).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, posBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(0, 3, GL_FLOAT, false, 0, 0);

            // Texture coordinates VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            textCoordsBuffer = MemoryUtil.memAllocFloat(textCoords.length);
            textCoordsBuffer.put(textCoords).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, textCoordsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(1, 2, GL_FLOAT, false, 0, 0);

            // Vertex normals VBO
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            vecNormalsBuffer = MemoryUtil.memAllocFloat(normals.length);
            vecNormalsBuffer.put(normals).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, vecNormalsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(2, 3, GL_FLOAT, false, 0, 0);

            // Weights
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            weightsBuffer = MemoryUtil.memAllocFloat(weights.length);
            weightsBuffer.put(weights).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, weightsBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(3, 4, GL_FLOAT, false, 0, 0);

            // Joint indices
            vboId = glGenBuffers();
            vboIdList.add(vboId);
            jointIndicesBuffer = MemoryUtil.memAllocInt(jointIndices.length);
            jointIndicesBuffer.put(jointIndices).flip();
            glBindBuffer(GL_ARRAY_BUFFER, vboId);
            glBufferData(GL_ARRAY_BUFFER, jointIndicesBuffer, GL_STATIC_DRAW);
            glVertexAttribPointer(4, 4, GL_FLOAT, false, 0, 0);

            // Index VBO
            if(indices != null) {
                vboId = glGenBuffers();
                vboIdList.add(vboId);
                indicesBuffer = MemoryUtil.memAllocInt(indices.length);
                indicesBuffer.put(indices).flip();
                glBindBuffer(GL_ELEMENT_ARRAY_BUFFER, vboId);
                glBufferData(GL_ELEMENT_ARRAY_BUFFER, indicesBuffer, GL_STATIC_DRAW);
            }

            glBindBuffer(GL_ARRAY_BUFFER, 0);
            glBindVertexArray(0);
        } finally {
            if (posBuffer != null) {
                MemoryUtil.memFree(posBuffer);
            }
            if (textCoordsBuffer != null) {
                MemoryUtil.memFree(textCoordsBuffer);
            }
            if (vecNormalsBuffer != null) {
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (weightsBuffer != null) {
                MemoryUtil.memFree(weightsBuffer);
            }
            if (jointIndicesBuffer != null) {
                MemoryUtil.memFree(jointIndicesBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
    }

    private void calculateBoundingRadius(float positions[]) {
        int length = positions.length;
        boundingRadius = 0;
        for(int i=0; i< length; i++) {
            float pos = positions[i];
            boundingRadius = Math.max(Math.abs(pos), boundingRadius);
        }
    }

    /**
     * Get the material of the mesh
     * @return The material
     */
    public Material getMaterial() {
        return material;
    }

    /**
     * Set the material of the mesh.
     * @param material The material
     */
    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Get the vao id of the mesh
     * @return The vaoid.
     */
    public final int getVaoId() {
        return vaoId;
    }

    /**
     * Get the vertex count of the mesh
     * @return The vertex count.
     */
    public int getVertexCount() {
        return vertexCount;
    }

    /**
     * Get the bounding radius.
     * <p><b>Not Implemented</b></p>
     * @return The bounding radius
     */
    public float getBoundingRadius() {
        return boundingRadius;
    }

    /**
     * Set the bounding radius.
     * @param boundingRadius The bounding radius.
     */
    public void setBoundingRadius(float boundingRadius) {
        this.boundingRadius = boundingRadius;
    }

    protected void initRender() {
        Texture texture = material != null ? material.getTexture() : null;
        if (texture != null) {
            // Activate first texture bank
            glActiveTexture(GL_TEXTURE0);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, texture.getId());
        }
        Texture normalMap = material != null ? material.getNormalMap() : null;
        if (normalMap != null) {
            // Activate second texture bank
            glActiveTexture(GL_TEXTURE1);
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, normalMap.getId());
        }

//        Texture specMap = material != null ? material.getSpecularMap() : null;
//        if (specMap != null) {
//            // Activate third texture bank
//            glActiveTexture(GL_TEXTURE2);
//            // Bind the texture
//            glBindTexture(GL_TEXTURE_2D, specMap.getId());
//        }

        int[] textures = {GL_TEXTURE3, GL_TEXTURE4, GL_TEXTURE5, GL_TEXTURE6, GL_TEXTURE7};
        for(int i = 0; i < material.getOverlayTextures().size(); i++){
            Texture ovText = material != null ? material.getOverlayTextures().get(i) : null;
            if (ovText != null) {
                // Activate i texture bank
                glActiveTexture(textures[i]);
                // Bind the texture
                glBindTexture(GL_TEXTURE_2D, ovText.getId());
            }
        }


        // Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
        glEnableVertexAttribArray(2);
        glEnableVertexAttribArray(3);
        glEnableVertexAttribArray(4);
    }

    protected void endRender() {
        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glDisableVertexAttribArray(2);
        glDisableVertexAttribArray(3);
        glDisableVertexAttribArray(4);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Render the mesh.
     * <p>Internal use only.</p>
     */
    public void render() {
        initRender();

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        endRender();
    }

    /**
     * Render the mesh.
     * <p>Internal use only.</p>
     * @param gameItems The game items to render
     * @param consumer The consumer
     */
    public void renderList(List<GameItem> gameItems, Consumer<GameItem> consumer){
        initRender();
        for(GameItem gameItem : gameItems){
            consumer.accept(gameItem);
            glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        endRender();
    }


    /**
     * Cleanup the mesh
     */
    public void cleanUp() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the texture
        Texture texture = material.getTexture();
        if (texture != null) {
            texture.cleanup();
        }

        for(int i = 0; i < material.getOverlayTextures().size(); i++){
            material.getOverlayTextures().get(i).cleanup();
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    /**
     * Delete buffers
     * @deprecated Use {@link #cleanUp()}
     */
    public void deleteBuffers() {
        glDisableVertexAttribArray(0);

        // Delete the VBOs
        glBindBuffer(GL_ARRAY_BUFFER, 0);
        for (int vboId : vboIdList) {
            glDeleteBuffers(vboId);
        }

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }

    protected static float[] createEmptyFloatArray(int length, float defaultValue) {
        float[] result = new float[length];
        Arrays.fill(result, defaultValue);
        return result;
    }

    protected static int[] createEmptyIntArray(int length, int defaultValue) {
        int[] result = new int[length];
        Arrays.fill(result, defaultValue);
        return result;
    }
}
