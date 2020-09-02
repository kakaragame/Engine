package org.kakara.engine.gameitems.mesh;

import org.joml.Matrix4f;
import org.kakara.engine.GameHandler;
import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.math.KMath;
import org.kakara.engine.render.Transformation;
import org.kakara.engine.render.culling.FrustumCullingFilter;
import org.lwjgl.system.MemoryUtil;

import java.nio.FloatBuffer;
import java.util.List;

import static org.lwjgl.opengl.GL15.*;
import static org.lwjgl.opengl.GL20.glDisableVertexAttribArray;
import static org.lwjgl.opengl.GL20.glEnableVertexAttribArray;
import static org.lwjgl.opengl.GL20C.glVertexAttribPointer;
import static org.lwjgl.opengl.GL30.glBindVertexArray;
import static org.lwjgl.opengl.GL31.glDrawElementsInstanced;
import static org.lwjgl.opengl.GL33.glVertexAttribDivisor;

/**
 * A version of the Mesh that takes advantage of OpenGL's instanced rendering.
 * <p>This is to be used when you have a lot of the same object that you want to render. (Must be the same mesh and texture.)</p>
 * <p>This class is <b>not</b> thread safe.</p>
 */
public class InstancedMesh extends Mesh {

    private static final int VECTOR4F_SIZE_BYTES = 4 * 4;

    private static final int MATRIX_SIZE_BYTES = 4 * InstancedMesh.VECTOR4F_SIZE_BYTES;

    private static final int MATRIX_SIZE_FLOATS = 4 * 4;

    private static final int FLOAT_SIZE_BYTES = 4;

    private static final int INSTANCE_SIZE_BYTES = InstancedMesh.MATRIX_SIZE_BYTES * 2 + InstancedMesh.FLOAT_SIZE_BYTES * 2 + InstancedMesh.FLOAT_SIZE_BYTES;

    private static final int INSTANCE_SIZE_FLOATS = InstancedMesh.MATRIX_SIZE_FLOATS * 2 + 3;

    private final int numInstances;

    private final int modelViewVBO;

    private final int modelLightViewVBO;

    private FloatBuffer modelViewBuffer;

    private FloatBuffer modelLightViewBuffer;

    public InstancedMesh(float[] positions, float[] textCoords, float[] normals, int[] indices, int numInstances) {
        super(positions, textCoords, normals, indices, Mesh.createEmptyIntArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0), Mesh.createEmptyFloatArray(Mesh.MAX_WEIGHTS * positions.length / 3, 0));

        this.numInstances = numInstances;

        glBindVertexArray(vaoId);

        modelViewVBO = glGenBuffers();
        vboIdList.add(modelViewVBO);
        this.modelViewBuffer = MemoryUtil.memAllocFloat(numInstances * MATRIX_SIZE_FLOATS);
        glBindBuffer(GL_ARRAY_BUFFER, modelViewVBO);
        int start = 5;
        int strideStart = 0;
        for (int i = 0; i < 4; i++) {
            glVertexAttribPointer(start, 4, GL_FLOAT, false, MATRIX_SIZE_BYTES, strideStart);
            glVertexAttribDivisor(start, 1);
            start++;
            strideStart += InstancedMesh.VECTOR4F_SIZE_BYTES;
        }

        // Light view matrix
        modelLightViewVBO = glGenBuffers();
        vboIdList.add(modelLightViewVBO);
        this.modelLightViewBuffer = MemoryUtil.memAllocFloat(numInstances * InstancedMesh.MATRIX_SIZE_FLOATS);
        glBindBuffer(GL_ARRAY_BUFFER, modelLightViewVBO);
        for (int i = 0; i < 4; i++) {
            glVertexAttribPointer(start, 4, GL_FLOAT, false, InstancedMesh.MATRIX_SIZE_BYTES, strideStart);
            glVertexAttribDivisor(start, 1);
            glEnableVertexAttribArray(start);
            start++;
            strideStart += InstancedMesh.VECTOR4F_SIZE_BYTES;
        }

        glVertexAttribPointer(start, 1, GL_FLOAT, false, InstancedMesh.INSTANCE_SIZE_BYTES, strideStart);
        glVertexAttribDivisor(start, 1);
        glEnableVertexAttribArray(start);

        glBindBuffer(GL_ARRAY_BUFFER, 0);
        glBindVertexArray(0);
    }

    @Override
    protected void initRender() {
        super.initRender();
        int start = 5;
        int numElements = 4 * 2;
        for (int i = 0; i < numElements; i++) {
            glEnableVertexAttribArray(start + i);
        }
    }

    @Override
    protected void endRender() {
        int start = 5;
        int numElements = 4 * 2;
        for (int i = 0; i < numElements; i++) {
            glDisableVertexAttribArray(start + i);
        }
        super.endRender();
    }

    public void renderListInstanced(List<GameItem> gameItems, boolean depthMap, Transformation transformation, Matrix4f viewMatrix, Matrix4f lightViewMatrix) {
        initRender();

        int chunkSize = numInstances;
        int length = gameItems.size();
        for (int i = 0; i < length; i += chunkSize) {
            int end = Math.min(length, i + chunkSize);
            List<GameItem> subList = gameItems.subList(i, end);
            renderChunkInstanced(subList, depthMap, transformation, viewMatrix, lightViewMatrix);
        }

        endRender();
    }

    private void renderChunkInstanced(List<GameItem> gameItems, boolean depthMap, Transformation transformation, Matrix4f viewMatrix, Matrix4f lightViewMatrix) {
        this.modelViewBuffer.clear();
        this.modelLightViewBuffer.clear();
        int i = 0;
        FrustumCullingFilter filter = GameHandler.getInstance().getGameEngine().getRenderer().getFrustumFilter();
        for (GameItem gameItem : gameItems) {
            if (!filter.testCollider(gameItem.getCollider()))
                continue;

            if (KMath.distance(gameItem.getPosition(), GameHandler.getInstance().getCurrentScene().getCamera().getPosition()) > 100)
                continue;

            Matrix4f modelMatrix = transformation.buildModelMatrix(gameItem);
            if (!depthMap) {
                Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
                modelViewMatrix.get(MATRIX_SIZE_FLOATS * i, modelViewBuffer);
            }
            Matrix4f modelLightViewMatrix = transformation.buildModelLightViewMatrix(modelMatrix, lightViewMatrix);
            modelLightViewMatrix.get(MATRIX_SIZE_FLOATS * i, this.modelLightViewBuffer);
            i++;
        }
        glBindBuffer(GL_ARRAY_BUFFER, modelViewVBO);
        glBufferData(GL_ARRAY_BUFFER, modelViewBuffer, GL_DYNAMIC_DRAW);
        glBindBuffer(GL_ARRAY_BUFFER, modelLightViewVBO);
        glBufferData(GL_ARRAY_BUFFER, modelLightViewBuffer, GL_DYNAMIC_DRAW);
        glDrawElementsInstanced(GL_TRIANGLES, getVertexCount(), GL_UNSIGNED_INT, 0, i);
        glBindBuffer(GL_ARRAY_BUFFER, 0);
    }

    @Override
    public void cleanUp() {
        super.cleanUp();
        if (this.modelViewBuffer != null) {
            MemoryUtil.memFree(this.modelViewBuffer);
            this.modelViewBuffer = null;
        }
        if (this.modelLightViewBuffer != null) {
            MemoryUtil.memFree(this.modelLightViewBuffer);
            this.modelLightViewBuffer = null;
        }
    }
}
