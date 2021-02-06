package org.kakara.engine.ui.items;

import org.kakara.engine.GameHandler;
import org.kakara.engine.exceptions.ui.HierarchyException;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.ui.UICanvas;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.components.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Holds all of the components.
 * <p>This is what is added directly to the hud.</p>
 */
public class ComponentCanvas implements UICanvas {
    boolean init = false;
    private final List<Component> components;
    private final Scene scene;
    /*
     * Tagable data
     */
    private List<Object> data;
    private String tag;
    private boolean autoScaled = true;

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
    public void add(Component component) {
        if (component.getParent() != null)
            throw new HierarchyException("Error: That component already has a parent!");
        component.setCanvas(this);
        components.add(component);
        if (init) {
            component.init(scene.getUserInterface(), GameHandler.getInstance());
        }
    }

    @Override
    public void init(UserInterface userInterface, GameHandler handler) {
        init = true;
        for (Component c : components) {
            c.init(userInterface, handler);
        }
    }

    @Override
    public void render(UserInterface userInterface, GameHandler handler) {
        for (Component component : components) {
            component.render(new Vector2(0, 0), userInterface, handler);
        }
    }

    @Override
    public void cleanup(GameHandler handler) {
        for (Component component : components) {
            component.cleanup(handler);
        }
    }

    @Override
    public boolean isAutoScaled() {
        return autoScaled;
    }

    public void setAutoScaled(boolean autoScaled) {
        this.autoScaled = autoScaled;
    }

    /**
     * Get a list of the child components
     *
     * @return The child components
     */
    public List<Component> getComponents() {
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
    public void removeComponent(Component c) {
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
