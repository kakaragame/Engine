package org.kakara.engine.render.preset.shader;

import org.kakara.engine.GameEngine;
import org.kakara.engine.render.Shader;
import org.kakara.engine.render.ShaderProgram;
import org.kakara.engine.utils.Utils;

/**
 * The default shader for the Particle system.
 * <p>This shader follows the standard naming convention.</p>
 *
 * @since 1.0-Pre4
 */
public class ParticleShader implements ShaderProgram {
    private Shader shader;

    @Override
    public void initializeShader() {
        try {
            shader = new Shader();
            shader.createVertexShader(Utils.loadResource("/shaders/particle/particleVertex.vs"));
            shader.createFragmentShader(Utils.loadResource("/shaders/particle/particleFragment.fs"));
            shader.link();

            shader.createUniform("projectionMatrix");
            shader.createUniform("modelViewMatrix");
            shader.createUniform("texture_sampler");

            shader.createUniform("texXOffset");
            shader.createUniform("texYOffset");
            shader.createUniform("numCols");
            shader.createUniform("numRows");
        } catch (Exception ex) {
            GameEngine.LOGGER.error("Unable to initialize particle shader", ex);
        }
    }

    @Override
    public Shader getShader() {
        return shader;
    }
}
