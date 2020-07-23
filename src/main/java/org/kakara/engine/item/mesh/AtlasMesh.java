package org.kakara.engine.item.mesh;

import org.kakara.engine.GameEngine;
import org.kakara.engine.engine.CubeData;
import org.kakara.engine.exceptions.InvalidThreadException;
import org.kakara.engine.item.GameItem;
import org.kakara.engine.item.Material;
import org.kakara.engine.renderobjects.RenderTexture;
import org.kakara.engine.renderobjects.TextureAtlas;
import org.kakara.engine.renderobjects.renderlayouts.Layout;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.nio.IntBuffer;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static org.lwjgl.opengl.GL30.*;

/**
 * The normal mesh for game items.
 * If a ton of objects use this mesh, than consider changing to {@link InstancedMesh}.
 * <p>This class is <b>not</b> thread safe.</p>
 * @since 1.0-Pre1
 */
public class AtlasMesh implements IMesh {

    protected final int vaoId;

    protected final List<Integer> vboIdList;

    private final int vertexCount;

    private TextureAtlas atlas;

    private boolean wireframe;

    /**
     * Create a new AtlasMesh
     * @param texture The RenderTexture to use
     * @param atlas The texture atlas
     * @param layout The layout to use.
     * @param positions The list of positions
     * @param normals The list of normals.
     * @param indices The indices
     */
    public AtlasMesh(RenderTexture texture, TextureAtlas atlas, Layout layout, float[] positions, float[] normals, int[] indices) {
        if(Thread.currentThread() != GameEngine.currentThread)
            throw new InvalidThreadException("This class can only be constructed on the main thread!");
        this.atlas = atlas;
        List<Float> textCords = new ArrayList<>();
        textCords.addAll(layout.getTextureCords().getFront(texture.getXOffset(), texture.getYOffset(), atlas.getNumberOfRows()));
        textCords.addAll(layout.getTextureCords().getBack(texture.getXOffset(), texture.getYOffset(), atlas.getNumberOfRows()));
        textCords.addAll(layout.getTextureCords().getTop(texture.getXOffset(), texture.getYOffset(), atlas.getNumberOfRows()));
        textCords.addAll(layout.getTextureCords().getBottom(texture.getXOffset(), texture.getYOffset(), atlas.getNumberOfRows()));
        textCords.addAll(layout.getTextureCords().getRight(texture.getXOffset(), texture.getYOffset(), atlas.getNumberOfRows()));
        textCords.addAll(layout.getTextureCords().getLeft(texture.getXOffset(), texture.getYOffset(), atlas.getNumberOfRows()));

        System.out.println(textCords.size());

        float[] data = new float[textCords.size()];
        int i = 0;
        for(Float f : textCords){
            data[i] = f;
            i++;
        }

        FloatBuffer posBuffer = null;
        FloatBuffer textCoordsBuffer = null;
        FloatBuffer vecNormalsBuffer = null;
        IntBuffer indicesBuffer = null;
        try {

            if(indices != null)
                vertexCount = indices.length;
            else{
                vertexCount = positions.length/3;
            }
            vboIdList = new ArrayList<>();

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
            textCoordsBuffer = MemoryUtil.memAllocFloat(data.length);
            textCoordsBuffer.put(data).flip();
            System.out.println(CubeData.texture.length);
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
            MemoryUtil.memFree(textCoordsBuffer);
            if (vecNormalsBuffer != null) {
                MemoryUtil.memFree(vecNormalsBuffer);
            }
            if (indicesBuffer != null) {
                MemoryUtil.memFree(indicesBuffer);
            }
        }
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


    protected void initRender() {
        // Activate first texture bank
        glActiveTexture(GL_TEXTURE0);
        // Bind the texture
        glBindTexture(GL_TEXTURE_2D, atlas.getTexture().getId());

        // Draw the mesh
        glBindVertexArray(getVaoId());
        glEnableVertexAttribArray(0);
        glEnableVertexAttribArray(1);
    }

    protected void endRender() {
        // Restore state
        glDisableVertexAttribArray(0);
        glDisableVertexAttribArray(1);
        glBindVertexArray(0);

        glBindTexture(GL_TEXTURE_2D, 0);
    }

    /**
     * Render the mesh.
     * <p>Internal use only.</p>
     */
    public void render() {
        initRender();

        if(isWireframe())
            glPolygonMode(GL_FRONT_AND_BACK, GL_LINE);

        glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);

        if(isWireframe())
            glPolygonMode(GL_FRONT_AND_BACK, GL_FILL);

        endRender();
    }

    /**
     * Render the mesh.
     * <p>Internal use only.</p>
     * @param gameItems The game items to render
     * @param consumer The consumer
     */
    @Override
    public void renderList(List<GameItem> gameItems, Consumer<GameItem> consumer){
        initRender();
        for(GameItem gameItem : gameItems){
            consumer.accept(gameItem);
            glDrawElements(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0);
        }
        endRender();
    }

    @Override
    public Optional<Material> getMaterial(){
        return Optional.empty();
    }

    /**
     * If you want the mesh to be rendered as a wireframe.
     * @since 1.0-Pre2
     * @param value If the mesh is a wireframe.
     */
    @Override
    public void setWireframe(boolean value){
        this.wireframe = value;
    }

    /**
     * If the mesh is a wire frame.
     * @return If the mesh is a wireframe.
     */
    @Override
    public boolean isWireframe(){
        return this.wireframe;
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

        // Delete the VAO
        glBindVertexArray(0);
        glDeleteVertexArrays(vaoId);
    }
}
