package org.kakara.engine.item;

import org.kakara.engine.item.mesh.InstancedMesh;
import org.kakara.engine.item.mesh.Mesh;

import java.util.*;

/**
 * Handles all of the items.
 * <p>This class does not manager render chunks. See: {@link org.kakara.engine.renderobjects.ChunkHandler} to add render chunks.</p>
 */
public class ItemHandler {
    private List<GameItem> items;

    private Map<Mesh, List<GameItem>> nonInstancedMeshMap;
    private Map<InstancedMesh, List<GameItem>> instancedMeshMap;

    public ItemHandler() {
        items = new ArrayList<>();
        nonInstancedMeshMap = new HashMap<>();
        instancedMeshMap = new HashMap<>();
    }

    /**
     * Add an item to the item manager.
     * <p>Both instanced and non-instanced meshes are allowed.</p>
     * @param obj The object to be added.
     */
    public void addItem(GameItem obj) {
        Mesh mesh = obj.getMesh();
        if(mesh instanceof InstancedMesh){
            List<GameItem> list = instancedMeshMap.computeIfAbsent((InstancedMesh) mesh, k -> new ArrayList<>());
            list.add(obj);
        }else{
            List<GameItem> list = nonInstancedMeshMap.computeIfAbsent(mesh, k -> new ArrayList<>());
            list.add(obj);
        }
    }

    /**
     * Remove an item from the item manager.
     * <p>Both instanced and non-instanced meshes are allowed.</p>
     * @since 1.0-Pre1
     * @param obj The object to be removed
     */
    public void removeItem(GameItem obj){
        Mesh mesh = obj.getMesh();
        if(mesh instanceof InstancedMesh){
            instancedMeshMap.remove(mesh);
        }
        else{
            nonInstancedMeshMap.remove(obj);
        }
    }

    /**
     * Get the map of non instanced meshes
     * @return The map of non instanced meshes
     */
    public Map<Mesh, List<GameItem>> getNonInstancedMeshMap(){
        return nonInstancedMeshMap;
    }

    /**
     * Get the map of instanced meshes
     * @return The map of instanced meshes
     */
    public Map<InstancedMesh, List<GameItem>> getInstancedMeshMap(){
        return instancedMeshMap;
    }

    /**
     * Get gameobjects with a certain id.
     *
     * @param id The id
     * @return Returns the gameobject. (Returns null if none found).
     */
    public GameItem getItemWithId(UUID id) {
        for (GameItem obj : items) {
            if (obj.getId() == id) return obj;
        }
        return null;
    }

    /**
     * Grab a list of all of the items
     * @return A list of all of the items.
     */
    public List<GameItem> getItems() {
        return items;
    }

    /**
     * Cleanup all of the items.
     * <p>Internal Use Only.</p>
     */
    public void cleanup(){
        for(Mesh m : instancedMeshMap.keySet()){
            for(GameItem gi : instancedMeshMap.get(m)){
                gi.cleanup();
            }
        }

        for(Mesh m : nonInstancedMeshMap.keySet()){
            for(GameItem gi : nonInstancedMeshMap.get(m)){
                gi.cleanup();
            }
        }
    }
}
