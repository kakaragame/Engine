package org.kakara.engine.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.kakara.engine.Camera;
import org.kakara.engine.GameEngine;
import org.kakara.engine.components.MeshRenderer;
import org.kakara.engine.gameitems.mesh.IMesh;
import org.kakara.engine.lighting.DirectionalLight;
import org.kakara.engine.lighting.ShadowMap;
import org.kakara.engine.render.culling.FrustumCullingFilter;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.ui.objectcanvas.UIObject;
import org.kakara.engine.utils.Utils;
import org.kakara.engine.voxels.VoxelChunk;
import org.kakara.engine.voxels.mesh.VoxelMesh;
import org.kakara.engine.window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL30.GL_FRAMEBUFFER;
import static org.lwjgl.opengl.GL30.glBindFramebuffer;

/**
 * Handles the rendering pipeline of the game.
 * <p>As of 1.0-pre4, additional pipelines can be added: {@link PipelineManager} and {@link RenderPipeline}</p>
 * <p>Please note that the Skybox, Shadow System, and UI, are not apart of the pipeline system.</p>
 */
public final class Renderer {
    // FOV information.
    private static final float FOV = (float) Math.toRadians(60.0f);
    private static final float Z_NEAR = 0.01f;
    private static final float Z_FAR = 1000.0f;
    private final Transformation transformation;
    private final FrustumCullingFilter frustumFilter;
    private final GameEngine engine;
    private Shader skyBoxShaderProgram;
    private Shader depthShaderProgram;
    private Shader hudShaderProgram;
    private ShadowMap shadowMap;
    public Renderer(GameEngine engine) {
        transformation = new Transformation();
        frustumFilter = new FrustumCullingFilter();
        this.engine = engine;
    }

    /**
     * Setup shaders
     *
     * @throws Exception An exception is thrown when the setup of the shaders fail.
     */
    public void init() throws Exception {
        shadowMap = new ShadowMap();

        engine.getShaderManager().initShaders();

        // Shaders separate from the pipeline system.
        setupSkyBoxShader();
        setupDepthShader();
        setupHudShader();

        for (RenderPipeline pipeline : engine.getPipelineManager().getPipelines()) {
            pipeline.init(engine.getShaderManager(), transformation, frustumFilter, shadowMap);
        }
    }

    /**
     * Render the game.
     *
     * @param window The window
     * @param camera The camera
     * @param scene  The scene
     */
    public void render(Window window, Camera camera, Scene scene) {
        clear();

        // Render the depth map (which is separate from the pipeline system.)
        renderDepthMap(window, camera, scene);

        glViewport(0, 0, window.getWidth(), window.getHeight());

        transformation.updateProjectionMatrix(FOV, window.getWidth(), window.getHeight(), Z_NEAR, Z_FAR);
        scene.getCamera().updateViewMatrix();

        frustumFilter.updateFrustum(transformation.getProjectionMatrix(), camera.getViewMatrix());

        // Render the pipelines.
        for (RenderPipeline pipeline : engine.getPipelineManager().getPipelines()) {
            pipeline.render(scene);
        }
    }

    /**
     * Render the 3D portion of the HUD.
     * <p>See {@link org.kakara.engine.ui.objectcanvas.UIObject} and {@link org.kakara.engine.ui.canvases.ObjectCanvas} for more info.</p>
     *
     * @param window  The window of the current game.
     * @param objects The list of objects.
     * @param isAuto  If the engine will scale the objects automatically.
     */
    public void renderHUD(Window window, List<UIObject> objects, boolean isAuto) {
        hudShaderProgram.bind();
        int width = isAuto ? window.initialWidth : window.getWidth();
        int height = isAuto ? window.initialHeight : window.getHeight();
        Matrix4f orthoProjection = transformation.buildOrtho(0, width, height, 0);
        for (UIObject object : objects) {
            hudShaderProgram.setUniform("ortho", orthoProjection);
            hudShaderProgram.setUniform("model", transformation.buildModelViewMatrixUI(object));
            for (IMesh mesh : object.getMeshes()) {
                mesh.render();
            }
        }
        hudShaderProgram.unbind();
    }

    /**
     * Test the queries to see if they are culled.
     *
     * @param chunks             Render Chunks.
     * @param chunkShaderProgram The ShaderProgram for the chunk
     * @param viewMatrix         The view matrix.
     * @param lightViewMatrix    The lightViewMatrix
     * @deprecated Unused
     */
    private void doOcclusionTest(List<VoxelChunk> chunks, Shader chunkShaderProgram, Matrix4f viewMatrix, Matrix4f lightViewMatrix) {
        if (chunks == null || chunks.isEmpty() || chunks.get(0).getVoxelMesh() == null) return;
        if (chunks.get(0).getVoxelMesh().getQuery() == null) return;
        glColorMask(false, false, false, false);
        glDepthMask(false);
        for (VoxelChunk chunk : new ArrayList<>(chunks)) {
            // If the chunk is out of the frustum then don't bother testing.
            if (!frustumFilter.testRenderObject(chunk.transform.getPosition(), 16, 16, 16))
                continue;
            VoxelMesh mesh = chunk.getVoxelMesh();
            if (mesh == null) continue;
            if (mesh.getQuery() != null) {
                // Calculate the Matrix for the chunk so it is tested in the right spot
                Matrix4f modelMatrix = transformation.buildModelMatrix(chunk);
                Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
                chunkShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                Matrix4f modelLightViewMatrix = transformation.buildModelLightViewMatrix(modelMatrix, lightViewMatrix);
                chunkShaderProgram.setUniform("modelLightViewMatrix", modelLightViewMatrix);

                // Do the query
                mesh.getQuery().start();
                mesh.render();
                mesh.getQuery().end();
            }
        }
        glColorMask(true, true, true, true);
        glDepthMask(true);
    }

    /**
     * Render the depth map.
     *
     * @param window The window.
     * @param camera The camera.
     * @param scene  The scene.
     */
    private void renderDepthMap(Window window, Camera camera, Scene scene) {
        glBindFramebuffer(GL_FRAMEBUFFER, shadowMap.getDepthMapFBO());
        glViewport(0, 0, ShadowMap.SHADOW_MAP_WIDTH, ShadowMap.SHADOW_MAP_HEIGHT);
        glClear(GL_DEPTH_BUFFER_BIT);

        depthShaderProgram.bind();

        DirectionalLight light = Objects.requireNonNull(scene.getLightHandler()).getDirectionalLight();
        Vector3f lightDirection = light.getDirection().toJoml();

        float lightAngleX = (float) Math.toDegrees(Math.acos(lightDirection.z));
        float lightAngleY = (float) Math.toDegrees(Math.asin(lightDirection.x));
        float lightAngleZ = 0;
        Matrix4f lightViewMatrix = transformation.updateLightViewMatrix(new Vector3f(lightDirection).mul(light.getShadowPosMult()), new Vector3f(lightAngleX, lightAngleY, lightAngleZ));
        DirectionalLight.OrthoCoords orthCoords = light.getOrthoCoords();
        Matrix4f orthoProjMatrix = transformation.updateOrthoProjectionMatrix(orthCoords.left, orthCoords.right, orthCoords.bottom, orthCoords.top, orthCoords.near, orthCoords.far);
        depthShaderProgram.setUniform("orthoProjectionMatrix", orthoProjMatrix);

        for (RenderPipeline pipeline : engine.getPipelineManager().getPipelines()) {
            pipeline.renderDepthMap(scene, depthShaderProgram, lightViewMatrix);
        }

        // Unbind
        depthShaderProgram.unbind();
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }


    /**
     * Render the skybox
     *
     * @param window The window
     * @param camera The game
     * @param scene  The current scene
     */
    public void renderSkyBox(Window window, Camera camera, Scene scene) {
        skyBoxShaderProgram.bind();
        skyBoxShaderProgram.setUniform("texture_sampler", 0);
        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        skyBoxShaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = scene.getCamera().getViewMatrix();
        viewMatrix.m30(0);
        viewMatrix.m31(0);
        viewMatrix.m32(0);
        //TODO remove model view matrix
        Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(scene.getSkyBox(), viewMatrix);
        skyBoxShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
        skyBoxShaderProgram.setUniform("ambientLight",
                Objects.requireNonNull(scene.getLightHandler()).getSkyBoxLight().toVector());
        scene.getSkyBox().getComponent(MeshRenderer.class).getMesh().render();

        skyBoxShaderProgram.unbind();
    }

    /**
     * Setup the hud shader.
     *
     * @throws Exception The hud shader.
     */
    private void setupHudShader() throws Exception {
        hudShaderProgram = new Shader();
        hudShaderProgram.createVertexShader(Utils.loadResource("/shaders/hud/hudVertex.vs"));
        hudShaderProgram.createFragmentShader(Utils.loadResource("/shaders/hud/hudFragment.fs"));
        hudShaderProgram.link();

        // Create uniforms for Orthographic-model projection matrix and base colour
        hudShaderProgram.createUniform("model");
        hudShaderProgram.createUniform("ortho");
    }

    /**
     * Setup the skybox shader.
     *
     * @throws Exception
     */
    private void setupSkyBoxShader() throws Exception {
        skyBoxShaderProgram = new Shader();
        skyBoxShaderProgram.createVertexShader(Utils.loadResource("/shaders/scene/skyboxVertex.vs"));
        skyBoxShaderProgram.createFragmentShader(Utils.loadResource("/shaders/scene/skyboxFragment.fs"));
        skyBoxShaderProgram.link();

        skyBoxShaderProgram.createUniform("projectionMatrix");
        skyBoxShaderProgram.createUniform("modelViewMatrix");
        skyBoxShaderProgram.createUniform("texture_sampler");
        skyBoxShaderProgram.createUniform("ambientLight");
    }


    /**
     * Setup the depth shader.
     *
     * @throws Exception
     */
    private void setupDepthShader() throws Exception {
        depthShaderProgram = new Shader();
        depthShaderProgram.createVertexShader(Utils.loadResource("/shaders/depth/depthVertex.vs"));
        depthShaderProgram.createFragmentShader(Utils.loadResource("/shaders/depth/depthFragment.fs"));
        depthShaderProgram.link();

        depthShaderProgram.createUniform("isInstanced");
        depthShaderProgram.createUniform("modelLightViewNonInstancedMatrix");
        depthShaderProgram.createUniform("orthoProjectionMatrix");
    }


    /**
     * Clear the screen.
     */
    public void clear() {
        glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT | GL_STENCIL_BUFFER_BIT);
    }

    /**
     * cleanup the renderer.
     */
    public void cleanup() {
        if (skyBoxShaderProgram != null) {
            skyBoxShaderProgram.cleanup();
        }
        if (depthShaderProgram != null) {
            depthShaderProgram.cleanup();
        }

        engine.getShaderManager().cleanup();
        shadowMap.cleanup();
    }

    /**
     * Get the transformation.
     *
     * @return The transformation.
     */
    public Transformation getTransformation() {
        return transformation;
    }

    /**
     * Get the FrustumCullingFilter for the Renderer.
     *
     * @return The Frustum Culling Filter.
     */
    public FrustumCullingFilter getFrustumFilter() {
        return frustumFilter;
    }
}
