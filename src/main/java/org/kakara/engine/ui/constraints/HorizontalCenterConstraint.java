package org.kakara.engine.ui.constraints;

import org.kakara.engine.GameHandler;
import org.kakara.engine.gui.Window;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.components.Component;

/**
 * Used to horizontally center the component.
 * @since 1.0-Pre1
 */
public class HorizontalCenterConstraint implements Constraint {

    private Window window;
    private float offset;

    public HorizontalCenterConstraint(){
        this(0);
    }

    /**
     * Horizontally center a component
     * @param offset The offset.
     */
    public HorizontalCenterConstraint(float offset){
        this.offset = offset;
    }

    @Override
    public void onAdd(Component component) {
        window = GameHandler.getInstance().getGameEngine().getWindow();
    }

    @Override
    public void onRemove(Component component) {

    }

    @Override
    public void update(Component component){
        Vector2 scale = component.getParent() != null ? component.getParent().getScale()
                : new Vector2(window.initalWidth, window.initalHeight);
        component.setPosition((scale.x/2 - component.getScale().x/2) + offset, component.getPosition().y);
    }
}
