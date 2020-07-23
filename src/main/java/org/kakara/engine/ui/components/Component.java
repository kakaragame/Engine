package org.kakara.engine.ui.components;

import org.jetbrains.annotations.Nullable;
import org.kakara.engine.GameHandler;
import org.kakara.engine.item.Tagable;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.HUD;
import org.kakara.engine.ui.events.UActionEvent;
import org.kakara.engine.ui.constraints.Constraint;

import java.util.List;

/**
 * The main UI component
 */
public interface Component extends Tagable {
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
     * Internal Use Only
     */
    void cleanup(GameHandler handler);

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

    /**
     * Get the parent of the component
     * @since 1.0-Pre1
     * @return The parent of the component (Null if none or the parent is the canvas component).
     */
    Component getParent();

    /**
     * Set the parent of the component
     * <p><b>Internal Use Only!</b></p>
     * @since 1.0-Pre1
     * @param parent The parent.
     */
    void setParent(@Nullable Component parent);

    /**
     * Add a constraint to the component
     * @since 1.0-Pre1
     * @param constraint The property to add.
     */
    void addConstraint(Constraint constraint);

    /**
     * Remove a constraint from the component
     * @since 1.0-Pre1
     * @param constraint The property class to remove.
     */
    void removeConstraint(Class<Constraint> constraint);

}
