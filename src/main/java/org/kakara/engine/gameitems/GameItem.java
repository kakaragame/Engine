package org.kakara.engine.gameitems;

import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.kakara.engine.Camera;
import org.kakara.engine.components.Component;
import org.kakara.engine.components.MeshRenderer;
import org.kakara.engine.components.Transform;
import org.kakara.engine.gameitems.features.Feature;
import org.kakara.engine.gameitems.mesh.IMesh;
import org.kakara.engine.gameitems.mesh.Mesh;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.physics.collision.ColliderComponent;
import org.kakara.engine.properties.Tagable;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * The most basic implementation of the GameItem.
 * <p>
 * This is a Collidable GameItem. That uses meshes to create an item
 * </p>
 */
public class GameItem implements Tagable {
    private final UUID uuid;
    private final List<Feature> features = new ArrayList<>();
    private IMesh[] meshes;
    private int textPos;
    private boolean visible = true;

    /*
        The physics section.
     */

    /*
        The tagable section
     */
    private String tag;
    private List<Object> data;

    /*
        Components
     */
    private final List<Component> components = new ArrayList<>();
    public final Transform transform;

    public GameItem() {
        uuid = UUID.randomUUID();
        textPos = 0;
        tag = "";
        data = new ArrayList<>();
        this.transform = addComponent(Transform.class);
    }

    public GameItem(IMesh mesh) {
        this(new IMesh[]{mesh});
    }

    public GameItem(IMesh[] meshes) {
        this.meshes = meshes;
        uuid = UUID.randomUUID();
        textPos = 0;
        tag = "";
        data = new ArrayList<>();
        this.transform = addComponent(Transform.class);
        addComponent(MeshRenderer.class).setMesh(meshes);
    }

    public <T extends Component> T addComponent(Class<T> component) {
        if(components.stream().anyMatch(comp -> comp.getClass() == component))
            throw new RuntimeException("This game item already has that component!");
        T comp;
        try {
            comp = (T) component.getConstructors()[0].newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | IndexOutOfBoundsException e) {
            throw new RuntimeException("Cannot add illegal component!");
        }
        this.components.add(comp);

        return comp;
    }

    public <T extends Component> T getComponent(Class<T> component){
        List<Component> compLst = components.stream().filter(comp -> comp.getClass() == component)
                .collect(Collectors.toList());
        if(compLst.size() > 0)
            return (T) compLst.get(0);

        return null;
    }

    public boolean hasComponent(Class<Component> component){
        return components.stream().anyMatch(comp -> comp.getClass() == component);
    }

    public List<Component> getComponents(){
        return components;
    }

    public Transform getTransform(){
        return transform;
    }

    /**
     * Get the mesh of the object
     * <p>If there is more than one mesh, than the first one is returned.</p>
     *
     * @return The mesh
     */
    public IMesh getMesh() {
        if (meshes.length == 0) return null;
        return meshes[0];
    }

    public void setMesh(@NotNull Mesh mesh) {
        this.meshes = new Mesh[]{mesh};
    }

    public int getTextPos() {
        return this.textPos;
    }

    public void setTextPos(int pos) {
        this.textPos = pos;
    }

    public List<Feature> getFeatures() {
        return features;
    }

    public void addFeature(Feature feature) {
        features.add(feature);
        feature.updateValues(this);
    }

    /**
     * Get all of the meshes of the object.
     *
     * @return The array of meshes.
     */
    public IMesh[] getMeshes() {
        return meshes;
    }

    /**
     * Set the array of meshes.
     *
     * @param meshes The array of meshes
     */
    public void setMeshes(@NotNull Mesh[] meshes) {
        this.meshes = meshes;
    }



    public UUID getUUID() {
        return uuid;
    }

    /**
     * Render the item.
     * <p>Internal Use Only</p>
     */
    public void render() {
        if (isVisible()) {
            for (IMesh mesh : meshes) {
                mesh.render();
            }
        }
    }

    /**
     * Cleanup the item.
     * <p>Internal Use Only</p>
     */
    public void cleanup() {
        int numMeshes = this.meshes != null ? this.meshes.length : 0;
        for (int i = 0; i < numMeshes; i++) {
            this.meshes[i].cleanUp();
        }
    }


    /**
     * A safe way to clone a gameobject.
     *
     * @param exact If you want it to be an exact copy.
     * @return The clone of the gameobject.
     */
    public GameItem clone(boolean exact) {
        GameItem clone = new GameItem(this.meshes);
        if (exact) {
            clone.transform.setPosition(transform.getPosition().x, transform.getPosition().y, transform.getPosition().z);
            clone.transform.setRotation(transform.getRotation());
            clone.transform.setScale(transform.getScale());
        }
        return clone;
    }

    public boolean isVisible() {
        return visible;
    }

    /**
     * Sets weather the object is visible. The Collision Engine will continue to work
     *
     * @param visible is the item visible
     */
    public void setVisible(boolean visible) {
        this.visible = visible;
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
