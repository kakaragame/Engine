package org.kakara.engine.ui.components;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.HUD;
import org.kakara.engine.ui.events.UActionEvent;

import java.util.List;

/**
 * The main UI component
 */
public interface Component {
    void addUActionEvent(UActionEvent uae, Class<? extends UActionEvent> clazz);

    /**
     * Add a child component to this component
     * @param component Child component
     */
    void add(Component component);

    /**
     * Internal Use Only
     */
    void render(Vector2 relativePosition, HUD hud, GameHandler handler);

    /**
     * Internal Use Only
     */
    void init(HUD hud, GameHandler handler);

    /**
     * Set the position of the element
     * @param x The x value
     * @param y The y value
     */
    void setPosition(float x, float y);

    /**
     * Set the position of the element
     * @param pos The Vector 2
     */
    void setPosition(Vector2 pos);

    /**
     * Get the position of the element
     * @return The position
     */
    Vector2 getPosition();

    /**
     * Set the scale of the element
     * @param x x scale
     * @param y y scale
     */
    void setScale(float x, float y);

    /**
     * Set the scale of the element
     * @param scale The scale in a Vector2
     */
    void setScale(Vector2 scale);

    /**
     * Get the scale
     * @return The scale
     */
    Vector2 getScale();

    /**
     * Set if the element is currently visible
     * @param visible Visibility of the element.
     */
    void setVisible(boolean visible);

    /**
     * Check if the element is currently visible
     * @return If the element is currently visible.
     */
    boolean isVisible();

    /**
     * Get all child components
     * @return The list of child components
     */
    List<Component> getChildren();

    /**
     * Clear the component of all children.
     */
    void clearChildren();

    /**
     * Remove a component from the list of children.
     * @param component The component to remove.
     */
    void remove(Component component);

}
