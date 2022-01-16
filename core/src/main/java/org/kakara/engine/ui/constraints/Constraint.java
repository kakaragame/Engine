package org.kakara.engine.ui.constraints;

import org.kakara.engine.ui.components.GeneralUIComponent;
import org.kakara.engine.ui.components.UIComponent;

/**
 * This interface allows for the creation of constraints.
 *
 * <p>Constraints allow the system to set the position or scale of the UIComponent based upon conditions.</p>
 *
 * @since 1.0-Pre1
 */
public interface Constraint {
    /**
     * When the constraint is added to the component
     *
     * @param component The component it was added to
     */
    void onAdd(UIComponent component);

    /**
     * When the constraint is removed from the component.
     * <p>This is only fired when the {@link GeneralUIComponent#removeConstraint(Class)} method is called.</p>
     * <p>This is <b>not</b> called when the scene is un loaded.</p>
     *
     * @param component The component the constraint was removed from.
     */
    void onRemove(UIComponent component);

    /**
     * This method is called every update.
     *
     * @param component The component the constraint is on.
     */
    void update(UIComponent component);
}
