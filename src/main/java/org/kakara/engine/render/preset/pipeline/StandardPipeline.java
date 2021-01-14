package org.kakara.engine.render.preset.pipeline;

import org.joml.Matrix4f;
import org.kakara.engine.GameHandler;
import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.gameitems.MeshGameItem;
import org.kakara.engine.gameitems.mesh.IMesh;
import org.kakara.engine.gameitems.mesh.InstancedMesh;
import org.kakara.engine.lighting.LightHandler;
import org.kakara.engine.lighting.ShadowMap;
import org.kakara.engine.render.*;
import org.kakara.engine.render.culling.FrustumCullingFilter;
import org.kakara.engine.scene.Scene;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.GL_TEXTURE_2D;
import static org.lwjgl.opengl.GL11.glBindTexture;
import static org.lwjgl.opengl.GL13.GL_TEXTURE2;
import static org.lwjgl.opengl.GL13.glActiveTexture;

/**
 * The default render pipeline for the GameItem system.
 *
 * @since 1.0-Pre4
 */
public class StandardPipeline implements RenderPipeline {

    private Shader shaderProgram;
    private Transformation transformation;
    private FrustumCullingFilter frustumFilter;
    private ShadowMap shadowMap;

    @Override
    public void init(ShaderManager manager, Transformation transformation, FrustumCullingFilter frustumFilter, ShadowMap shadowMap) {
        shaderProgram = manager.findShader("Standard").getShader();
        this.transformation = transformation;
        this.frustumFilter = frustumFilter;
        this.shadowMap = shadowMap;
    }

    @Override
    public void render(Scene scene) {
        shaderProgram.bind();
        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        shaderProgram.setUniform("projectionMatrix", projectionMatrix);
        Matrix4f orthoProjMatrix = transformation.getOrthoProjectionMatrix();
        shaderProgram.setUniform("orthoProjectionMatrix", orthoProjMatrix);
        Matrix4f lightViewMatrix = transformation.getLightViewMatrix();

        Matrix4f viewMatrix = scene.getCamera().getViewMatrix();

        // Render Lighting
        LightHandler lh = GameHandler.getInstance().getSceneManager().getCurrentScene().getLightHandler();
        assert lh != null;

        Graphics.renderLights(scene.getCamera(), lh, shaderProgram);

        shaderProgram.setUniform("fog", scene.getFog());
        shaderProgram.setUniform("shadowMap", 2);

        renderNonInstancedMeshes(scene, false, shaderProgram, viewMatrix, lightViewMatrix);

        renderInstancedMeshes(scene, false, shaderProgram, viewMatrix, lightViewMatrix);

        shaderProgram.unbind();
    }

    /**
     * Render all of the non instanced meshes
     *
     * @param scene           The scene
     * @param depthMap        If the depthmap is being rendered
     * @param shader          The shader
     * @param viewMatrix      The view matrix
     * @param lightViewMatrix The light view matrix.
     */
    private void renderNonInstancedMeshes(Scene scene, boolean depthMap, Shader shader, Matrix4f viewMatrix, Matrix4f lightViewMatrix) {
        shaderProgram.setUniform("isInstanced", 0);

        // Render each mesh with the associated game Items
        Map<IMesh, List<GameItem>> mapMeshes = Objects.requireNonNull(scene.getItemHandler()).getNonInstancedMeshMap();
        for (IMesh mesh : mapMeshes.keySet()) {
            if (!depthMap) {
                if (mesh.getMaterial().isPresent())
                    shader.setUniform("material", mesh.getMaterial().get());
                glActiveTexture(GL_TEXTURE2);
                glBindTexture(GL_TEXTURE_2D, shadowMap.getDepthMapTexture().getId());
            }

            mesh.renderList(mapMeshes.get(mesh), frustumFilter, (GameItem gameItem) -> {
                MeshGameItem meshGameItem = ((MeshGameItem) gameItem);
                Matrix4f modelMatrix = transformation.buildModelMatrix(gameItem);
                if (!depthMap) {
                    Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
                    shaderProgram.setUniform("modelViewNonInstancedMatrix", modelViewMatrix);
                }
                Matrix4f modelLightViewMatrix = transformation.buildModelLightViewMatrix(modelMatrix, lightViewMatrix);
                shaderProgram.setUniform("modelLightViewNonInstancedMatrix", modelLightViewMatrix);
                // Render every mesh (some game items can have more than one)
                for (IMesh m : meshGameItem.getMeshes()) {
                    m.render();
                }
            });
        }
    }

    /**
     * Render instanced meshes.
     *
     * @param scene           The scene
     * @param depthMap        If the depth map is being rendered
     * @param shader          The shader
     * @param viewMatrix      The view matrix
     * @param lightViewMatrix The light view matrix.
     */
    private void renderInstancedMeshes(Scene scene, boolean depthMap, Shader shader, Matrix4f viewMatrix, Matrix4f lightViewMatrix) {
        shaderProgram.setUniform("isInstanced", 1);

        // Render each mesh with the associated game Items
        Map<InstancedMesh, List<GameItem>> mapMeshes = Objects.requireNonNull(scene.getItemHandler()).getInstancedMeshMap();
        for (InstancedMesh mesh : mapMeshes.keySet()) {
            if (!depthMap) {
                shaderProgram.setUniform("material", mesh.getMaterial().get());
                glActiveTexture(GL_TEXTURE2);
                glBindTexture(GL_TEXTURE_2D, shadowMap.getDepthMapTexture().getId());
            }
            mesh.renderListInstanced(mapMeshes.get(mesh), depthMap, transformation, viewMatrix, lightViewMatrix);
        }
    }

    @Override
    public void renderDepthMap(Scene scene, Shader depthMap, Matrix4f lightViewMatrix) {

        renderNonInstancedMeshes(scene, true, depthMap, null, lightViewMatrix);

        renderInstancedMeshes(scene, true, depthMap, null, lightViewMatrix);

    }
}
