package org.kakara.engine.math;

import org.joml.Vector2d;

/**
 * Handles 2d vector calculations.
 */
public class Vector2 {
    public float x, y;

    /**
     * Create a vector using x and y
     * @param x X value
     * @param y Y value
     */
    public Vector2(float x, float y){
        this.x = x;
        this.y = y;
    }

    /**
     * Create a vector using another vector
     * <p>This create a clone of the vector.</p>
     * @param vec The vector.
     */
    public Vector2(Vector2 vec){
        this.x = vec.x;
        this.y = vec.y;
    }

    /**
     * Create a vector using the JOML Vector2d.
     * @param vec The vector2d from JOML
     */
    public Vector2(Vector2d vec){
        this.x = (float) vec.x;
        this.y = (float) vec.y;
    }

    /**
     * Add a value to the vector.
     * <p>This mutates the vector.</p>
     * @param x The x value
     * @param y The y value
     * @return The original vector.
     */
    public Vector2 add(float x, float y){
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Add a value to the vector.
     * <p>This mutates the original vector. The vector that is being added to this vector is unaffected.</p>
     * @param vec The vector to add by
     * @return The original vector.
     */
    public Vector2 add(Vector2 vec){
        return add(vec.x, vec.y);
    }

    /**
     * Subtract a value to the vector.
     * <p>This mutates the vector.</p>
     * @param x The x value
     * @param y The y value
     * @return The original vector.
     */
    public Vector2 subtract(float x, float y){
        this.x -= x;
        this.y -= y;
        return this;
    }

    /**
     * Subtract a value to the vector.
     * <p>THis mutates the original vector. The vector being subtracted in unaffected.</p>
     * @param vec The vector to subtract by
     * @return The original vector.
     */
    public Vector2 subtract(Vector2 vec){
        return subtract(vec.x, vec.y);
    }

    /**
     * Create a clone of the vector.
     * <p>This will create a new version of the vector that <b>will not</b> mutate the original vector.</p>
     * @return The vector clone.
     */
    public Vector2 clone(){
        return new Vector2(this);
    }

    @Override
    public String toString(){
        return "Vector2 { " + x + ", " + y + " }";
    }
}
