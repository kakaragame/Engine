package org.kakara.engine.utils;

/**
 * A utility class that is responsible for keeping track of time.
 */
public class Time {

    private static float deltaTime;
    private double lastLoopTime;

    /**
     * The time in between frames. (In milliseconds).
     *
     * @return The time between frames. (In milliseconds).
     */
    public static float getDeltaTime() {
        return Time.deltaTime;
    }

    public void init() {
        lastLoopTime = getTime();
    }

    /**
     * Get the current delta time.
     *
     * @return The delta time.
     */
    public double getTime() {
        return System.nanoTime() / 1000_000_000.0;
    }

    /**
     * the change in time between frames. (In milliseconds).
     *
     * @return The time between frames (delta time).
     */
    public float getElapsedTime() {
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        Time.deltaTime = elapsedTime;
        return elapsedTime;
    }

    public double getLastLoopTime() {
        return lastLoopTime;
    }
}
