package org.kakara.engine.ui;

/**
 * A class to store color values.
 */
public class RGBA {
    public int r;
    public int g;
    public int b;
    public float a;

    /**
     * Set the values of RGBA
     * @param r Red - (0-255)
     * @param g Green - (0-255)
     * @param b Blue - (0-255)
     * @param a Alpha - (0-1)
     */
    public RGBA(int r, int g, int b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
    }

    public RGBA(){
        this(255, 255, 255, 1);
    }

    /**
     * Set the values of RGBA
     * @param r Red - (0-255)
     * @param g Green - (0-255)
     * @param b Blue - (0-255)
     * @param a Alpha - (0-1)
     */
    public RGBA setRGBA(int r, int g, int b, float a){
        this.r = r;
        this.g = g;
        this.b = b;
        this.a = a;
        return this;
    }

    /**
     * Convert Alpha value to one that will be accepted by nanovg.
     * @return the int value
     */
    public int aToNano(){
        return (int) Math.floor(a * 255);
    }
}
