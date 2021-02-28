package org.kakara.engine.voxels;

import org.kakara.engine.math.KMath;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.physics.collision.ColliderComponent;
import org.kakara.engine.physics.collision.VoxelCollider;
import org.kakara.engine.scene.AbstractGameScene;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Handles the voxel chunks for the system. In order to use the Voxel System you must
 * use the {@link org.kakara.engine.scene.AbstractGameScene}.
 *
 * <p>Access this through {@link AbstractGameScene#getChunkHandler()} or use the general use #add() method.</p>
 */
public class ChunkHandler {
    public static final ExecutorService EXECUTORS = Executors.newFixedThreadPool(2);
    private final List<VoxelChunk> voxelChunkList;

    public ChunkHandler() {
        voxelChunkList = new ArrayList<>();
    }

    /**
     * Add a voxel chunk to the chunk list.
     *
     * @param chunk The chunk to add.
     */
    public void addChunk(VoxelChunk chunk) {
        voxelChunkList.add(chunk);
    }

    /**
     * Remove a voxel chunk using its chunk id.
     *
     * @param chunkId The id of the chunk. (chunk.getId()).
     */
    public void removeChunk(UUID chunkId) {
        int ind = -1;
        for (VoxelChunk rc : voxelChunkList) {
            if (rc.getId() == chunkId) {
                ind = voxelChunkList.indexOf(rc);
                break;
            }
        }
        if (ind != -1) {
            voxelChunkList.remove(ind);
        }
    }

    /**
     * Get a list of the chunk collisions.
     * <p>For performance reasons only the blocks around the position provided are returned.</p>
     *
     * @param position The position where the collider is to check around.
     * @return The list of colliders.
     */
    public List<ColliderComponent> getChunkCollisions(Vector3 position) {
        Vector3 pos = new Vector3((int) Math.floor(position.x), (int) Math.floor(position.y), (int) Math.floor(position.z));
        List<ColliderComponent> collisionList = new ArrayList<>();
        for (VoxelChunk chunk : new ArrayList<>(voxelChunkList)) {
            if (chunk == null) continue;
            if (KMath.distance(0, chunk.transform.getPosition().y, 0, 0, pos.y, 0) > 16) continue;
            if (KMath.distance(chunk.transform.getPosition().x, 0, 0, pos.x, 0, 0) < 17
                    && KMath.distance(0, 0, chunk.transform.getPosition().z, 0, 0, pos.z) < 17) {
                for (int x = -1; x < 2; x++) {
                    for (int y = -2; y < 3; y++) {
                        for (int z = -1; z < 2; z++) {
                            Vector3 coords = coordsToRenderCoords(chunk.transform.getPosition(), pos.add(x, y, z));
                            Voxel blck;
                            // This might be a bad idea.
                            try {
                                blck = chunk.getVoxelArray()[(int) coords.x][(int) coords.y][(int) coords.z];
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                continue;
                            }
                            if (blck != null)
                                collisionList.add(blck.getCollider());
                        }
                    }
                }
            }
        }
        return collisionList;
    }

    /**
     * Get a list of the chunk selections
     * <p>This is separate from the method above for performance reasons.</p>
     *
     * @param position The position where the selector is to check around.
     * @return The list of ColliderComponents {@link VoxelCollider}.
     */
    public List<ColliderComponent> getChunkSelections(Vector3 position) {
        Vector3 pos = new Vector3((int) Math.floor(position.x), (int) Math.floor(position.y), (int) Math.floor(position.z));
        List<ColliderComponent> collisionList = new ArrayList<>();
        for (VoxelChunk chunk : new ArrayList<>(voxelChunkList)) {
            if (KMath.distance(0, chunk.transform.getPosition().y, 0, 0, pos.y, 0) > 17) continue;
            if (KMath.distance(chunk.transform.getPosition().x, 0, chunk.transform.getPosition().z, pos.x, 0, pos.z) < 25) {
                for (int x = -10; x < 10; x++) {
                    for (int y = -10; y < 10; y++) {
                        for (int z = -10; z < 10; z++) {
                            Vector3 coords = coordsToRenderCoords(chunk.transform.getPosition(), pos.add(x, y, z));
                            Voxel blck;
                            // This might also be a bad idea.
                            try {
                                blck = chunk.getVoxelArray()[(int) coords.x][(int) coords.y][(int) coords.z];
                            } catch (ArrayIndexOutOfBoundsException ex) {
                                continue;
                            }
                            if (blck != null)
                                collisionList.add(blck.getCollider());
                        }
                    }
                }
            }
        }
        return collisionList;
    }

    /**
     * Get the list of render chunks
     *
     * @return THe list of render chunks
     */
    public List<VoxelChunk> getVoxelChunkList() {
        return new ArrayList<>(voxelChunkList);
    }

    /**
     * Remove all chunks from the render list.
     */
    public void removeAll() {
        for (VoxelChunk rc : voxelChunkList) {
            rc.cleanup();
        }
        voxelChunkList.clear();
    }

    /**
     * Convert normal positions to render positions (Which can only be 0-16 for x, y, and z).
     *
     * @param chunkpos The chunk position
     * @param input    The input position (Mutated)
     * @return The render position.
     */
    public Vector3 coordsToRenderCoords(Vector3 chunkpos, Vector3 input) {
        // Mutate the input
        return input.subtractMut(chunkpos);
    }


}
