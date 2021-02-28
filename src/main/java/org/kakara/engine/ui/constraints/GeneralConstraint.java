package org.kakara.engine.ui.constraints;

import org.jetbrains.annotations.Nullable;
import org.kakara.engine.GameHandler;
import org.kakara.engine.ui.components.UIComponent;
import org.kakara.engine.window.Window;

/**
 * This constraint sets the position of the component relative to another component.
 * <code>
 * button.addConstraint(new GeneralConstraint(ComponentSide.TOP, button2, ComponentSide.Bottom, 5));
 * </code>
 * The code above puts button 5 units below button2.
 *
 * @since 1.0-Pre1
 */
public class GeneralConstraint implements Constraint {

    private final ComponentSide mySide;
    private final UIComponent otherComponent;
    private final ComponentSide otherComponentSide;
    private final float value;

    // The component that this constraint is added to.
    private UIComponent component;

    private Window window;

    /**
     * Apply a general constraint.
     *
     * @param mySide             The side of the component that this constraint is being added to.
     * @param otherComponent     The component to compare to. If this value is null, then it compares itself to the parent.
     * @param otherComponentSide The side of the other component to compare.
     * @param value              The value
     */
    public GeneralConstraint(ComponentSide mySide, @Nullable UIComponent otherComponent, ComponentSide otherComponentSide, float value) {
        this.mySide = mySide;
        this.otherComponent = otherComponent;
        this.otherComponentSide = otherComponentSide;
        this.value = value;
    }


    @Override
    public void onAdd(UIComponent component) {
        this.window = GameHandler.getInstance().getWindow();
        this.component = component;
    }

    @Override
    public void onRemove(UIComponent component) {

    }

    @Override
    public void update(UIComponent component) {
        switch (mySide) {
            case TOP:
                component.setPosition(component.getPosition().x, getSide(component, otherComponent, otherComponentSide) + value);
                break;
            case BOTTOM:
                component.setPosition(component.getPosition().x, getSide(component, otherComponent, otherComponentSide) - component.getScale().y - value);
                break;
            case LEFT:
                component.setPosition(getSide(component, otherComponent, otherComponentSide) + value, component.getPosition().y);
                break;
            case RIGHT:
                component.setPosition(getSide(component, otherComponent, otherComponentSide) - component.getScale().x - value, component.getPosition().y);
                break;
        }
    }


    private float getSide(UIComponent mainComponent, UIComponent otherComponent, ComponentSide side) {
        if (otherComponent != null) {
            switch (side) {
                case TOP:
                    return otherComponent.getPosition().y;
                case BOTTOM:
                    return otherComponent.getPosition().y + otherComponent.getScale().y;
                case LEFT:
                    return otherComponent.getPosition().x;
                case RIGHT:
                    return otherComponent.getPosition().x + otherComponent.getScale().x;
            }
        }
        if (mainComponent.getParent() == null) {
            switch (side) {
                case TOP:
                case LEFT:
                    return 0;
                case BOTTOM:
                    return component.getParentCanvas().isAutoScale() ? 720 : window.getHeight();
                case RIGHT:
                    return component.getParentCanvas().isAutoScale() ? 1080 : window.getWidth();
            }
        }
        switch (side) {
            case TOP:
                return mainComponent.getParent().getPosition().y;
            case BOTTOM:
                return mainComponent.getParent().getPosition().y + mainComponent.getParent().getScale().y;
            case LEFT:
                return mainComponent.getParent().getPosition().x;
            case RIGHT:
                return mainComponent.getParent().getPosition().x + mainComponent.getParent().getScale().x;
        }
        return 0;
    }
}
