package org.kakara.engine.render.preset.shader;

import org.kakara.engine.GameEngine;
import org.kakara.engine.lighting.LightHandler;
import org.kakara.engine.render.Shader;
import org.kakara.engine.render.ShaderProgram;
import org.kakara.engine.utils.Utils;

/**
 * The default shader for the GameItem system.
 * <p>This shader follows the standard naming convention.</p>
 *
 * @since 1.0-Pre4
 */
public class StandardShader implements ShaderProgram {

    private Shader shader;

    @Override
    public void initializeShader() {
        try {
            shader = new Shader();
            shader.createVertexShader(Utils.loadResource("/shaders/scene/sceneVertex.vs"));
            shader.createFragmentShader(Utils.loadResource("/shaders/scene/sceneFragment.fs"));
            shader.link();
            shader.createUniform("projectionMatrix");
            shader.createUniform("modelViewNonInstancedMatrix");
            shader.createUniform("modelLightViewNonInstancedMatrix");
            shader.createMaterialUniform("material");
            /*
             * Setup uniforms for lighting
             */

            shader.createFogUniform("fog");
            shader.createUniform("shadowMap");
            shader.createUniform("orthoProjectionMatrix");
            shader.createUniform("isInstanced");

            // light shader.
            shader.createUniform("specularPower");
            shader.createUniform("ambientLight");
            shader.createPointLightListUniform("pointLights", LightHandler.MAX_POINT_LIGHTS);
            shader.createSpotLightListUniform("spotLights", LightHandler.MAX_SPOT_LIGHTS);
            shader.createDirectionalLightUniform("directionalLight");
        } catch (Exception ex) {
            GameEngine.LOGGER.error("An exception has occurred enabling the standard shader.", ex);
        }
    }

    @Override
    public Shader getShader() {
        return shader;
    }
}
