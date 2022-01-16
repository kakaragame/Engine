package org.kakara.engine.weather;

import org.kakara.engine.utils.RGBA;

/**
 * This class handles the fog.
 */
public class Fog {

    /**
     * Using Fog.NOFOG will result in no fog.
     */
    public static Fog NOFOG = new Fog();

    private boolean active;
    private RGBA color;
    private float density;

    /**
     * The default constructor for Fog.
     *
     * <p>This is the same as {@link Fog#NOFOG}.</p>
     */
    public Fog() {
        active = false;
        this.color = new RGBA(0, 0, 0);
        this.density = 0;
    }

    /**
     * Create some fog.
     *
     * @param active  If the fog is active.
     * @param color   The color of the fog.
     * @param density The density of the fog.
     */
    public Fog(boolean active, RGBA color, float density) {
        this.color = color;
        this.density = density;
        this.active = active;
    }

    /**
     * Get the color of the fog
     *
     * @return The color of the fog.
     */
    public RGBA getColor() {
        return color;
    }

    /**
     * Set the color of the fog
     *
     * @param color The color of the fog.
     */
    public void setColor(RGBA color) {
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
