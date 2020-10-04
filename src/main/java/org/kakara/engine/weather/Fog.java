package org.kakara.engine.weather;

import org.kakara.engine.math.Vector3;

/**
 * This class handles the fog.
 */
public class Fog {

    /**
     * Used if you want to fog.
     */
    public static Fog NOFOG = new Fog();
    private boolean active;
    private Vector3 color;
    private float density;

    /**
     * Used if you want no fog.
     */
    public Fog() {
        active = false;
        this.color = new Vector3(0, 0, 0);
        this.density = 0;
    }

    /**
     * Create some fog
     *
     * @param active  If the fog is active
     * @param color   The color of the fog
     * @param density The density of the fog.
     */
    public Fog(boolean active, Vector3 color, float density) {
        this.color = color;
        this.density = density;
        this.active = active;
    }

    /**
     * Get the color of the fog
     *
     * @return The color of the fog.
     */
    public Vector3 getColor() {
        return color;
    }

    /**
     * Set the color of the fog
     *
     * @param color The color of the fog.
     */
    public void setColor(Vector3 color) {
        this.color = color;
    }

    /**
     * Get the density of the fog
     *
     * @return The density
     */
    public float getDensity() {
        return density;
    }

    /**
     * Set the density of the fog
     *
     * @param density The density
     */
    public void setDensity(float density) {
        this.density = density;
    }

    /**
     * Check if the fog is active
     *
     * @return If the fog is active.
     */
    public boolean isActive() {
        return active;
    }

    /**
     * Set if the fog is active
     *
     * @param active If the fog is active
     */
    public void setActive(boolean active) {
        this.active = active;
    }
}
