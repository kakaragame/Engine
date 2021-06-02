package org.kakara.engine.lighting;

import org.joml.Vector3f;

/**
 * The color for lights in the Kakara Engine.
 *
 * <p>There are many predefined light colors, like {@link #RED}.</p>
 */
public class LightColor {

    public static final LightColor RED = new LightColor(255, 0, 0);
    public static final LightColor GREEN = new LightColor(0, 255, 0);
    public static final LightColor BLUE = new LightColor(0, 0, 255);
    public static final LightColor PURPLE = new LightColor(128, 0, 128);
    public static final LightColor PINK = new LightColor(255, 192, 203);
    public static final LightColor ORANGE = new LightColor(255, 165, 0);
    public static final LightColor YELLOW = new LightColor(255, 255, 0);
    public static final LightColor CYAN = new LightColor(0, 255, 255);
    public static final LightColor BROWN = new LightColor(165, 42, 42);
    public static final LightColor WHITE = new LightColor(255, 255, 255);
    public static final LightColor IVORY = new LightColor(255, 255, 240);
    public static final LightColor GRAY = new LightColor(128, 128, 128);

    private int red;
    private int green;
    private int blue;

    /**
     * A light color.
     *
     * @param r Red (0 - 255)
     * @param g Green (0 - 255)
     * @param b Blue (0 - 255)
     */
    public LightColor(int r, int g, int b) {
        red = (r > 255 || r < 0) ? 255 : r;
        green = (g > 255 || g < 0) ? 255 : g;
        blue = (b > 255 || b < 0) ? 255 : b;
    }

    /**
     * Get the red color
     *
     * @return 0 - 255
     */
    public int getRed() {
        return red;
    }

    /**
     * Set the red color
     *
     * @param r Red (0 - 255)
     */
    public void setRed(int r) {
        red = (r > 255 || r < 0) ? 255 : r;
    }

    /**
     * Get the green color
     *
     * @return 0 - 255
     */
    public int getGreen() {
        return green;
    }

    /**
     * Set the green color
     *
     * @param g Green (0 - 255)
     */
    public void setGreen(int g) {
        green = (g > 255 || g < 0) ? 255 : g;
    }

    /**
     * Get the blue color
     *
     * @return 0 - 255
     */
    public int getBlue() {
        return blue;
    }

    /**
     * Set the blue color.
     *
     * @param b Blue (0 - 255)
     */
    public void setBlue(int b) {
        blue = (b > 255 || b < 0) ? 255 : b;
    }

    /**
     * Convert this color into a vector that JOML can read.
     *
     * @return A Vector3f
     */
    public Vector3f toVector() {
        return new Vector3f((float) red / (float) 255, (float) green / (float) 255, (float) blue / (float) 255);
    }
}
