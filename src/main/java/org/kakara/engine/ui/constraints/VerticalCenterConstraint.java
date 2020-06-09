package org.kakara.engine.ui.constraints;

import org.kakara.engine.GameHandler;
import org.kakara.engine.gui.Window;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.components.Component;

/**
 * Vertically center the component.
 * @since 1.0-Pre1
 */
public class VerticalCenterConstraint implements Constraint {

    private Window window;
    private float offset;

    public VerticalCenterConstraint(){
        this(0);
    }

    /**
     * Vertically constrain the component
     * @param offset The offset.
     */
    public VerticalCenterConstraint(float offset){
        this.offset = offset;
    }

    @Override
    public void onAdd(Component component) {
        window = GameHandler.getInstance().getGameEngine().getWindow();
    }

    @Override
    public void onRemove(Component component) {}

    @Override
    public void update(Component component){
        Vector2 scale = component.getParent() != null ? component.getParent().getScale()
                : new Vector2(window.initalWidth, window.initalHeight);
        component.setPosition(component.getPosition().x, (scale.y/2 - component.getScale().y/2) + offset);
    }
}
