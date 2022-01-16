package org.kakara.engine.ui.canvases;

import org.kakara.engine.GameHandler;
import org.kakara.engine.exceptions.ui.HierarchyException;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.ui.UICanvas;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.components.GeneralUIComponent;
import org.kakara.engine.ui.components.UIComponent;

import java.util.ArrayList;
import java.util.List;

/**
 * The ComponentCanvas is a canvas that holders UIComponents.
 *
 * <p>See {@link UIComponent} and {@link GeneralUIComponent} for more information.</p>
 *
 * <p>Example:</p>
 * <code>
 * ComponentCanvas canvas = new ComponentCanvas(this); <br>
 * Rectangle rectangle = new Rectangle(); <br>
 * canvas.add(rectangle); <br>
 * add(canvas); <br>
 * </code>
 */
public class ComponentCanvas implements UICanvas {
    private final List<UIComponent> components;
    private final Scene scene;
    private boolean init = false;
    private boolean autoScale;

    /*
     * Tagable data
     */
    private List<Object> data;
    private String tag;

    /**
     * Create a new canvas component
     *
     * @param scene The current scene.
     */
    public ComponentCanvas(Scene scene) {
        components = new ArrayList<>();
        this.scene = scene;
    }

    /**
     * Add a child component into the canvas
     *
     * @param component The component to add.
     */
    public void add(UIComponent component) {
        if (component.getParent() != null)
            throw new HierarchyException("Error: That component already has a parent!");
        components.add(component);
        component.setParentCanvas(this);
        if (init) {
            component.init(scene.getUserInterface(), GameHandler.getInstance());
        }
    }

    @Override
    public void init(UserInterface userInterface, GameHandler handler) {
        init = true;
        for (UIComponent c : components) {
            c.init(userInterface, handler);
        }
    }

    @Override
    public void render(UserInterface userInterface, GameHandler handler) {
        for (UIComponent component : components) {
            if (component instanceof GeneralUIComponent)
                ((GeneralUIComponent) component).pollRender(new Vector2(0, 0), userInterface, handler);
            component.render(new Vector2(0, 0), userInterface, handler);
        }
    }

    @Override
    public void cleanup(GameHandler handler) {
        for (UIComponent component : components) {
            component.cleanup(handler);
        }
    }

    @Override
    public boolean isAutoScale() {
        return autoScale;
    }

    @Override
    public void setAutoScale(boolean autoScale) {
        this.autoScale = autoScale;
    }

    /**
     * Get a list of the child components
     *
     * @return The child components
     */
    public List<UIComponent> getComponents() {
        return components;
    }

    /**
     * Clear all of the components
     */
    public void clearComponents() {
        components.clear();
    }

    /**
     * Remove a component from the list.
     *
     * @param c The component
     */
    public void removeComponent(UIComponent c) {
        components.remove(c);
    }

    @Override
    public List<Object> getData() {
        return data;
    }

    @Override
    public void setData(List<Object> data) {
        this.data = data;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }
}
