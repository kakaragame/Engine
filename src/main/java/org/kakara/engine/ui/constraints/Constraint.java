package org.kakara.engine.ui.constraints;

import org.kakara.engine.ui.components.Component;

/**
 * Constraints for UI components
 *
 * @since 1.0-Pre1
 */
public interface Constraint {
    /**
     * When the constraint is added to the component
     *
     * @param component The component it was added to
     */
    void onAdd(Component component);

    /**
     * When the constraint is removed from the component.
     * <p>This is only fired when the {@link org.kakara.engine.ui.components.GeneralComponent#removeConstraint(Class)} method is called.</p>
     * <p>This is <b>not</b> called when the scene is un loaded.</p>
     *
     * @param component The component the constraint was removed from.
     */
    void onRemove(Component component);

    /**
     * This method is called every update.
     *
     * @param component The component the constraint is on.
     */
    void update(Component component);
}
