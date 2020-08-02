package org.kakara.engine.math;

import org.joml.Vector3f;

import java.lang.ref.WeakReference;

/**
 * A math class to provide a nice representation of a vector.
 */
public class Vector3 {

    public float x, y, z;

    /**
     * Create a vector3.
     * @param x The x value
     * @param y The y value
     * @param z The z value
     */
    public Vector3(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a vector 3 based off of another one.
     * <p>The vector that is provided is cloned and not mutated.</p>
     * @param vec The vector to clone.
     */
    public Vector3(Vector3f vec){
        this(vec.x, vec.y, vec.z);
    }

    /**
     * Create a vector 3 using doubles
     * @param x The x double
     * @param y The y double
     * @param z The z double
     */
    public Vector3(double x, double y, double z) {
        this((float) x, (float) y, (float) z);
    }

    /**
     * Creates a vector 3 using a 2d vector.
     *
     * @since 1.0-Pre3
     * @param vec The 2d vector to get the x and y values from.
     * @param z The z value.
     */
    public Vector3(Vector2 vec, float z){
        this.x = vec.x;
        this.y = vec.y;
        this.z = z;
    }

    /**
     * Creates a vector 3 using a 2d vector. (z is set to 0).
     *
     * @since 1.0-Pre3
     * @param vec The 2d vector to get the x and y values from.
     */
    public Vector3(Vector2 vec){
        this(vec, 0);
    }

    /**
     * Get the x value.
     * @since 1.0-Pre1
     * @return The x value.
     */
    public float getX(){
        return x;
    }

    /**
     * Set the x value.
     * @since 1.0-Pre1
     * @param x The x value.
     */
    public void setX(float x){
        this.x = x;
    }

    /**
     * Get the y value.
     * @since 1.0-Pre1
     * @return The y value.
     */
    public float getY(){
        return this.y;
    }

    /**
     * Set the y value.
     * @since 1.0-Pre1
     * @param y The y value.
     */
    public void setY(float y){
        this.y = y;
    }

    /**
     * Get the z value.
     * @since 1.0-Pre1
     * @return The z value.
     */
    public float getZ(){
        return this.z;
    }

    /**
     * Set the z value.
     * @since 1.0-Pre1
     * @param z The z value.
     */
    public void setZ(float z){
        this.z = z;
    }

    /**
     * Clone the vector
     * TODO THIS IS ONLY A TEMPORARY FIX. ADDITIONAL MEASURES MUST BE TAKEN TO KEEP MEMORY DOWN.
     * <p>The original vector is not mutated.</p>
     * @return The cloned vector.
     */
    public Vector3 clone(){
        return this;
    }

    /**
     * Subtract a vector
     * <p>The operation does <b>not</b> mutate the vector.</p>
     * @param other The other vector
     * @return The vector after the operation.
     */
    public Vector3 subtract(Vector3 other){
        return new Vector3(this.x - other.x, this.y - other.y, this.z - other.z);
    }

    /**
     * Subtract a vector
     * <p>The operation does <b>not</b> mutate the vector.</p>
     * @param x x value
     * @param y y value
     * @param z z value
     * @return The vector after the operation.
     */
    public Vector3 subtract(float x, float y, float z){
        return new Vector3(this.x - x, this.y - y, this.z - z);
    }

    /**
     * Add a vector
     * <p>The operation does <b>not</b> mutate the vector.</p>
     * @param other The other vector
     * @return The vector after the operation.
     */
    public Vector3 add(Vector3 other){
        return new Vector3(this.x + other.x, this.y + other.y, this.z + other.z);
    }

    /**
     * Add a vector
     * <p>The operation does <b>not</b> mutate the vector.</p>
     * @param x x value
     * @param y y value
     * @param z z value
     * @return The vector after the operation.
     */
    public Vector3 add(float x, float y, float z){
        return new Vector3(this.x + x, this.y + y, this.z + z);
    }

    /**
     * Multiply a vector by a constant
     * <p>The operation does <b>not</b> mutate the vector.</p>
     * @param v The constant
     * @return The vector after the operation.
     */
    public Vector3 multiply(float v){
        return new Vector3(this.x * v, this.y * v, this.z * v);
    }

    /**
     * Divide a vector by a constant
     * <p>The operation does <b>not</b> mutate the vector.</p>
     * @param v The constant
     * @return The vector after the operation.
     */
    public Vector3 divide(float v){
        return new Vector3(this.x / v, this.y / v, this.z / v);
    }

    /**
     * Convert the vector to the JOML version.
     * @return The JOML Vector3f object.
     */
    public Vector3f toJoml(){
        return new Vector3f(this.x, this.y, this.z);
    }

    /**
     * Compare one vector to another.
     * @param other The other vector
     * @return If this vector is greater than the other one.
     */
    public boolean greaterThan(Vector3 other){
        return Math.sqrt(Math.pow(this.x, 2) + Math.pow(this.y, 2) + Math.pow(this.z, 2)) > Math.sqrt(Math.pow(other.x, 2) + Math.pow(other.y, 2) + Math.pow(other.z, 2));
    }

    /**
     * Compare one vector to another using the compare point is the point of comparision.
     * @param other The vector to compare
     * @param comparePoint The point at which the vectors will be compared to.
     * @return If this vector is greater than the other one.
     */
    public boolean greaterThan(Vector3 other, Vector3 comparePoint){
        return KMath.distance(this, comparePoint) > KMath.distance(other, comparePoint);
    }

    @Override
    public boolean equals(Object o){
        if(!(o instanceof Vector3)) return false;
        Vector3 vec = (Vector3) o;
        return vec.x == x && vec.y == y && vec.z == z;
    }

    @Override
    public String toString(){
        return "{" + this.x + ", " + this.y + ", " + this.z + "}";
    }
}
