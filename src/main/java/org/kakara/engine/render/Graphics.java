package org.kakara.engine.render;

import org.joml.Matrix4f;
import org.joml.Vector3f;
import org.joml.Vector4f;
import org.kakara.engine.Camera;
import org.kakara.engine.lighting.DirectionalLight;
import org.kakara.engine.lighting.LightHandler;
import org.kakara.engine.lighting.PointLight;
import org.kakara.engine.lighting.SpotLight;
import org.kakara.engine.math.Vector3;

import java.util.List;

/**
 * This utility class allows easy rendering of basic information and makes up the rendering MAPI.
 * <p>This class assumes that the Standard naming format is used for the shaders. All
 * built-in shaders follow this format.</p>
 *
 * <p>Note: An active game must be going for this class to work.</p>
 *
 * @since 1.0-Pre4
 */
public class Graphics {
    /**
     * Render lights in a scene.
     * <p>The sole purpose of this method is the handle the light rendering, all other uniforms must be set.</p>
     * <p>For most purposes, {@link #renderLights(Camera, LightHandler, Shader)} should be use. Only use this if you need to.</p>
     * @param viewMatrix The view matrix for the camera.
     * @param ambientLight The ambient lighting.
     * @param pointLightList The point lighting list.
     * @param spotLightList The spot light list.
     * @param directionalLight The directional light.
     * @param specularPower The specular light list.
     * @param program The shader program to use.
     *                <p>Must follow the standard shading system.</p>
     */
    public static void renderLights(Matrix4f viewMatrix, Vector3f ambientLight, List<PointLight> pointLightList, List<SpotLight> spotLightList, DirectionalLight directionalLight, float specularPower, Shader program) {
        program.setUniform("ambientLight", ambientLight);
        program.setUniform("specularPower", specularPower);

        // Process Point Lights
        int numLights = pointLightList != null ? pointLightList.size() : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the point light object and transform its position to view coordinates
            PointLight currPointLight = new PointLight(pointLightList.get(i));
            Vector3f lightPos = currPointLight.getPosition().toJoml();
            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;
            currPointLight.setPosition(lightPos.x, lightPos.y, lightPos.z);
            program.setUniform("pointLights", currPointLight, i);
        }

        // Process Spot Ligths
        numLights = spotLightList != null ? spotLightList.size() : 0;
        for (int i = 0; i < numLights; i++) {
            // Get a copy of the spot light object and transform its position and cone direction to view coordinates
            SpotLight currSpotLight = new SpotLight(spotLightList.get(i));
            Vector4f dir = new Vector4f(currSpotLight.getConeDirection(), 0);
            dir.mul(viewMatrix);
            currSpotLight.setConeDirection(new Vector3f(dir.x, dir.y, dir.z));
            Vector3f lightPos = currSpotLight.getPointLight().getPosition().toJoml();

            Vector4f aux = new Vector4f(lightPos, 1);
            aux.mul(viewMatrix);
            lightPos.x = aux.x;
            lightPos.y = aux.y;
            lightPos.z = aux.z;

            program.setUniform("spotLights", currSpotLight, i);
        }

        // Get a copy of the directional light object and transform its position to view coordinates
        DirectionalLight currDirLight = new DirectionalLight(directionalLight);
        Vector4f dir = new Vector4f(currDirLight.getDirection().toJoml(), 0);
        dir.mul(viewMatrix);
        currDirLight.setDirection(new Vector3(dir.x, dir.y, dir.z));
        program.setUniform("directionalLight", currDirLight);
    }

    /**
     * Render lights in a scene.
     * <p>The sole purpose of this method is the handle the light rendering, all other uniforms must be set.</p>
     * @param camera The camera.
     * @param lightHandler The light handler.
     * @param shader The shader.
     */
    public static void renderLights(Camera camera, LightHandler lightHandler, Shader shader){
        renderLights(camera.getViewMatrix(), lightHandler.getAmbientLight().toVector(), lightHandler.getPointLights(),
                lightHandler.getSpotLights(), lightHandler.getDirectionalLight(), 10f, shader);
    }
}
