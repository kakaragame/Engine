package org.kakara.engine.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.kakara.engine.gameitems.Material;
import org.kakara.engine.lighting.DirectionalLight;
import org.kakara.engine.lighting.PointLight;
import org.kakara.engine.lighting.SpotLight;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.weather.Fog;
import org.lwjgl.system.MemoryStack;

import java.nio.FloatBuffer;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.lwjgl.opengl.GL20.*;

/**
 * Create custom shader programs.
 */
public class Shader {
    private final int programId;
    private final Map<String, Integer> uniforms;
    private int vertexShaderId;
    private int fragmentShaderId;

    /**
     * Create a new shader.
     *
     * @throws Exception
     */
    public Shader() throws Exception {
        programId = glCreateProgram();
        uniforms = new HashMap<>();
        if (programId == 0) {
            throw new Exception("Could not create Shader");
        }
    }

    /**
     * Create a vertex shader.
     *
     * @param shaderCode The shader code.
     * @throws Exception
     */
    public void createVertexShader(String shaderCode) throws Exception {
        vertexShaderId = createShader(shaderCode, GL_VERTEX_SHADER);
    }

    /**
     * Create a fragment shader.
     *
     * @param shaderCode The shader code.
     * @throws Exception
     */
    public void createFragmentShader(String shaderCode) throws Exception {
        fragmentShaderId = createShader(shaderCode, GL_FRAGMENT_SHADER);
    }

    /**
     * Create a shader
     * <p>Same as doing {@link #createVertexShader(String)} and {@link #createFragmentShader(String)}.</p>
     *
     * @param shaderCode The shader code
     * @param shaderType The shader type
     * @return The shader id.
     * @throws Exception
     */
    protected int createShader(String shaderCode, int shaderType) throws Exception {
        int shaderId = glCreateShader(shaderType);
        if (shaderId == 0) {
            throw new Exception("Error creating shader. Type: " + shaderType);
        }

        glShaderSource(shaderId, shaderCode);
        glCompileShader(shaderId);

        if (glGetShaderi(shaderId, GL_COMPILE_STATUS) == 0) {
            throw new Exception("Error compiling Shader code: " + glGetShaderInfoLog(shaderId, 1024));
        }

        glAttachShader(programId, shaderId);

        return shaderId;
    }

    /**
     * Link the shader to OpenGL
     *
     * @throws Exception
     */
    public void link() throws Exception {
        glLinkProgram(programId);
        if (glGetProgrami(programId, GL_LINK_STATUS) == 0) {
            throw new Exception("Error linking Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

        if (vertexShaderId != 0) {
            glDetachShader(programId, vertexShaderId);
        }
        if (fragmentShaderId != 0) {
            glDetachShader(programId, fragmentShaderId);
        }

        glValidateProgram(programId);
        if (glGetProgrami(programId, GL_VALIDATE_STATUS) == 0) {
            System.err.println("Warning validating Shader code: " + glGetProgramInfoLog(programId, 1024));
        }

    }

    /**
     * Create a new uniform
     *
     * @param uniformName The uniform name
     * @throws Exception If the uniform could not be found.
     */
    public void createUniform(String uniformName) throws Exception {
        int uniformLocation = glGetUniformLocation(programId, uniformName);
        if (uniformLocation < 0) {
            throw new Exception("Could not find uniform:" + uniformName);
        }
        uniforms.put(uniformName, uniformLocation);
    }

    /**
     * set a Matrix4f uniform
     *
     * @param uniformName The uniform name
     * @param value       The value
     */
    public void setUniform(String uniformName, Matrix4f value) {
        try (MemoryStack stack = MemoryStack.stackPush()) {
            FloatBuffer fb = stack.mallocFloat(16);
            value.get(fb);
            glUniformMatrix4fv(uniforms.get(uniformName), false, fb);
        }
    }

    /**
     * Set an int uniform
     *
     * @param uniformName The uniform name
     * @param value       The uniform value.
     */
    public void setUniform(String uniformName, int value) {
        glUniform1i(uniforms.get(uniformName), value);
    }

    /*
     *
     * Brand new lighting junk
     */

    /**
     * Set a float uniform
     *
     * @param uniformName The uniform name
     * @param value       The value
     */
    public void setUniform(String uniformName, float value) {
        glUniform1f(uniforms.get(uniformName), value);
    }

    /**
     * Set a vector3f uniform
     *
     * @param uniformName The uniform name
     * @param value       The uniform value.
     */
    public void setUniform(String uniformName, Vector3f value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    /**
     * Set a vector3 uniform
     *
     * @param uniformName The uniform name
     * @param value       The uniform value
     * @since 1.0-pre1
     */
    public void setUniform(String uniformName, Vector3 value) {
        glUniform3f(uniforms.get(uniformName), value.x, value.y, value.z);
    }

    /**
     * Set a vector4f uniform
     *
     * @param uniformName The uniform name
     * @param value       The uniform value.
     */
    public void setUniform(String uniformName, Vector4f value) {
        glUniform4f(uniforms.get(uniformName), value.x, value.y, value.z, value.w);
    }

    /**
     * Create the point light uniform
     *
     * @param uniformName The uniform name
     * @param size        The amount of point lights allowed.
     * @throws Exception
     */
    public void createPointLightListUniform(String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            createPointLightUniform(uniformName + "[" + i + "]");
        }
    }

    /**
     * Creates a point light uniform
     *
     * @param uniformName The uniform name.
     * @throws Exception
     */
    public void createPointLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".position");
        createUniform(uniformName + ".intensity");
        createUniform(uniformName + ".att.constant");
        createUniform(uniformName + ".att.linear");
        createUniform(uniformName + ".att.exponent");
    }

    /**
     * Creates a spot light list uniform
     *
     * @param uniformName The uniform name
     * @param size        The amount of spot lights allowed.
     * @throws Exception
     */
    public void createSpotLightListUniform(String uniformName, int size) throws Exception {
        for (int i = 0; i < size; i++) {
            createSpotLightUniform(uniformName + "[" + i + "]");
        }
    }

    /**
     * Create spot light uniforms
     *
     * @param uniformName The uniform name
     * @throws Exception
     */
    public void createSpotLightUniform(String uniformName) throws Exception {
        createPointLightUniform(uniformName + ".pl");
        createUniform(uniformName + ".conedir");
        createUniform(uniformName + ".cutoff");
    }

    /**
     * Create a directional light uniform
     *
     * @param uniformName The uniform name
     * @throws Exception
     */
    public void createDirectionalLightUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".direction");
        createUniform(uniformName + ".intensity");
    }

    /**
     * Create a material uniform
     *
     * @param uniformName The uniform name.
     * @throws Exception
     */
    public void createMaterialUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".ambient");
        createUniform(uniformName + ".diffuse");
        createUniform(uniformName + ".specular");
        createUniform(uniformName + ".hasTexture");
        createUniform(uniformName + ".reflectance");

        for (int i = 0; i < 5; i++)
            createUniform(uniformName + ".overlayTextures[" + i + "]");
        createUniform(uniformName + ".numberOfOverlays");
    }
    /*
        Lighting Set Uniform
     */

    /**
     * Set the point light uniform
     *
     * @param uniformName The uniform name
     * @param pointLights The list of point lights.
     */
    public void setPointLightsUniform(String uniformName, List<PointLight> pointLights) {
        int numLights = pointLights != null ? pointLights.size() : 0;
        for (int i = 0; i < numLights; i++) {
            setUniform(uniformName, pointLights.get(i), i);
        }
    }

    /**
     * Set the point light list at a certain position.
     *
     * @param uniformName The uniform name
     * @param spotLight   The point light.
     * @param pos         The position to insert at.
     */
    public void setUniform(String uniformName, PointLight spotLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }

    /**
     * Set the point light uniform
     *
     * @param uniformName The uniform name
     * @param pointLight  The point light.
     */
    public void setUniform(String uniformName, PointLight pointLight) {
        setUniform(uniformName + ".color", pointLight.getColor().toVector());
        setUniform(uniformName + ".position", pointLight.getPosition());
        setUniform(uniformName + ".intensity", pointLight.getIntensity());
        PointLight.Attenuation att = pointLight.getAttenuation();
        setUniform(uniformName + ".att.constant", att.getConstant());
        setUniform(uniformName + ".att.linear", att.getLinear());
        setUniform(uniformName + ".att.exponent", att.getExponent());
    }

    /**
     * Set the material uniform
     *
     * @param uniformName The uniform name
     * @param material    The material uniform.
     */
    public void setUniform(String uniformName, Material material) {
        setUniform(uniformName + ".ambient", material.getAmbientColor().getVectorColor());
        setUniform(uniformName + ".diffuse", material.getDiffuseColor().getVectorColor());
        setUniform(uniformName + ".specular", material.getSpecularColor().getVectorColor());
        setUniform(uniformName + ".hasTexture", material.isTextured() ? 1 : 0);
        setUniform(uniformName + ".reflectance", material.getReflectance());

        for (int i = 0; i < 5; i++)
            setUniform(uniformName + ".overlayTextures[" + i + "]", i + 3);
        setUniform(uniformName + ".numberOfOverlays", material.getOverlayTextures().size());
    }

    /**
     * Set the directional light uniform
     *
     * @param uniformName The uniform name
     * @param dirLight    The directional light uniform.
     */
    public void setUniform(String uniformName, DirectionalLight dirLight) {
        setUniform(uniformName + ".color", dirLight.getColor().toVector());
        setUniform(uniformName + ".direction", dirLight.getDirection());
        setUniform(uniformName + ".intensity", dirLight.getIntensity());
    }

    /**
     * Set the list of spot lights uniform
     *
     * @param uniformName The uniform name
     * @param spotLights  The list of spot lights.
     */
    public void setUniform(String uniformName, List<SpotLight> spotLights) {
        int numLights = spotLights != null ? spotLights.size() : 0;
        for (int i = 0; i < numLights; i++) {
            setUniform(uniformName, spotLights.get(i), i);
        }
    }

    /**
     * Set the spot light at a position.
     *
     * @param uniformName The uniform name for the spot light list.
     * @param spotLight   The spot light
     * @param pos         The position value.
     */
    public void setUniform(String uniformName, SpotLight spotLight, int pos) {
        setUniform(uniformName + "[" + pos + "]", spotLight);
    }

    /**
     * Set the spotlight uniform
     *
     * @param uniformName The uniform name.
     * @param spotLight   The spot light
     */
    public void setUniform(String uniformName, SpotLight spotLight) {
        setUniform(uniformName + ".pl", spotLight.getPointLight());
        setUniform(uniformName + ".conedir", spotLight.getConeDirection());
        setUniform(uniformName + ".cutoff", spotLight.getCutOff());
    }

    /**
     * Create a new fog uniform.
     *
     * @param uniformName The uniform name
     * @throws Exception
     */
    public void createFogUniform(String uniformName) throws Exception {
        createUniform(uniformName + ".activeFog");
        createUniform(uniformName + ".color");
        createUniform(uniformName + ".density");
    }

    /**
     * Set the fog uniform
     *
     * @param uniformName The uniform name
     * @param fog         The fog.
     */
    public void setUniform(String uniformName, Fog fog) {
        setUniform(uniformName + ".activeFog", fog.isActive() ? 1 : 0);
        setUniform(uniformName + ".color", fog.getColor());
        setUniform(uniformName + ".density", fog.getDensity());
    }

    /**
     * Bind the shader program
     */
    public void bind() {
        glUseProgram(programId);
    }

    /**
     * Unbind the shader program
     */
    public void unbind() {
        glUseProgram(0);
    }

    /**
     * Cleanup the shader.
     */
    public void cleanup() {
        unbind();
        if (programId != 0) {
            glDeleteProgram(programId);
        }
    }
}
