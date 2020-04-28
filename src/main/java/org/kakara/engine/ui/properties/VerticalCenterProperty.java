package org.kakara.engine.ui.properties;

import org.kakara.engine.GameHandler;
import org.kakara.engine.gui.Window;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.components.Component;

/**
 * Vertically center your component.
 * @since 1.0-Pre1
 */
public class VerticalCenterProperty implements ComponentProperty {

    private Window window;

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
        component.setPosition(component.getPosition().x, scale.y/2 - component.getScale().y/2);
    }
}
