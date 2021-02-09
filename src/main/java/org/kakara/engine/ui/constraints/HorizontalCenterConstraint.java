package org.kakara.engine.ui.constraints;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.components.UIComponent;
import org.kakara.engine.window.Window;

/**
 * Used to horizontally center the component.
 *
 * @since 1.0-Pre1
 */
public class HorizontalCenterConstraint implements Constraint {

    private Window window;
    private UserInterface userInterface;
    private final float offset;

    public HorizontalCenterConstraint() {
        this(0);
    }

    /**
     * Horizontally center a component
     *
     * @param offset The offset.
     */
    public HorizontalCenterConstraint(float offset) {
        this.offset = offset;
    }

    @Override
    public void onAdd(UIComponent component) {
        window = GameHandler.getInstance().getGameEngine().getWindow();
        userInterface = GameHandler.getInstance().getCurrentScene().getUserInterface();
    }

    @Override
    public void onRemove(UIComponent component) {

    }

    @Override
    public void update(UIComponent component) {
        Vector2 scale = component.getParent() != null ? component.getParent().getScale()
                : getWindowSize();
        component.setPosition((scale.x / 2 - component.getScale().x / 2) + offset, component.getPosition().y);
    }

    private Vector2 getWindowSize() {
        return userInterface.isAutoScaled() ? new Vector2(window.initalWidth, window.initalHeight)
                : new Vector2(window.getWidth(), window.getHeight());
    }
}
