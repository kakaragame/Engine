package org.kakara.engine.utils;

import org.joml.Vector3f;
import org.joml.Vector4f;

/**
 * RGBA stores the color values for the engine.
 *
 * <p>There are 34 pre-defined colors for you to use. They have the same RGB values
 * as their CSS counter parts for consistency.</p>
 *
 * <h2>Color Values</h2>
 * <table>
 *     <tr>
 *         <th>Color Value</th>
 *         <th>Definition</th>
 *     </tr>
 *     <tr>
 *         <td>Red (R)</td>
 *         <td>The amount of red. (Can be defined as 0-255 or 0-1 depending on the constructor used.)</td>
 *     </tr>
 *     <tr>
 *         <td>Green (G)</td>
 *         <td>The amount of green. (Can be defined as 0-255 or 0-1 depending on the constructor used.)</td>
 *     </tr>
 *     <tr>
 *         <td>Blue (B)</td>
 *         <td>The amount of blue. (Can be defined as 0-255 or 0-1 depending on the constructor used.)</td>
 *     </tr>
 *     <tr>
 *         <td>Alpha (a)</td>
 *         <td>The opacity of the color. 0-1 values with 1 being complete opaque and 0 being transparent.</td>
 *     </tr>
 *     <caption>The RGBA table</caption>
 * </table>
 */
public class RGBA {
    public static final RGBA RED = new RGBA(255, 0, 0);
    public static final RGBA SALMON = new RGBA(250, 128, 114);
    public static final RGBA CRIMSON = new RGBA(220, 20, 60);
    public static final RGBA ORANGE = new RGBA(255, 165, 0);
    public static final RGBA CORAL = new RGBA(255, 127, 80);
    public static final RGBA GOLD = new RGBA(255, 215, 0);
    public static final RGBA YELLOW = new RGBA(255, 255, 0);
    public static final RGBA PEACH = new RGBA(255, 218, 185);
    public static final RGBA LIGHT_YELLOW = new RGBA(255, 255, 224);
    public static final RGBA LIME = new RGBA(0, 255, 0);
    public static final RGBA GREEN = new RGBA(0, 128, 0);
    public static final RGBA DARK_GREEN = new RGBA(0, 100, 0);
    public static final RGBA SPRING_GREEN = new RGBA(0, 255, 127);
    public static final RGBA SEA_GREEN = new RGBA(46, 139, 87);
    public static final RGBA OLIVE = new RGBA(128, 128, 0);
    public static final RGBA CYAN = new RGBA(0, 255, 255);
    public static final RGBA LIGHT_CYAN = new RGBA(224, 255, 255);
    public static final RGBA TURQUOISE = new RGBA(64, 224, 208);
    public static final RGBA TEAL = new RGBA(0, 128, 128);
    public static final RGBA SKY_BLUE = new RGBA(135, 206, 235);
    public static final RGBA BLUE = new RGBA(0, 0, 255);
    public static final RGBA DARK_BLUE = new RGBA(0, 0, 139);
    public static final RGBA MAGENTA = new RGBA(255, 0, 255);
    public static final RGBA VIOLET = new RGBA(148, 0, 211);
    public static final RGBA PURPLE = new RGBA(128, 0, 128);
    public static final RGBA PINK = new RGBA(255, 192, 203);
    public static final RGBA DEEP_PINK = new RGBA(255, 20, 147);
    public static final RGBA WHITE = new RGBA(255, 255, 255);
    public static final RGBA AZURE = new RGBA(240, 255, 255);
    public static final RGBA LIGHT_GRAY = new RGBA(211, 211, 211);
    public static final RGBA SILVER = new RGBA(192, 192, 192);
    public static final RGBA GRAY = new RGBA(128, 128, 128);
    public static final RGBA BLACK = new RGBA(0, 0, 0);
    public static final RGBA BROWN = new RGBA(165, 42, 42);
    public static final RGBA CHOCOLATE = new RGBA(210, 105, 30);

    private final Vector4f vec;
    public int r;
    public int g;
    public int b;
    public float a;

    /**
     * Set the values of RGBA.
     *
     * <p>By default the a value is set to 1.</p>
     *
     * @param r Red - (0-255)
     * @param g Green - (0-255)
     * @param b Blue - (0-255)
     */
    public RGBA(int r, int g, int b) {
        if (r < 0 || r > 255)
            throw new IllegalArgumentException("The red value is not within the range 0-255");
        if (g < 0 || g > 255)
            throw new IllegalArgumentException("The green value is not within the range 0-255");
        if (b < 0 || b > 255)
            throw new IllegalArgumentException("The blue value is not within the range 0-255");

        this.r = r;
        this.g = g;
        this.b = b;
        this.a = 1;

        vec = new Vector4f(r / 255f, g / 255f, b / 255f, 1);
    }

    /**
     * Set the values of RGBA
     *
     * @param r Red - (0-255)
     * @param g Green - (0-255)
     * @param b Blue - (0-255)
     * @param a Alpha - (0-1)
     */
    public RGBA(int r, int g, int b, float a) {
        if (r < 0 || r > 255)
            throw new IllegalArgumentException("The red value is not within the range 0-255");
        if (g < 0 || g > 255)
            throw new IllegalArgumentException("The green value is not within the range 0-255");
        if (b < 0 || b > 255)
            throw new IllegalArgumentException("The blue value is not within the range 0-255");
        if (a < 0 || a > 1)
            throw new IllegalArgumentException("The alpha value is not within the range 0-1");

        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        vec = new Vector4f(r / 255f, g / 255f, b / 255f, a);
    }

    /**
     * Create a new RGBA Object.
     *
     * @param r Red - (0-1)
     * @param g Green - (0-1)
     * @param b Blue - (0-1)
     * @param a Alpha - (0-1)
     */
    public RGBA(float r, float g, float b, float a) {
        if (r < 0 || r > 1)
            throw new IllegalArgumentException("The red value is not within the range 0-255");
        if (g < 0 || g > 1)
            throw new IllegalArgumentException("The green value is not within the range 0-255");
        if (b < 0 || b > 1)
            throw new IllegalArgumentException("The blue value is not within the range 0-255");
        if (a < 0 || a > 1)
            throw new IllegalArgumentException("The alpha value is not within the range 0-1");

        this.r = Math.round(r);
        this.g = Math.round(g);
        this.b = Math.round(b);
        this.a = a;
        vec = new Vector4f(r, g, b, a);
    }

    /**
     * Create a color object with the default values. (255, 255, 255, 1)
     */
    public RGBA() {
        this(255, 255, 255, 1);
    }

    /**
     * Set the values of RGBA
     *
     * @param r Red - (0-255)
     * @param g Green - (0-255)
     * @param b Blue - (0-255)
     * @param a Alpha - (0-1)
     * @return The RGBA color for the object
     */
    public RGBA setRGBA(int r, int g, int b, float a) {
        if (r < 0 || r > 255)
            throw new IllegalArgumentException("The red value is not within the range 0-255");
        if (g < 0 || g > 255)
            throw new IllegalArgumentException("The green value is not within the range 0-255");
        if (b < 0 || b > 255)
            throw new IllegalArgumentException("The blue value is not within the range 0-255");
        if (a < 0 || a > 1)
            throw new IllegalArgumentException("The alpha value is not within the range 0-1");

        this.r = r / 255;
        this.g = g / 255;
        this.b = b / 255;
        this.a = a;
        return this;
    }

    /**
     * Convert Alpha value to one that will be accepted by NanoVG.
     *
     * @return The alpha format used by NanoVG.
     */
    public int aToNano() {
        return (int) Math.floor(a * 255);
    }

    /**
     * Convert RGBA to a vector value.
     *
     * <p>All values are 0-1.</p>
     *
     * @return The Vector4 object that represents this RGBA color.
     */
    public Vector4f getVectorColor() {
        vec.x = r;
        vec.y = g;
        vec.z = b;
        vec.w = a;
        return vec;
    }

    /**
     * Convert RGBA to a Vector3 value.
     *
     * <p>All values are 0-1.</p>
     *
     * @return The Vector3 object that represents this RGBA color.
     */
    public Vector3f getVector3Color() {
        return new Vector3f(vec.x, vec.y, vec.z);
    }
}
