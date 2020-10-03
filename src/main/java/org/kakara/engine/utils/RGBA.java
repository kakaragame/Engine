package org.kakara.engine.utils;

import org.joml.Vector4f;

/**
 * A class to store color values.
 */
public class RGBA {
    public int r;
    public int g;
    public int b;
    public float a;

    private Vector4f vec;

    /**
     * Set the values of RGBA
     *
     * @param r Red - (0-255)
     * @param g Green - (0-255)
     * @param b Blue - (0-255)
     * @param a Alpha - (0-1)
     */
    public RGBA(int r, int g, int b, float a) {
        if(r < 0 || r > 255)
            throw new IllegalArgumentException("The red value is not within the range 0-255");
        if(g < 0 || g > 255)
            throw new IllegalArgumentException("The green value is not within the range 0-255");
        if(b < 0 || b > 255)
            throw new IllegalArgumentException("The blue value is not within the range 0-255");
        if(a < 0 || a > 1)
            throw new IllegalArgumentException("The alpha value is not within the range 0-1");

        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        vec = new Vector4f(r/255f, g/255f, b/255f, a);
    }

    /**
     * Create a new RGBA Object.
     *
     * @param r Red - (0-1)
     * @param g Green - (0-1)
     * @param b Blue - (0-1)
     * @param a Alpha - (0-1)
     */
    public RGBA(float r, float g, float b, float a){
        if(r < 0 || r > 1)
            throw new IllegalArgumentException("The red value is not within the range 0-255");
        if(g < 0 || g > 1)
            throw new IllegalArgumentException("The green value is not within the range 0-255");
        if(b < 0 || b > 1)
            throw new IllegalArgumentException("The blue value is not within the range 0-255");
        if(a < 0 || a > 1)
            throw new IllegalArgumentException("The alpha value is not within the range 0-1");

        this.r = Math.round(r * 255);
        this.g = Math.round(g * 255);
        this.b = Math.round(b * 255);
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
     */
    public RGBA setRGBA(int r, int g, int b, float a) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    /**
     * Convert Alpha value to one that will be accepted by nanovg.
     *
     * @return the int value
     */
    public int aToNano() {
        return (int) Math.floor(a * 255);
    }

    /**
     * Convert RGBA to a vector value.
     *
     * @return The Vector4 object that represents this RGBA color.
     */
    public Vector4f getVectorColor() {
        vec.x = r/255f;
        vec.y = g/255f;
        vec.z = b/255f;
        vec.w = a;
        return vec;
    }
}
