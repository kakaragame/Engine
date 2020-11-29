package org.kakara.engine.events;

import org.kakara.engine.GameEngine;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Handles all of the events.
 */
public class EventManager {
    private final List<Object> handlers;

    public EventManager() {
        handlers = new CopyOnWriteArrayList<>();
    }

    /**
     * Register an event for a specific scene.
     * If you want the event to always be active no matter the scene than set scene to null.
     *
     * @param obj The object that the event is in
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
            List<Method> mtd = new ArrayList<>(Arrays.asList(event.getClass().getDeclaredMethods()));
            for (Method msd : mtd) {
                if (msd.getParameterCount() != 1) continue;
                if (!msd.isAnnotationPresent(EventHandler.class)) continue;
                if (msd.getParameters()[0].getType().isInstance(eventInstance)) {
                    try {
                        msd.invoke(event, eventInstance);
                    } catch (IllegalAccessException | InvocationTargetException ex) {
                        GameEngine.LOGGER.error("Cannot fire event specified : " + eventInstance.getClass().getName(), ex);
                    }
                }

            }
        }

    }
}
