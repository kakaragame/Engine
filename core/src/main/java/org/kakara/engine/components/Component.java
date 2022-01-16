package org.kakara.engine.components;

import org.kakara.engine.exceptions.UsedComponentException;
import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.physics.collision.ColliderComponent;

/**
 * Components are what give GameItems functionality. This class is the parent class
 * for all components.
 *
 * <p>Components do not have a Constructor. Information instead needs to be passed in via setters.</p>
 *
 * <p>Please see the documentation for a detailed guide on creating components.</p>
 *
 * <p>Some built-in components have special functionality. See the
 * <a href="https://docs.kakara.org/engine/getting-started-1/components">documentation</a> for more information.</p>
 */
public abstract class Component {

    private GameItem gameItem;

    /**
     * This method is called when a component is added to a GameItem.
     */
    public abstract void start();

    /**
     * This method is called on each update.
     */
    public abstract void update();

    /**
     * This method is called on each physics update.
     * <p>Graphical processes cannot be done here. Please use this for physics calculation only.</p>
     *
     * @param deltaTime The delta time for the physics update.
     */
    public void physicsUpdate(float deltaTime) {
    }

    /**
     * This is called when a component is removed, the scene closed, or the game ends.
     */
    public void cleanup() {
    }

    /**
     * This is called when a component is removed.
     */
    public void onRemove() {
    }

    /**
     * This is called when a collision is detected.
     *
     * @param other The other component.
     */
    public void onCollision(ColliderComponent other) {
    }

    /**
     * This initializes the Component.
     * <p>This is to be called by the engine when the component is added to a GameItem.</p>
     *
     * @param item The item added.
     */
    public final void init(GameItem item) {
        if (gameItem != null)
            throw new UsedComponentException("This component already has a GameItem!");
        this.gameItem = item;

        afterInit();
    }

    /**
     * This is called directly after init.
     */
    public void afterInit() {
    }

    /**
     * Get a component from the parent game item.
     *
     * @param component The component to get.
     * @param <T>       The type of component to get.
     * @return The component.
     */
    public final <T extends Component> T getComponent(Class<T> component) {
        return gameItem.getComponent(component);
    }

    /**
     * Add a component to the parent game item.
     *
     * @param component The component to add.
     * @param <T>       The type of component to add.
     * @return The component.
     */
    public final <T extends Component> T addComponent(Class<T> component) {
        return gameItem.addComponent(component);
    }

    /**
     * Check if a component exists on the parent game item.
     *
     * @param component The component to add.
     * @param <T>       The type of component to check.
     * @return If the component exists on the parent.
     */
    public final <T extends Component> boolean hasComponent(Class<T> component) {
        return gameItem.hasComponent(component);
    }

    /**
     * Get the parent game item.
     *
     * @return The parent game item.
     */
    public final GameItem getGameItem() {
        return gameItem;
    }

    /**
     * Get the transform of the parent game item.
     *
     * @return The transform of the parent game item.
     */
    public final Transform getTransform() {
        return gameItem.getTransform();
    }
}
