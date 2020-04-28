package org.kakara.engine.events;

import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.scene.Scene;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * Handles all of the events.
 */
public class EventManager {
    private Map<Object, Scene> handlers;
    private GameHandler gameHandler;

    public EventManager(GameHandler gameHandler) {
        handlers = new HashMap<>();
        this.gameHandler = gameHandler;
    }

    /**
     * Register an event.
     *
     * @param obj The object to register.
     * @deprecated Use {@link #registerHandler(Object, Scene)} instead.
     */
    public void registerHandler(Object obj, boolean global) {
        if (gameHandler.getSceneManager().getCurrentScene() == null) {
            handlers.put(obj, null);
            return;
        }
        handlers.put(obj, global ? null : gameHandler.getSceneManager().getCurrentScene());
    }

    /**
     * Register an event for a specific scene.
     * If you want the event to always be active no matter the scene than set scene to null.
     *
     * @param obj   The object that the event is in
     * @param scene The current scene, null if you want the event to be global.
     */
    public void registerHandler(Object obj, Scene scene) {
        handlers.put(obj, scene);
    }

    /**
     * Fire an event.
     *
     * @param eventInstance The instance of said event.
     */
    public void fireHandler(Object eventInstance) {
        for (Map.Entry<Object, Scene> event : new ArrayList<>(handlers.entrySet())) {
            Object obj = event.getKey();
            List<Method> mtd = new ArrayList<>(Arrays.asList(obj.getClass().getDeclaredMethods()));
            for (Method msd : mtd) {
                if (msd.getParameterCount() != 1) continue;
                if (msd.isAnnotationPresent(EventHandler.class)) {
                    if (msd.getParameters()[0].getType().isInstance(eventInstance)) {
                        try {
                            msd.invoke(obj, eventInstance);
                        } catch (IllegalAccessException | InvocationTargetException ex) {
                            GameEngine.LOGGER.error("Cannot fire event specified : " + eventInstance.getClass().getName());
                        }
                    }
                }
            }
        }

    }

    /**
     * Cleanup the Event Manager that way it is ready for the next scene.
     * <p>Internal Use Only</p>
     */
    public void cleanup() {
        // I think this was added in Java 8.
        handlers.entrySet().removeIf(event -> event.getValue() == GameHandler.getInstance().getSceneManager().getCurrentScene());
    }
}
