package org.kakara.engine.gameitems;

import org.kakara.engine.GameHandler;
import org.kakara.engine.components.Component;
import org.kakara.engine.components.MeshRenderer;
import org.kakara.engine.components.Transform;
import org.kakara.engine.gameitems.features.Feature;
import org.kakara.engine.gameitems.mesh.IMesh;
import org.kakara.engine.properties.Identifiable;
import org.kakara.engine.properties.Tagable;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * GameItems are the basic building block of the engine. They are used to create objects
 * that can be seen in a game.
 * <p>
 * GameItems on their own are nothing. Components are what give GameItems their functionality.
 * All GameItems have a transform component, which contains the position, rotation, and scale of
 * the item.
 * <br>
 * Common components have their own getters or public fields for easy access ({@link #getTransform()},
 * {@link #getMeshRenderer()}).
 * <br>
 * Custom components can be made by extending {@link Component}. Components are then added using
 * {@link #addComponent(Class)}. Please see the documentation for more information on making
 * custom components.
 * </p>
 * <h3>Examples:</h3>
 * <code>
 * GameItem item = new GameItem(exampleMesh); <br>
 * item.transform.setPosition(10, 10, 10);<br>
 * item.addComponent(ObjectBoxCollider.class);<br>
 * item.addComponent(PhysicsItem.class);
 * </code>
 * <code>
 * GameItem item = new GameItem();
 * </code>
 */
public class GameItem implements Tagable, Identifiable {
    public final Transform transform;
    // The UUID of the GameItem.
    private final UUID uuid;
    // The feature list.
    private final List<Feature> features = new ArrayList<>();
    /*
        Components
     */
    private final List<Component> components = new ArrayList<>();
    private String tag;
    private List<Object> data;
    // This stores the texture position if a sprite sheet is used.
    private int textPos;
    private MeshRenderer meshRenderer;

    /**
     * Construct a GameItem with the default constructor.
     *
     * <p>This GameItem will only have a Transform component.</p>
     */
    public GameItem() {
        uuid = UUID.randomUUID();
        textPos = 0;
        tag = "";
        data = new ArrayList<>();
        this.transform = addComponent(Transform.class);
    }

    /**
     * Construct a GameItem with a mesh.
     * <p>The MeshRenderer component will automatically be added.</p>
     *
     * @param mesh The mesh to add.
     */
    public GameItem(IMesh mesh) {
        this(new IMesh[]{mesh});
    }

    /**
     * Construct a GameItem with an array of meshes.
     * <p>The MeshRenderer component will automatically be added.</p>
     *
     * @param meshes The meshes to add.
     */
    public GameItem(IMesh[] meshes) {
        uuid = UUID.randomUUID();
        textPos = 0;
        tag = "";
        data = new ArrayList<>();
        this.transform = addComponent(Transform.class);
        this.meshRenderer = addComponent(MeshRenderer.class);
        this.meshRenderer.setMesh(meshes);
    }

    /**
     * Add a component to the game item.
     * <p>
     * Example:
     * <code>
     * PhysicsComponent pi = gameItem.addComponent(PhysicsComponent.class);
     * </code>
     *
     * <p>Please note that some built-in components have special behavior.
     * See the documentation for more information.</p>
     *
     * @param component The component to add.
     * @param <T>       The type of component.
     * @return The instance of the added component.
     */
    public <T extends Component> T addComponent(Class<T> component) {
        if (components.stream().anyMatch(comp -> comp.getClass() == component))
            throw new RuntimeException("This game item already has that component!");
        T comp;
        try {
            comp = (T) component.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IndexOutOfBoundsException e) {
            throw new RuntimeException("Cannot add illegal component!");
        }

        comp.init(this);
        this.components.add(comp);

        if (component == MeshRenderer.class) {
            ItemHandler itemHandler = GameHandler.getInstance().getCurrentScene().getItemHandler();
            assert itemHandler != null;
            if (itemHandler.containsItem(this)) {
                itemHandler.removeItem(this);
                this.meshRenderer = (MeshRenderer) comp;
                itemHandler.addItem(this);
            }
        }
        comp.start();
        return comp;
    }

    /**
     * Get a component from the GameItem.
     *
     * <p>Example:</p>
     * <code>
     * PhysicsComponent pc = gameItem.getComponent(PhysicsComponent.class);
     * </code>
     *
     * <p>You can also use a parent class: </p>
     * <code>
     * ColliderComponent cc = gameItem.getComponent(ColliderComponent.class);
     * </code>
     *
     * @param component The component to obtain.
     * @param <T>       The type of component to obtain.
     * @return The instance of the component. (Returns null if not found.)
     */
    public <T extends Component> T getComponent(Class<T> component) {
        List<Component> compLst = components.stream().filter(comp -> component.isAssignableFrom(comp.getClass()))
                .collect(Collectors.toList());
        if (compLst.size() > 0)
            return (T) compLst.get(0);

        return null;
    }

    /**
     * Remove a component from the GameItem.
     * <p>Example:</p>
     * <code>
     * gameItem.removeComponent(PhysicsComponent.class);
     * </code>
     * <p>Please note that some built-in components have special behavior. Required components,
     * like the Transform component, cannot be removed.</p>
     *
     * <p>Parent classes cannot be used to remove components.</p>
     *
     * @param component The component to remove.
     * @param <T>       The type of component to remove.
     */
    public <T extends Component> void removeComponent(Class<T> component) {
        if (component == Transform.class)
            throw new IllegalArgumentException("Unable to remove required component!");

        getComponent(component).onRemove();

        components.removeIf(comp -> comp.getClass() == component);


        if (component == MeshRenderer.class) {
            ItemHandler itemHandler = GameHandler.getInstance().getCurrentScene().getItemHandler();
            assert itemHandler != null;
            if (itemHandler.containsItem(this)) {
                itemHandler.removeItem(this);
                this.meshRenderer = null;
                itemHandler.addItem(this);
            }
        }
    }

    /**
     * Check to see if a component exists.
     * <p>This does not work with parent classes.</p>
     *
     * @param component The component to find.
     * @param <T>       The type of component to find.
     * @return If the component was found.
     */
    public <T extends Component> boolean hasComponent(Class<T> component) {
        return components.stream().anyMatch(comp -> comp.getClass() == component);
    }

    /**
     * Get the list of components.
     *
     * @return An read-only list of components.
     */
    public List<Component> getComponents() {
        return Collections.unmodifiableList(components);
    }

    /**
     * Get the transform component.
     * <p>This is the same as using the public transform field. There is no difference or preference.</p>
     *
     * @return The transform component.
     */
    public Transform getTransform() {
        return transform;
    }

    /**
     * Get the mesh renderer for the GameItem.
     *
     * @return An optional (possibly) containing the MeshRenderer component.
     */
    public Optional<MeshRenderer> getMeshRenderer() {
        return Optional.ofNullable(meshRenderer);
    }

    /**
     * Get the texture position of the GameItem on a sprite sheet.
     * <p>
     * TODO Place this on the Material class instead?
     *
     * @return The texture position.
     */
    public int getTextPos() {
        return this.textPos;
    }

    /**
     * Set the texture position.
     * <p>
     * TODO Place this on the Material class instead?
     *
     * @param pos The texture position.
     */
    public void setTextPos(int pos) {
        this.textPos = pos;
    }

    /**
     * Get the list of features.
     *
     * @return The list of features.
     */
    public List<Feature> getFeatures() {
        return features;
    }

    /**
     * Add a feature to this GameItem.
     * <p>Consider using a {@link Component} instead of a feature.</p>
     *
     * @param feature The feature to add.
     */
    public void addFeature(Feature feature) {
        features.add(feature);
        feature.updateValues(this);
    }


    /**
     * A safe way to clone a gameobject.
     *
     * @param exact If you want it to be an exact copy.
     * @return The clone of the gameobject.
     */
    public GameItem clone(boolean exact) {
        GameItem clone = new GameItem(this.meshRenderer.getMeshes());
        if (exact) {
            clone.transform.setPosition(transform.getPosition().x, transform.getPosition().y, transform.getPosition().z);
            clone.transform.setRotation(transform.getRotation());
            clone.transform.setScale(transform.getScale());
        }
        return clone;
    }

    @Override
    public UUID getUUID() {
        return uuid;
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

    @Override
    public String toString() {
        return "GameItem{" +
                "uuid=" + uuid +
                ", tag='" + tag + '\'' +
                ", textPos=" + textPos +
                '}';
    }
}
