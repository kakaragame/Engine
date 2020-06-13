package org.kakara.engine.events;

import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.scene.Scene;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Handles all of the events.
 */
public class EventManager {
    private List<Object> handlers;

    public EventManager() {
        handlers = new CopyOnWriteArrayList<>();
    }

    /**
     * Register an event for a specific scene.
     * If you want the event to always be active no matter the scene than set scene to null.
     *
     * @param obj   The object that the event is in
     */
    public void registerHandler(Object obj) {
        handlers.add(obj);
    }

    /**
     * Fire an event.
     *
     * @param eventInstance The instance of said event.
     */
    public void fireHandler(Object eventInstance) {
        for (Object event : handlers) {
            Object obj = event;
            List<Method> mtd = new ArrayList<>(Arrays.asList(obj.getClass().getDeclaredMethods()));
            for (Method msd : mtd) {
                if (msd.getParameterCount() != 1) continue;
                if (msd.isAnnotationPresent(EventHandler.class)) {
                    if (msd.getParameters()[0].getType().isInstance(eventInstance)) {
                        try {
                            msd.invoke(obj, eventInstance);
                        } catch (IllegalAccessException | InvocationTargetException ex) {
                            GameEngine.LOGGER.error("Cannot fire event specified : " + eventInstance.getClass().getName(), ex);
                        }
                    }
                }
            }
        }

    }

    /**
     * Used to debug the game.
     * @deprecated Not to be used in final release.
     */
    public void debug(){
        GameEngine.LOGGER.debug(handlers.toString());
    }
}
