package org.kakara.engine.ui.constraints;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UICanvas;
import org.kakara.engine.ui.components.UIComponent;
import org.kakara.engine.window.Window;

/**
 * Vertically center the component.
 *
 * @since 1.0-Pre1
 */
public class VerticalCenterConstraint implements Constraint {

    private Window window;
    private final float offset;
    private UIComponent component;

    public VerticalCenterConstraint() {
        this(0);
    }

    /**
     * Vertically constrain the component
     *
     * @param offset The offset.
     */
    public VerticalCenterConstraint(float offset) {
        this.offset = offset;
    }

    @Override
    public void onAdd(UIComponent component) {
        window = GameHandler.getInstance().getGameEngine().getWindow();
        this.component = component;
    }

    @Override
    public void onRemove(UIComponent component) {
    }

    @Override
    public void update(UIComponent component) {
        Vector2 scale = component.getParent() != null ? component.getParent().getScale()
                : getWindowSize();
        component.setPosition(component.getPosition().x, (scale.y / 2 - component.getScale().y / 2) + offset);
    }

    private Vector2 getWindowSize() {
        return component.getParentCanvas().isAutoScale() ? new Vector2(window.initialWidth, window.initialHeight)
                : new Vector2(window.getWidth(), window.getHeight());
    }
}
