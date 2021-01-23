package org.kakara.engine.gameitems;

import org.jetbrains.annotations.NotNull;
import org.joml.Quaternionf;
import org.kakara.engine.Camera;
import org.kakara.engine.GameHandler;
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
import java.util.Optional;
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
    private int textPos;

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
    private MeshRenderer meshRenderer;

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
        uuid = UUID.randomUUID();
        textPos = 0;
        tag = "";
        data = new ArrayList<>();
        this.transform = addComponent(Transform.class);
        this.meshRenderer = addComponent(MeshRenderer.class);
        this.meshRenderer.setMesh(meshes);
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

        comp.init(this);
        this.components.add(comp);

        if(component == MeshRenderer.class){
            ItemHandler itemHandler = GameHandler.getInstance().getCurrentScene().getItemHandler();
            assert itemHandler != null;
            if(itemHandler.containsItem(this)){
                itemHandler.removeItem(this);
                this.meshRenderer = (MeshRenderer) comp;
                itemHandler.addItem(this);
            }
        }

        return comp;
    }

    public <T extends Component> T getComponent(Class<T> component){
        List<Component> compLst = components.stream().filter(comp -> comp.getClass() == component)
                .collect(Collectors.toList());
        if(compLst.size() > 0)
            return (T) compLst.get(0);

        return null;
    }

    public <T extends Component> void removeComponent(Class<T> component){
        if(component == Transform.class)
            throw new IllegalArgumentException("Unable to remove required component!");

        getComponent(component).onRemove();

        components.removeIf(comp -> comp.getClass() == component);


        if(component == MeshRenderer.class){
            ItemHandler itemHandler = GameHandler.getInstance().getCurrentScene().getItemHandler();
            assert itemHandler != null;
            if(itemHandler.containsItem(this)){
                itemHandler.removeItem(this);
                this.meshRenderer = null;
                itemHandler.addItem(this);
            }
        }
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

    public Optional<MeshRenderer> getMeshRenderer(){
        return Optional.ofNullable(meshRenderer);
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



    public UUID getUUID() {
        return uuid;
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
