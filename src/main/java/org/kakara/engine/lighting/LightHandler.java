package org.kakara.engine.lighting;

import org.kakara.engine.math.Vector3;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Handles the lighting for the engine.
 */
public class LightHandler {

    public static final int MAX_POINT_LIGHTS = 5;
    public static final int MAX_SPOT_LIGHTS = 5;

    private final List<PointLight> pointLights;
    private final List<SpotLight> spotLights;
    private DirectionalLight directionalLight;
    private LightColor ambientLight;
    private LightColor skyBoxLight;

    public LightHandler() {
        this.pointLights = new ArrayList<>();
        this.spotLights = new ArrayList<>();
        this.directionalLight = new DirectionalLight(new LightColor(255, 255, 255), new Vector3(0, 1, 0), 0.5f);
        ambientLight = new LightColor(76, 76, 76);
        skyBoxLight = new LightColor(255, 255, 255);
    }

    /**
     * Add a point light
     *
     * @param pl The point light to add
     */
    public void addPointLight(PointLight pl) {
        pointLights.add(pl);
    }

    /**
     * Remove a point light.
     *
     * @param pl The point light to remove.
     */
    public void removePointLight(PointLight pl) {
        pointLights.remove(pl);
    }

    /**
     * Add a spot light
     *
     * @param sl The spot light to add.
     */
    public void addSpotLight(SpotLight sl) {
        spotLights.add(sl);
    }

    /**
     * Remove a spot light
     *
     * @param sl The spot light to remove.
     */
    public void removeSpotLight(SpotLight sl) {
        spotLights.remove(sl);
    }

    /**
     * Gets the list of point lights that are going to be displayed.
     *
     * @return The list of displayed point lights.
     */
    public List<PointLight> getDisplayPointLights() {
        if (pointLights.size() <= MAX_POINT_LIGHTS) {
            return pointLights;
        }
        List<PointLight> lights = new ArrayList<>(pointLights);
        Collections.sort(lights);
        lights.subList(MAX_POINT_LIGHTS, lights.size()).clear();
        return lights;
    }

    /**
     * Gets the list of point lights that are going to be displayed.
     *
     * @return The list of displayed point lights.
     */
    public List<SpotLight> getDisplaySpotLights() {
        if (spotLights.size() <= MAX_SPOT_LIGHTS) {
            return spotLights;
        }
        List<SpotLight> lights = new ArrayList<>(spotLights);
        Collections.sort(lights);
        lights.subList(MAX_SPOT_LIGHTS, lights.size()).clear();
        return lights;
    }

    /**
     * Get a point light by an id.
     *
     * @param id The index of the point light
     * @return The point light
     */
    public PointLight getPointLight(int id) {
        return pointLights.get(id);
    }

    /**
     * Get the list of point lights
     *
     * @return The list of point lights.
     */
    public List<PointLight> getPointLights() {
        return pointLights;
    }

    /**
     * Get a spot light by and id.
     *
     * @param id The id
     * @return The spot light
     */
    public SpotLight getSpotLight(int id) {
        return spotLights.get(id);
    }

    /**
     * Get the list of spot lights
     *
     * @return The list of spot lights.
     */
    public List<SpotLight> getSpotLights() {
        return spotLights;
    }

    /**
     * Get the directional light
     *
     * @return The directional light
     */
    public DirectionalLight getDirectionalLight() {
        return directionalLight;
    }

    /**
     * Set the directional light
     *
     * @param dl THe directional light.
     */
    public void setDirectionalLight(DirectionalLight dl) {
        this.directionalLight = dl;
    }

    /**
     * Get the ambient light color
     * <p>The ambient light is the color of the scene when there are no lights.</p>
     *
     * @return The ambient light color
     */
    public LightColor getAmbientLight() {
        return ambientLight;
    }

    /**
     * Set the ambient light color.
     *
     * @param ambientLight The color
     */
    public void setAmbientLight(LightColor ambientLight) {
        this.ambientLight = ambientLight;
    }

    /**
     * Set the ambient light color
     *
     * @param r Red (0-255)
     * @param g Green (0-255)
     * @param b Blue (0-255)
     */
    public void setAmbientLight(int r, int g, int b) {
        this.ambientLight = new LightColor(r, g, b);
    }

    /**
     * Get the color of the skybox
     *
     * @return The color of the skybox.
     */
    public LightColor getSkyBoxLight() {
        return skyBoxLight;
    }

    /**
     * Set the color of the skybox light.
     *
     * @param skyBoxLight The skybox light.
     */
    public void setSkyBoxLight(LightColor skyBoxLight) {
        this.skyBoxLight = skyBoxLight;
    }

    /**
     * Set the lighting of the skybox.
     *
     * @param r (0-255)
     * @param g (0-255)
     * @param b (0-255)
     */
    public void setSkyBoxLight(int r, int g, int b) {
        this.skyBoxLight = new LightColor(r, g, b);
    }

}
