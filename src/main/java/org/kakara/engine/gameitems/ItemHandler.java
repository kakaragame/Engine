package org.kakara.engine.gameitems;

import org.kakara.engine.GameEngine;
import org.kakara.engine.components.Component;
import org.kakara.engine.gameitems.features.Feature;
import org.kakara.engine.gameitems.mesh.IMesh;
import org.kakara.engine.gameitems.mesh.InstancedMesh;
import org.kakara.engine.gameitems.mesh.NullMesh;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Handles all of the items.
 * <p>This class does not manager render chunks. See: {@link org.kakara.engine.voxels.ChunkHandler} to add render chunks.</p>
 * <p>Please note that {@link CopyOnWriteArrayList} is used to store the items, allowing for the addition of items on separate threads.
 * Due to this, making large additions to the ItemHandler often is performance heavy and could cause FPS drop. Please
 * keep this in mind when designing your code.</p>
 */
public class ItemHandler {
    private final List<GameItem> items;

    private final NullMesh nullMesh = new NullMesh();

    private final Map<IMesh, List<GameItem>> nonInstancedMeshMap;
    private final Map<InstancedMesh, List<GameItem>> instancedMeshMap;

    public ItemHandler() {
        items = new CopyOnWriteArrayList<>();
        nonInstancedMeshMap = new HashMap<>();
        instancedMeshMap = new HashMap<>();
    }

    /**
     * Add an item to the item manager.
     * <p>Both instanced and non-instanced meshes are allowed.</p>
     *
     * @param obj The object to be added.
     */
    public void addItem(GameItem obj) {
        IMesh mesh;
        if (obj.getMeshRenderer().isPresent())
            mesh = obj.getMeshRenderer().get().getMesh();
        else
            mesh = nullMesh;
        List<GameItem> list;
        if (mesh instanceof InstancedMesh) {
            list = instancedMeshMap.computeIfAbsent((InstancedMesh) mesh, k -> new CopyOnWriteArrayList<>());
        } else {
            list = nonInstancedMeshMap.computeIfAbsent(mesh, k -> new CopyOnWriteArrayList<>());
        }
        list.add(obj);
        items.add(obj);
    }

    /**
     * Remove an item from the item manager.
     * <p>Both instanced and non-instanced meshes are allowed.</p>
     *
     * @param obj The object to be removed
     * @since 1.0-Pre1
     */
    public void removeItem(GameItem obj) {
        IMesh mesh;
        if (obj.getMeshRenderer().isPresent())
            mesh = obj.getMeshRenderer().get().getMesh();
        else
            mesh = new NullMesh();

        if (mesh instanceof InstancedMesh) {
            instancedMeshMap.get(mesh).remove(obj);
        } else {
            nonInstancedMeshMap.get(mesh).remove(obj);
        }
        items.remove(obj);
    }

    /**
     * Remove items with a certain tag.
     *
     * @param tag The tag to remove.
     * @since 1.0-Pre3
     */
    public void removeItemWithTag(String tag) {
        for (GameItem item : new ArrayList<>(items)) {
            if (item.getTag().equals(tag)) {
                IMesh mesh;
                if (item.getMeshRenderer().isPresent())
                    mesh = item.getMeshRenderer().get().getMesh();
                else
                    mesh = new NullMesh();
                if (mesh instanceof InstancedMesh) {
                    instancedMeshMap.get(mesh).remove(item);
                } else {
                    nonInstancedMeshMap.get(mesh).remove(item);
                }
                items.remove(item);
            }
        }
    }

    public boolean containsItem(GameItem item) {
        return items.contains(item);
    }

    /**
     * Get the map of non instanced meshes
     *
     * @return The map of non instanced meshes
     */
    public Map<IMesh, List<GameItem>> getNonInstancedMeshMap() {
        return nonInstancedMeshMap;
    }

    /**
     * Get the map of instanced meshes
     *
     * @return The map of instanced meshes
     */
    public Map<InstancedMesh, List<GameItem>> getInstancedMeshMap() {
        return instancedMeshMap;
    }

    /**
     * Get game items with a certain id.
     *
     * @param id The id
     * @return Returns the game item.
     */
    public Optional<GameItem> getItemWithId(UUID id) {
        for (GameItem obj : items) {
            if (obj.getUUID() == id) return Optional.of(obj);
        }
        return Optional.empty();
    }

    /**
     * Grab a list of all of the items
     *
     * @return A list of all of the items.
     */
    public List<GameItem> getItems() {
        return items;
    }

    /**
     * Grab game items based upon the provided tag(s).
     *
     * @param tag A list of tags.
     * @return A list of game items with the specified tag.
     * @since 1.0-Pre3
     */
    public List<GameItem> getItemsWithTag(String... tag) {
        List<String> desiredTags = new ArrayList<>(Arrays.asList(tag));
        List<GameItem> output = new ArrayList<>();
        for (GameItem item : items) {
            if (desiredTags.contains(item.getTag())) {
                output.add(item);
            }
        }
        return output;
    }

    /**
     * Update features within game items. (Features and Components).
     * <p>Internal use only.</p>
     */
    public void update() {
        for (GameItem item : items) {
            for (Feature feature : item.getFeatures()) {
                try {
                    feature.update(item);
                } catch (Exception e) {
                    GameEngine.LOGGER.error("Unable to run feature " + feature.getClass().getName() + ". In item " + item.toString(), e);
                }
            }
            for (Component component : item.getComponents()) {
                try {
                    component.update();
                } catch (Exception e) {
                    GameEngine.LOGGER.error("Unable to run component " + component.getClass().getName() + ". In item " + item.toString(), e);
                }
            }
        }

    }

    /**
     * Cleanup all of the items.
     * <p>Internal Use Only.</p>
     */
    public void cleanup() {
        for(GameItem item : items){
            for(Component component : item.getComponents()){
                component.cleanup();
            }
        }
    }
}
