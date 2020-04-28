package org.kakara.engine.ui.properties;

import org.kakara.engine.ui.components.Component;

/**
 * Properties for UI components
 * @since 1.0-Pre1
 */
public interface ComponentProperty {
    /**
     * When the property is added to the component
     * @param component The component it was added to
     */
    void onAdd(Component component);

    /**
     * When the property is removed from the component.
     * <p>This is only fired when the {@link org.kakara.engine.ui.components.GeneralComponent#removeProperty(Class)} method is called.</p>
     * <p>This is <b>not</b> called when the scene is un loaded.</p>
     * @param component The component the property was removed from.
     */
    void onRemove(Component component);

    /**
     * This method is called every update.
     * @param component The component the property is on.
     */
    void update(Component component);
}
