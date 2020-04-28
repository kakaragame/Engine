package org.kakara.engine.utils;

/**
 * A utility class that is responsible for keeping track of time.
 */
public class Time {

    private double lastLoopTime;

    /**
     * The change in time between frames (In milliseconds).
     */
    public static float deltaTime;

    public void init(){
        lastLoopTime = getTime();
    }

    /**
     * Get the current delta time.
     * @return The delta time.
     */
    public double getTime(){
        return System.nanoTime() / 1000_000_000.0;
    }

    /**
     * the change in time between frames. (In milliseconds).
     * @return
     */
    public float getElapsedTime(){
        double time = getTime();
        float elapsedTime = (float) (time - lastLoopTime);
        lastLoopTime = time;
        return elapsedTime;
    }

    public double getLastLoopTime(){
        return lastLoopTime;
    }
}
