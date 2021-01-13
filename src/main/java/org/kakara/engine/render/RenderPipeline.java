package org.kakara.engine.render;

import org.joml.Matrix4f;
import org.kakara.engine.lighting.ShadowMap;
import org.kakara.engine.render.culling.FrustumCullingFilter;
import org.kakara.engine.scene.Scene;

/**
 * Implement a pipeline for the render system.
 *
 * @since 1.0-Pre4
 */
public interface RenderPipeline {
    /**
     * When the renderer is first initialized.
     *
     * @param manager        The shader manager.
     * @param transformation The transformation class.
     * @param frustumFilter  The frustum filter.
     * @param shadowMap      The shadow map.
     */
    void init(ShaderManager manager, Transformation transformation, FrustumCullingFilter frustumFilter, ShadowMap shadowMap);

    /**
     * Render the pipeline.
     *
     * @param scene The scene.
     */
    void render(Scene scene);

    /**
     * Render the depth map.
     * <p>This is called so the engine can calculate shadows. It uses its own shader.</p>
     *
     * @param scene           The scene.
     * @param depthShader     The depth shader.
     * @param lightViewMatrix The light view matrix.
     */
    void renderDepthMap(Scene scene, Shader depthShader, Matrix4f lightViewMatrix);
}
