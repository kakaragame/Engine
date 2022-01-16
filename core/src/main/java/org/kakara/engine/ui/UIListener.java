package org.kakara.engine.ui;

import org.kakara.engine.ui.events.UActionEvent;

import java.util.Map;

public interface UIListener {
    /**
     * Add supported UAction events.
     *
     * @param clazz The UAction event class.
     * @param uae   The event.
     */
    void addUActionEvent(Class<? extends UActionEvent> clazz, UActionEvent uae);

    /**
     * Process and call events.
     * <p>
     * TODO fix this.
     *
     * @param clazz The type of event
     * @param objs  The parameters
     * @param <T>   The object types.
     */
    <T> void triggerEvent(Class<? extends UActionEvent> clazz, T... objs);

    /**
     * Returns a Map of all registered UActionEvent
     *
     * @return the map
     * @see UActionEvent
     */
    Map<UActionEvent, Class<? extends UActionEvent>> getEvents();
}
