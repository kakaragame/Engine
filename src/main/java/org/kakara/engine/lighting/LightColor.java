package org.kakara.engine.lighting;

import org.joml.Vector3f;

/**
 * Handles the coloring of the lights.
 */
public class LightColor {
    private int red;
    private int green;
    private int blue;

    /**
     * A light color.
     * @param r Red (0 - 255)
     * @param g Green (0 - 255)
     * @param b Blue (0 - 255)
     */
    public LightColor(int r, int g, int b){
        red = (r > 255 || r < 0) ? 255 : r;
        green = (g > 255 || g < 0) ? 255 : g;
        blue = (b > 255 || b < 0) ? 255 : b;
    }

    /**
     * Set the red color
     * @param r Red (0 - 255)
     */
    public void setRed(int r){
        red = (r > 255 || r < 0) ? 255 : r;
    }

    /**
     * Set the green color
     * @param g Green (0 - 255)
     */
    public void setGreen(int g){
        green = (g > 255 || g < 0) ? 255 : g;
    }

    /**
     * Set the blue color.
     * @param b Blue (0 - 255)
     */
    public void setBlue(int b){
        blue = (b > 255 || b < 0) ? 255 : b;
    }

    /**
     * Get the red color
     * @return 0 - 255
     */
    public int getRed(){
        return red;
    }

    /**
     * Get the green color
     * @return 0 - 255
     */
    public int getGreen(){
        return green;
    }

    /**
     * Get the blue color
     * @return 0 - 255
     */
    public int getBlue(){
        return blue;
    }

    /**
     * Convert this color into a vector that JOML can read.
     * @return A Vector3f
     */
    public Vector3f toVector(){
        return new Vector3f((float)red/(float)255, (float)green/(float)255, (float)blue/(float)255);
    }
}
