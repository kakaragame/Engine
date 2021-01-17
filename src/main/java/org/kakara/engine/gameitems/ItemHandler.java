package org.kakara.engine.gameitems;

import org.kakara.engine.gameitems.features.Feature;
import org.kakara.engine.gameitems.mesh.IMesh;
import org.kakara.engine.gameitems.mesh.InstancedMesh;
import org.kakara.engine.gameitems.mesh.Mesh;

import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Handles all of the items.
 * <p>This class does not manager render chunks. See: {@link org.kakara.engine.renderobjects.ChunkHandler} to add render chunks.</p>
 */
public class ItemHandler {
    private final List<GameItem> items;

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
        IMesh mesh = obj.getMesh();
        if (mesh instanceof InstancedMesh) {
            List<GameItem> list = instancedMeshMap.computeIfAbsent((InstancedMesh) mesh, k -> new ArrayList<>());
            list.add(obj);
        } else {
            List<GameItem> list = nonInstancedMeshMap.computeIfAbsent(mesh, k -> new ArrayList<>());
            list.add(obj);
        }
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
        IMesh mesh = obj.getMesh();
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
                if (item.getMesh() instanceof InstancedMesh) {
                    instancedMeshMap.get(item.getMesh()).remove(item);
                } else {
                    nonInstancedMeshMap.get(item.getMesh()).remove(item);
                }
                items.remove(item);
            }
        }
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
     * Update features within game items.
     * <p>Internal use only.</p>
     */
    public void update() {
        for (GameItem item : items) {
            for (Feature feature : item.getFeatures()) {
                feature.update(item);
            }
        }
    }

    /**
     * Cleanup all of the items.
     * <p>Internal Use Only.</p>
     */
    public void cleanup() {
        for (Map.Entry<InstancedMesh, List<GameItem>> m : instancedMeshMap.entrySet()) {
            for (GameItem gi : m.getValue()) {
                gi.cleanup();
            }
        }

        for (Map.Entry<IMesh, List<GameItem>> m : nonInstancedMeshMap.entrySet()) {
            for (GameItem gi : m.getValue()) {
                gi.cleanup();
            }
        }
    }
}
