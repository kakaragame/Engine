package org.kakara.engine.math;

import org.joml.Vector3f;

/**
 * A math class to provide a nice representation of a vector.
 */
public class Vector3 {

    public float x, y, z;

    private float idX = 0, idY = 0, idZ = 0;

    /**
     * Create a vector3.
     *
     * @param x The x value
     * @param y The y value
     * @param z The z value
     */
    public Vector3(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * Creates a vector 3 based off of another one.
     * <p>The vector that is provided is cloned and not mutated.</p>
     *
     * @param vec The vector to clone.
     */
    public Vector3(Vector3f vec) {
        this(vec.x, vec.y, vec.z);
    }

    /**
     * Create a vector 3 using doubles
     *
     * @param x The x double
     * @param y The y double
     * @param z The z double
     */
    public Vector3(double x, double y, double z) {
        this((float) x, (float) y, (float) z);
    }

    /**
     * Creates a zero vector.
     */
    public Vector3(){
        this(0, 0, 0);
    }

    /**
     * Creates a vector 3 using a 2d vector.
     *
     * @param vec The 2d vector to get the x and y values from.
     * @param z   The z value.
     * @since 1.0-Pre3
     */
    public Vector3(Vector2 vec, float z) {
        this.x = vec.x;
        this.y = vec.y;
        this.z = z;
    }

    /**
     * Creates a vector 3 using a 2d vector. (z is set to 0).
     *
     * @param vec The 2d vector to get the x and y values from.
     * @since 1.0-Pre3
     */
    public Vector3(Vector2 vec) {
        this(vec, 0);
    }

    /**
     * Get the x value.
     *
     * @return The x value.
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
     * Get the y value.
     *
     * @return The y value.
     * @since 1.0-Pre1
     */
    public float getY() {
        return this.y;
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

    /**
     * Get the z value.
     *
     * @return The z value.
     * @since 1.0-Pre1
     */
    public float getZ() {
        return this.z;
    }

    /**
     * Set the z value.
     *
     * @param z The z value.
     * @since 1.0-Pre1
     */
    public void setZ(float z) {
        this.z = z;
    }

    public Vector3 identity(float x, float y, float z){
        this.idX = x;
        this.idY = y;
        this.idZ = z;
        return this;
    }

    public Vector3 identity(){
        this.x = idX;
        this.y = idY;
        this.z = idZ;
        return this;
    }

    public Vector3 identitySelf(){
        this.idX = x;
        this.idY = y;
        this.idZ = z;
        return this;
    }

    /**
     * Clone the vector
     * <p>The original vector is not mutated.</p>
     *
     * @return The cloned vector.
     */
    @Override
    public Vector3 clone() {
        return new Vector3(this.x, this.y, this.z);
    }

    /**
     * Subtract a vector with mutation
     *
     * @param other The other vector
     * @return An instance of the current vector.
     */
    public Vector3 subtractMut(Vector3 other) {
        set(this.x - other.x, this.y - other.y, this.z - other.z);
        return this;
    }

    /**
     * Subtract a vector with mutation
     *
     * @param x x value
     * @param y y value
     * @param z z value
     * @return An instance of the current vector.
     */
    public Vector3 subtractMut(float x, float y, float z) {
        set(this.x - x, this.y - y, this.z - z);
        return this;
    }

    /**
     * Subtract a vector without mutating the current vector.
     *
     * @param x x value
     * @param y y value
     * @param z z value
     * @return The vector after subtraction.
     */
    public Vector3 subtract(float x, float y, float z) {
        return new Vector3(this.x - x, this.y - y, this.z - z);
    }

    /**
     * Subtract a vector without mutating the current vector.
     *
     * @param other The other vector.
     * @return The vector after subtraction.
     */
    public Vector3 subtract(Vector3 other) {
        return subtract(other.x, other.y, other.z);
    }

    /**
     * Add a vector with mutation
     *
     * @param other The other vector
     * @return An instance of the current vector.
     */
    public Vector3 addMut(Vector3 other) {
        set(this.x + other.x, this.y + other.y, this.z + other.z);
        return this;
    }

    /**
     * Add a vector with mutation
     *
     * @param x x value
     * @param y y value
     * @param z z value
     * @return An instance of the current vector.
     */
    public Vector3 addMut(float x, float y, float z) {
        set(this.x + x, this.y + y, this.z + z);
        return this;
    }

    /**
     * Add a vector with mutation.
     * @param other The other vector.
     * @return An instance of the current vector.
     */
    public Vector3 addMut(Vector3f other){
        return addMut(other.x, other.y, other.z);
    }

    /**
     * Add a vector without mutating the current vector.
     *
     * @param x x value
     * @param y y value
     * @param z z value
     * @return The vector after addition.
     */
    public Vector3 add(float x, float y, float z) {
        return new Vector3(x + this.x, y + this.y, z + this.z);
    }

    /**
     * Add a vector without mutating the current vector.
     *
     * @param other The other vector.
     * @return The vector after addition
     */
    public Vector3 add(Vector3 other) {
        return add(other.x, other.y, other.z);
    }

    /**
     * Add a vector without mutating the current vector.
     * @param other The other vector.
     * @return The vector after addition.
     */
    public Vector3 add(Vector3f other){
        return add(other.x, other.y, other.z);
    }

    /**
     * Multiply a vector by a constant with mutation
     *
     * @param v The constant
     * @return An instance of the current vector.
     */
    public Vector3 multiplyMut(float v) {
        set(this.x * v, this.y * v, this.z * v);
        return this;
    }

    /**
     * Divide a vector by a constant with mutation
     *
     * @param v The constant
     * @return An instance of the current vector.
     */
    public Vector3 divideMut(float v) {
        set(this.x / v, this.y / v, this.z / v);
        return this;
    }

    /**
     * Do the dot product with another vector.
     *
     * @param vec The other vector.
     * @return The dot product.
     */
    public float dot(Vector3 vec) {
        return (x * vec.x) + (y * vec.y) + (z * vec.z);
    }

    /**
     * Do the cross product with another vector. (No Mutation)
     *
     * @param vec The other vector.
     * @return The vector.
     */
    public Vector3 cross(Vector3 vec) {
        return new Vector3((y * vec.z) - (z * vec.y), (z * vec.x) - (x * vec.z), (x * vec.y) - (y * vec.x));
    }

    /**
     * Do the cross product with another vector. (Mutation)
     *
     * @param vec The other vector.
     * @return An instance of the current vector.
     */
    public Vector3 crossMut(Vector3 vec) {
        set((y * vec.z) - (z * vec.y), (z * vec.x) - (x * vec.z), (x * vec.y) - (y * vec.x));
        return this;
    }

    /**
     * Get the magnitude of the vector.
     *
     * @return The magnitude of the vector.
     */
    public double magnitude() {
        return Math.sqrt(Math.pow(x, 2) + Math.pow(y, 2) + Math.pow(z, 2));
    }

    /**
     * Convert the vector to the JOML version.
     *
     * @return The JOML Vector3f object.
     */
    public Vector3f toJoml() {
        return new Vector3f(this.x, this.y, this.z);
    }

    /**
     * Compare one vector to another.
     *
     * @param other The other vector
     * @return If this vector is greater than the other one.
     */
    public boolean greaterThan(Vector3 other) {
        return magnitude() > other.magnitude();
    }

    /**
     * Compare one vector to another using the compare point is the point of comparision.
     *
     * @param other        The vector to compare
     * @param comparePoint The point at which the vectors will be compared to.
     * @return If this vector is greater than the other one.
     */
    public boolean greaterThan(Vector3 other, Vector3 comparePoint) {
        return KMath.distance(this, comparePoint) > KMath.distance(other, comparePoint);
    }

    /**
     * Set the value of the vector.
     *
     * @param vec The vector to set this vector to. (vec is not mutated).
     */
    public void set(Vector3 vec) {
        set(vec.x, vec.y, vec.z);
    }

    /**
     * Set the value of the vector.
     *
     * @param x The x value.
     * @param y The y value.
     * @param z The z value.
     */
    public void set(float x, float y, float z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Vector3)) return false;
        Vector3 vec = (Vector3) o;
        return vec.x == x && vec.y == y && vec.z == z;
    }

    @Override
    public String toString() {
        return "{" + this.x + ", " + this.y + ", " + this.z + "}";
    }
}
