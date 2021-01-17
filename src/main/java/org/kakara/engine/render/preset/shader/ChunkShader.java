package org.kakara.engine.render.preset.shader;

import org.kakara.engine.GameEngine;
import org.kakara.engine.lighting.LightHandler;
import org.kakara.engine.render.Shader;
import org.kakara.engine.render.ShaderProgram;
import org.kakara.engine.utils.Utils;

/**
 * The default shader for the RenderChunk system.
 * <p>This shader follows the standard naming convention.</p>
 *
 * @since 1.0-Pre4
 */
public class ChunkShader implements ShaderProgram {
    private Shader shader;

    @Override
    public void initializeShader() {
        try{
            shader = new Shader();
            shader.createVertexShader(Utils.loadResource("/shaders/chunk/chunkVertex.vs"));
            shader.createFragmentShader(Utils.loadResource("/shaders/chunk/chunkFragment.fs"));
            shader.link();

            shader.createUniform("projectionMatrix");
            shader.createUniform("modelViewMatrix");
            shader.createUniform("modelLightViewMatrix");
            /*
             * Setup uniforms for lighting
             */
            shader.createFogUniform("fog");
            shader.createUniform("shadowMap");
            shader.createUniform("orthoProjectionMatrix");
            shader.createUniform("specularPower");
            shader.createUniform("ambientLight");
            shader.createPointLightListUniform("pointLights", LightHandler.MAX_POINT_LIGHTS);
            shader.createSpotLightListUniform("spotLights", LightHandler.MAX_SPOT_LIGHTS);
            shader.createDirectionalLightUniform("directionalLight");
            shader.createUniform("reflectance");
            // Texture Atlas
            shader.createUniform("textureAtlas");
        }catch (Exception ex){
            GameEngine.LOGGER.error("Unable to initialize chunk shader", ex);
        }
    }

    @Override
    public Shader getShader() {
        return shader;
    }
}
