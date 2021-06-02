package org.kakara.engine.lighting;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.KMath;
import org.kakara.engine.math.Vector3;

/**
 * A light that is based on a point. It radiates in all directions.
 * <br>
 * <code>
 *     PointLight pointLight = new PointLight(LightColor.RED, new Vector3(), 1);<br>
 *     add(pointLight);
 * </code>
 *
 */
public class PointLight implements Comparable<PointLight> {
    private LightColor color;
    private Vector3 position;
    private float intensity;

    private Attenuation attenuation;

    /**
     * Create a point light.
     *
     * @param color     The color of the light
     * @param position  The position of the light
     * @param intensity The intensity of the light. (0-1)
     */
    public PointLight(LightColor color, Vector3 position, float intensity) {
        attenuation = new Attenuation(1, 0, 0);
        this.color = color;
        this.position = position;
        this.intensity = intensity;
    }

    public PointLight(LightColor color, Vector3 position, float intensity, Attenuation attenuation) {
        this(color, position, intensity);
        this.attenuation = attenuation;
    }

    public PointLight(PointLight pointLight) {
        this(pointLight.getColor(), pointLight.getPosition().clone(),
                pointLight.getIntensity(), pointLight.getAttenuation());
    }

    /**
     * Get the light color of the light
     *
     * @return The color.
     */
    public LightColor getColor() {
        return color;
    }

    /**
     * Set the color of the light.
     *
     * @param color The color of the light.
     */
    public void setColor(LightColor color) {
        this.color = color;
    }

    /**
     * Set the color of the light
     *
     * @param r Red (0-255)
     * @param g Green (0-255)
     * @param b Blue (0-255)
     */
    public void setColor(int r, int g, int b) {
        this.color = new LightColor(r, g, b);
    }

    /**
     * Get the position of the light
     *
     * @return The position
     */
    public Vector3 getPosition() {
        return position;
    }

    /**
     * Set the position of the light
     *
     * @param position The position
     */
    public void setPosition(Vector3 position) {
        this.position = position;
    }

    /**
     * Set the position of the light.
     *
     * @param x X pos
     * @param y Y pos
     * @param z Z pos
     */
    public void setPosition(float x, float y, float z) {
        this.position = new Vector3(x, y, z);
    }

    /**
     * Get the intensity of the light
     *
     * @return The intensity
     */
    public float getIntensity() {
        return intensity;
    }

    /**
     * Set the intensity of the light
     *
     * @param intensity The intensity
     */
    public void setIntensity(float intensity) {
        this.intensity = intensity;
    }

    /**
     * Get the attenuation of the light
     *
     * @return The attenuation
     */
    public Attenuation getAttenuation() {
        return attenuation;
    }

    /**
     * Set the attenuation of the light
     *
     * @param attenuation The attenuation.
     */
    public void setAttenuation(Attenuation attenuation) {
        this.attenuation = attenuation;
    }

    @Override
    public int compareTo(PointLight o) {
        Vector3 cameraPos = GameHandler.getInstance().getCurrentScene().getCamera().getPosition();
        return Math.round(KMath.distance(cameraPos, getPosition()) - KMath.distance(cameraPos, o.getPosition()));
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof PointLight)) return false;
        PointLight other = (PointLight) obj;
        return position.equals(other.position) && color.equals(other.color)
                && attenuation.equals(other.attenuation) && intensity == other.intensity;
    }

    /**
     * Handles the Attenuation information of the light.
     */
    public static class Attenuation {
        private float constant;
        private float linear;
        private float exponent;

        public Attenuation(float constant, float linear, float exponent) {
            this.constant = constant;
            this.linear = linear;
            this.exponent = exponent;
        }

        public float getConstant() {
            return constant;
        }

        public void setConstant(float constant) {
            this.constant = constant;
        }

        public float getLinear() {
            return linear;
        }

        public void setLinear(float linear) {
            this.linear = linear;
        }

        public float getExponent() {
            return exponent;
        }

        public void setExponent(float exponent) {
            this.exponent = exponent;
        }
    }
}
