package org.kakara.engine.render;

/**
 * Implement a custom shader.
 *
 * @since 1.0-Pre4
 */
public interface ShaderProgram {
    /**
     * Initialize a shader.
     */
    void initializeShader();

    /**
     * Get the shader from the shader program.
     *
     * @return The shader.
     */
    Shader getShader();
}
