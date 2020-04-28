package org.kakara.engine.lighting;

import org.kakara.engine.math.Vector3;

/**
 * Handles Lighting that is similar to the sun.
 */
public class DirectionalLight {
    private LightColor color;
    private Vector3 direction;
    private float intensity;
    private OrthoCoords orthoCords;
    private float shadowPosMult;

    /**
     * Create a directional light.
     * @param color The color of the directional light (0-1 for all values).
     * @param direction The direction of the light. (0, 1, 0) is down.
     * @param intensity The intensity of the light (0-1).
     */
    public DirectionalLight(LightColor color, Vector3 direction, float intensity) {
        this.color = color;
        this.direction = direction;
        this.intensity = intensity;
        this.orthoCords = new OrthoCoords();
        this.shadowPosMult = 1;
        setOrthoCords(-10.0f, 10.0f, -10.0f, 10.0f, -1.0f, 20.0f);
    }

    /**
     * Clone the direction light
     * @param light The light to make the clone of.
     */
    public DirectionalLight(DirectionalLight light) {
        this(light.getColor(), light.getDirection().clone(), light.getIntensity());
    }

    /**
     * Get the color of the light.
     * @return The color of the light.
     */
    public LightColor getColor() {
        return color;
    }

    /**
     * Set the color of the light
     * @param color The color of the light
     */
    public void setColor(LightColor color) {
        this.color = color;
    }

    /**
     * Set the color of the light
     * @param r Red (0-255)
     * @param g Green (0-255)
     * @param b Blue (0-255)
     */
    public void setColor(int r, int g, int b){
        this.color = new LightColor(r, g, b);
    }

    /**
     * Get the direction of the light.
     * @return A *clone* of the direction.
     */
    public Vector3 getDirection() {
        return direction.clone();
    }

    /**
     * Set the direction of the light.
     * @param direction The direction
     */
    public void setDirection(Vector3 direction) {
        this.direction = direction;
    }

    /**
     * Set the direction of the light
     * @param x x param
     * @param y y param
     * @param z z param
     */
    public void setDirection(float x, float y, float z){
        this.direction = new Vector3(x, y, z);
    }

    /**
     * Get the intensity of the directional light
     * @return The intensity
     */
    public float getIntensity() {
        return intensity;
    }

    /**
     * Set the intensity of the light
     * @param intensity The intensity.
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    public float getShadowPosMult() {
        return shadowPosMult;
    }

    public void setShadowPosMult(float shadowPosMult) {
        this.shadowPosMult = shadowPosMult;
    }

    public OrthoCoords getOrthoCoords(){
        return orthoCords;
    }

    public void setOrthoCords(float left, float right, float bottom, float top, float near, float far) {
        orthoCords.left = left;
        orthoCords.right = right;
        orthoCords.bottom = bottom;
        orthoCords.top = top;
        orthoCords.near = near;
        orthoCords.far = far;
    }

    public static class OrthoCoords {

        public float left;

        public float right;

        public float bottom;

        public float top;

        public float near;

        public float far;
    }
}
