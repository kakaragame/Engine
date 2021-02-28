package org.kakara.engine.math;

import org.joml.Vector2d;

/**
 * Handles 2d vector calculations.
 */
public class Vector2 {
    public float x, y;

    /**
     * Create a vector using x and y
     *
     * @param x X value
     * @param y Y value
     */
    public Vector2(float x, float y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Create a vector using another vector
     * <p>This create a clone of the vector.</p>
     *
     * @param vec The vector.
     */
    public Vector2(Vector2 vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    /**
     * Create a vector using the JOML Vector2d.
     *
     * @param vec The vector2d from JOML
     */
    public Vector2(Vector2d vec) {
        this.x = (float) vec.x;
        this.y = (float) vec.y;
    }

    /**
     * Convert a Vector 3 to a Vector 2
     * <p>The z component of the Vector 3 is ignored.</p>
     *
     * @param vec The 3D vector.
     * @since 1.0-Pre3
     */
    public Vector2(Vector3 vec) {
        this.x = vec.x;
        this.y = vec.y;
    }

    /**
     * Add a value to the vector.
     * <p>This mutates the vector.</p>
     *
     * @param x The x value
     * @param y The y value
     * @return The original vector.
     */
    public Vector2 addMut(float x, float y) {
        this.x += x;
        this.y += y;
        return this;
    }

    /**
     * Add a value to the vector.
     * <p>This mutates the original vector. The vector that is being added to this vector is unaffected.</p>
     *
     * @param vec The vector to add by
     * @return The original vector.
     */
    public Vector2 addMut(Vector2 vec) {
        return addMut(vec.x, vec.y);
    }

    /**
     * Add a value to the vector. (Non-Mutated).
     *
     * @param x The x value.
     * @param y The y value.
     * @return The vector after addition.
     */
    public Vector2 add(float x, float y) {
        return new Vector2(this.x + x, this.y + y);
    }

    /**
     * Add a value to the vector. (Non-Mutated).
     *
     * @param vec The vector.
     * @return The vector after addition.
     */
    public Vector2 add(Vector2 vec) {
        return add(vec.x, vec.y);
    }

    /**
     * Subtract a value to the vector.
     * <p>This mutates the vector.</p>
     *
     * @param x The x value
     * @param y The y value
     * @return The original vector.
     */
    public Vector2 subtractMut(float x, float y) {
        this.x -= x;
        this.y -= y;
        return this;
    }

    /**
     * Subtract a value to the vector.
     * <p>This mutates the original vector. The vector being subtracted in unaffected.</p>
     *
     * @param vec The vector to subtract by
     * @return The original vector.
     */
    public Vector2 subtractMut(Vector2 vec) {
        return subtractMut(vec.x, vec.y);
    }

    /**
     * Subtract a x, y from the vector. (Non-Mutated).
     *
     * @param x The x value.
     * @param y The y value.
     * @return The vector after subtraction
     */
    public Vector2 subtract(float x, float y) {
        return new Vector2(this.x - x, this.y - y);
    }

    /**
     * Subtract a vector from the current vector. (Non-Mutated).
     *
     * @param vec The x value.
     * @return The vector after subtraction
     */
    public Vector2 subtract(Vector2 vec) {
        return subtract(vec.x, vec.y);
    }

    /**
     * Create a clone of the vector.
     * <p>This will create a new version of the vector that <b>will not</b> mutate the original vector.</p>
     *
     * @return The vector clone.
     */
    public Vector2 clone() {
        return new Vector2(this);
    }

    /**
     * Get the value of x.
     *
     * @return The value of x.
     * @since 1.0-Pre1
     */
    public float getX() {
        return x;
    }

    /**
     * Set the x value.
     *
     * @param x The x value.
     * @since 1.0-Pre1
     */
    public void setX(float x) {
        this.x = x;
    }

    /**
     * Get the value of y.
     *
     * @return The value of y.
     * @since 1.0-Pre1
     */
    public float getY() {
        return y;
    }

    /**
     * Set the y value.
     *
     * @param y The y value.
     * @since 1.0-Pre1
     */
    public void setY(float y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object obj) {
        if (!(obj instanceof Vector2)) return false;
        Vector2 otherVec = (Vector2) obj;
        return this.x == otherVec.x && this.y == otherVec.y;
    }

    @Override
    public String toString() {
        return "Vector2 { " + x + ", " + y + " }";
    }
}
