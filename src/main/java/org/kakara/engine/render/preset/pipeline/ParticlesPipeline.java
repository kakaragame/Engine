package org.kakara.engine.render.preset.pipeline;

import org.joml.Matrix4f;
import org.kakara.engine.exceptions.render.ShaderNotFoundException;
import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.gameitems.Texture;
import org.kakara.engine.gameitems.mesh.Mesh;
import org.kakara.engine.gameitems.particles.ParticleEmitter;
import org.kakara.engine.lighting.ShadowMap;
import org.kakara.engine.render.*;
import org.kakara.engine.render.culling.FrustumCullingFilter;
import org.kakara.engine.scene.Scene;

import java.util.List;
import java.util.Objects;

import static org.lwjgl.opengl.GL13.*;

/**
 * The default render pipeline for the Particle system.
 *
 * @since 1.0-Pre4
 */
public class ParticlesPipeline implements RenderPipeline {

    private Shader particleShaderProgram;
    private ShaderManager manager;
    private Transformation transformation;
    private FrustumCullingFilter frustumFilter;

    @Override
    public void init(ShaderManager manager, Transformation transformation, FrustumCullingFilter frustumFilter, ShadowMap shadowMap) {
        particleShaderProgram = manager.findShader("Particle").getShader();
        this.manager = manager;
        this.transformation = transformation;
        this.frustumFilter = frustumFilter;
    }

    /**
     * Set the shader of the Particle Pipeline.
     *
     * @param shader The name of the shader.
     */
    public void setShader(String shader) {
        ShaderProgram program = manager.findShader(shader);
        if (program == null)
            throw new ShaderNotFoundException("Unable to find shader: " + shader);
        this.particleShaderProgram = program.getShader();
    }

    @Override
    public void render(Scene scene) {
        particleShaderProgram.bind();

        particleShaderProgram.setUniform("texture_sampler", 0);
        Matrix4f projectionMatrix = transformation.getProjectionMatrix();
        particleShaderProgram.setUniform("projectionMatrix", projectionMatrix);

        Matrix4f viewMatrix = scene.getCamera().getViewMatrix();
        List<ParticleEmitter> emitters = Objects.requireNonNull(scene.getParticleHandler()).getParticleEmitters();
        int numEmitters = emitters != null ? emitters.size() : 0;

        glDepthMask(false);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE);

        for (int i = 0; i < numEmitters; i++) {
            ParticleEmitter emitter = emitters.get(i);
            Mesh mesh = (Mesh) emitter.getBaseParticle().getMeshRenderer().get().getMesh();

            Texture text = mesh.getMaterial().get().getTexture();
            particleShaderProgram.setUniform("numCols", text.getNumCols());
            particleShaderProgram.setUniform("numRows", text.getNumRows());

            mesh.renderList((emitter.getParticles()), frustumFilter, (GameItem gameItem) -> {
                        int col = gameItem.getTextPos() % text.getNumCols();
                        int row = gameItem.getTextPos() / text.getNumCols();
                        float textXOffset = (float) col / text.getNumCols();
                        float textYOffset = (float) row / text.getNumRows();
                        particleShaderProgram.setUniform("texXOffset", textXOffset);
                        particleShaderProgram.setUniform("texYOffset", textYOffset);

                        Matrix4f modelMatrix = transformation.buildModelMatrix(gameItem);

                        viewMatrix.transpose3x3(modelMatrix);
                        viewMatrix.scale(gameItem.transform.getScale().toJoml());

                        Matrix4f modelViewMatrix = transformation.buildModelViewMatrix(modelMatrix, viewMatrix);
                        modelViewMatrix.scale(gameItem.transform.getScale().toJoml());
                        particleShaderProgram.setUniform("modelViewMatrix", modelViewMatrix);
                    }
            );
        }

        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glDepthMask(true);

        particleShaderProgram.unbind();
    }


    @Override
    public void renderDepthMap(Scene scene, Shader depthShader, Matrix4f lightViewMatrix) {
    }
}
