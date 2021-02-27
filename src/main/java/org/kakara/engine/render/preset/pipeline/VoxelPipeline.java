package org.kakara.engine.render.preset.pipeline;

import org.joml.Matrix4f;
import org.kakara.engine.exceptions.render.ShaderNotFoundException;
import org.kakara.engine.lighting.LightHandler;
import org.kakara.engine.lighting.ShadowMap;
import org.kakara.engine.render.*;
import org.kakara.engine.render.culling.FrustumCullingFilter;
import org.kakara.engine.voxels.VoxelChunk;
import org.kakara.engine.scene.AbstractGameScene;
import org.kakara.engine.scene.Scene;

import java.util.List;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE0;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * The default render pipeline for the Voxel system.
 *
 * @since 1.0-Pre4
 */
// TODO fix depth map by copying standerd pipeline.
public class VoxelPipeline implements RenderPipeline {

    private Shader chunkShaderProgram;
    private ShaderManager manager;
    private Transformation transformation;
    private FrustumCullingFilter frustumFilter;
    private ShadowMap shadowMap;

    @Override
    public void init(ShaderManager manager, Transformation transformation, FrustumCullingFilter frustumFilter, ShadowMap shadowMap) {
        this.manager = manager;
        chunkShaderProgram = manager.findShader("Voxel").getShader();
        this.transformation = transformation;
        this.frustumFilter = frustumFilter;
        this.shadowMap = shadowMap;
    }

    /**
     * Set the shader of the Chunk Pipeline.
     *
     * @param shader The name of the shader.
     */
    public void setShader(String shader) {
        ShaderProgram program = manager.findShader(shader);
        if (program == null)
            throw new ShaderNotFoundException("Unable to find shader: " + shader);
        this.chunkShaderProgram = program.getShader();
    }

    @Override
    public void render(Scene scene) {
        renderChunk(scene);
    }


    @Override
    public void renderDepthMap(Scene scene, Shader depthShader, Matrix4f lightViewMatrix) {
        if (!(scene instanceof AbstractGameScene))
            return;
        AbstractGameScene ags = (AbstractGameScene) scene;
        if (ags.getChunkHandler().getVoxelChunkList().isEmpty())
            return;
        List<VoxelChunk> renderChunks = ags.getChunkHandler().getVoxelChunkList();
        if (renderChunks == null) return;

        for (VoxelChunk renderChunk : renderChunks) {
            if (renderChunk == null) continue;
            if (renderChunk.getVoxelCount() < 1) continue;

            if (!frustumFilter.testRenderObject(renderChunk.transform.getPosition(), 16, 16, 16))
                continue;

            Matrix4f modelMatrix = transformation.buildModelMatrix(renderChunk);
            Matrix4f modelLightViewMatrix = transformation.buildModelLightViewMatrix(modelMatrix, lightViewMatrix);
            depthShader.setUniform("modelLightViewMatrix", modelLightViewMatrix);
            depthShader.setUniform("orthoProjectionMatrix", transformation.getOrthoProjectionMatrix());


            renderChunk.render();
        }


        glBindTexture(GL_TEXTURE_2D, 0);
    }

    private void renderChunk(Scene scene) {
        if (!(scene instanceof AbstractGameScene)) return;
        AbstractGameScene ags = (AbstractGameScene) scene;
        if (ags.getChunkHandler().getVoxelChunkList().isEmpty())
            return;
        List<VoxelChunk> renderChunks = ags.getChunkHandler().getVoxelChunkList();
        if (renderChunks == null) return;
        chunkShaderProgram.bind();
        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        chunkShaderProgram.setUniform("projectionMatrix", projectionMatrix);
        Matrix4f orthoProjMatrix = transformation.getOrthoProjectionMatrix();
        chunkShaderProgram.setUniform("orthoProjectionMatrix", orthoProjMatrix);
        Matrix4f lightViewMatrix = transformation.getLightViewMatrix();

        Matrix4f viewMatrix = scene.getCamera().getViewMatrix();

        // Render Lighting
        LightHandler lh = ags.getLightHandler();
        assert lh != null;
        Graphics.renderLights(scene, scene.getCamera(), lh, chunkShaderProgram);
        chunkShaderProgram.setUniform("textureAtlas", 0);
        chunkShaderProgram.setUniform("reflectance", 1f);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, ags.getTextureAtlas().getTexture().getId());

        // TODO one day reimplement occlusion culling.
        //doOcclusionTest(renderChunks, chunkShaderProgram, viewMatrix, lightViewMatrix);

        for (VoxelChunk renderChunk : renderChunks) {
            if (renderChunk == null) continue;
            if (renderChunk.getVoxelCount() < 1) continue;

            if (!frustumFilter.testRenderObject(renderChunk.transform.getPosition(), 16, 16, 16))
                continue;
//            RenderMesh mesh = renderChunk.getRenderMesh();
//            if (mesh == null || mesh.getQuery() == null)
//                continue;
//            int i = mesh.getQuery().pollPreviousResult();
//
//            if (i == GL_FALSE)
//                continue;

            Matrix4f modelMatrix = transformation.buildModelMatrix(renderChunk);

            Graphics.bindShadowMap(shadowMap);

            Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);

            chunkShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);

            Matrix4f modelLightViewMatrix = transformation.buildModelLightViewMatrix(modelMatrix, lightViewMatrix);
            chunkShaderProgram.setUniform("modelLightViewMatrix", modelLightViewMatrix);


            renderChunk.render();
        }


        glBindTexture(GL_TEXTURE_2D, 0);

        chunkShaderProgram.unbind();
    }
}
