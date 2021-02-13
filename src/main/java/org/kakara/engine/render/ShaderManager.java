package org.kakara.engine.render;

import org.kakara.engine.GameEngine;
import org.kakara.engine.render.preset.shader.ChunkShader;
import org.kakara.engine.render.preset.shader.ParticleShader;
import org.kakara.engine.render.preset.shader.StandardShader;

import java.util.HashMap;
import java.util.Map;

/**
 * Handles shaders within the render system.
 * <p>This includes pre-made shaders: Standard, Chunk, and Particle. You can
 * obtain a pre-made shader though:</p>
 * <code>
 * ShaderProgram standard = shaderProgram.findShader("Standard");
 * </code>
 * <p>The skybox, depthMap (Shadow System), and UI shaders are not apart of this system.</p>
 * <p>Get the instance of this class through {@link GameEngine#getShaderManager()}.</p>
 *
 * @since 1.0-Pre4
 */
public final class ShaderManager {
    private final Map<String, ShaderProgram> programs;
    private boolean initialized;

    /**
     * Internal use only.
     */
    public ShaderManager() {
        programs = new HashMap<>();
        // Define the default shaders.
        programs.put("Standard", new StandardShader());
        programs.put("Chunk", new ChunkShader());
        programs.put("Particle", new ParticleShader());
        initialized = false;
    }

    /**
     * Initialize shaders.
     * <p>Internal use only.</p>
     */
    protected void initShaders() {
        if (initialized) return;
        for (ShaderProgram program : programs.values()) {
            program.initializeShader();
        }
        initialized = true;
    }

    /**
     * Add a shader to the system.
     *
     * @param name    The name of the shader.
     * @param program The program.
     */
    public void addShader(String name, ShaderProgram program) {
        if (this.programs.containsKey(name))
            throw new IllegalArgumentException("The specified name already exists.");
        this.programs.put(name, program);
        if(initialized)
            program.initializeShader();
    }

    /**
     * Find a shader with a specified name.
     *
     * @param name The name of the shader to find.
     * @return The shader. (returns null if not found)
     */
    public ShaderProgram findShader(String name) {
        return programs.get(name);
    }

    /**
     * Cleanup the shader program.
     */
    protected void cleanup() {
        for (ShaderProgram program : programs.values()) {
            program.getShader().cleanup();
        }
    }
}
