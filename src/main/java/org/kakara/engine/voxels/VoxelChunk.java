package org.kakara.engine.voxels;

import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.voxels.layouts.Face;
import org.kakara.engine.voxels.mesh.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A VoxelChunk is a 16 x 16 x 16 section of Voxels. Voxel Chunks are a single mesh so that
 * Voxels can be effectively rendered at a good FPS by OpenGL. Voxel Chunks are a performant
 * way to handles Voxels.
 *
 * <p>This class <b>is</b> thread safe.</p>
 */
public class VoxelChunk extends GameItem {
    private VoxelMesh mesh;
    private final Voxel[][][] voxelArray;
    private final UUID chunkId;
    private int voxelCount = 0;

    /**
     * Creates a new voxel chunk.
     * <p><b>Note: </b> The chunk is not automatically generated. You must call {@link #regenerateChunk(TextureAtlas, MeshType)}
     * for the chunk to be generated.</p>
     *
     * @param voxels The list of blocks
     * @param atlas  The texture atlas to use.
     * @deprecated There is not use for this constructor. Use {@link #VoxelChunk(List)} instead.
     */
    @Deprecated
    public VoxelChunk(List<Voxel> voxels, TextureAtlas atlas) {
        this(voxels);
    }

    /**
     * Creates a new voxel chunk.
     * <p><b>Note: </b> The chunk is not automatically generated. You must call {@link #regenerateChunk(TextureAtlas, MeshType)}
     * for the chunk to be generated.</p>
     *
     * @param voxels The list of blocks
     */
    public VoxelChunk(List<Voxel> voxels) {
        super();
        this.transform.setPosition(new Vector3(0, 0, 0));
        this.voxelArray = new Voxel[16][16][16];
        for (Voxel blck : voxels) {
            blck.setParentChunk(this);
            voxelArray[(int) blck.getPosition().x][(int) blck.getPosition().y][(int) blck.getPosition().z] = blck;
        }
        chunkId = UUID.randomUUID();
        voxelCount = voxels.size();
    }

    /**
     * Add a voxel to the chunk.
     * <p>This does not check to see if the block already exists. An error will be thrown if it does.</p>
     * <p>A voxel cannot be in more than chunk!</p>
     *
     * @param voxel The voxel to add.
     */
    public void addVoxel(Voxel voxel) {
        if (voxel.getParentChunk() != null)
            throw new RuntimeException("Error: This block already has a parent!");
        voxel.setParentChunk(this);
        if (voxelArray[(int) voxel.getPosition().x][(int) voxel.getPosition().y][(int) voxel.getPosition().z] == null)
            voxelCount++;
        voxelArray[(int) voxel.getPosition().x][(int) voxel.getPosition().y][(int) voxel.getPosition().z] = voxel;

    }

    /**
     * Remove a voxel from the chunk.
     *
     * @param voxel The voxel to remove.
     */
    public void removeVoxel(Voxel voxel) {
        voxelArray[(int) voxel.getPosition().x][(int) voxel.getPosition().y][(int) voxel.getPosition().z] = null;
        voxel.setParentChunk(null);
        voxelCount--;
    }

    /**
     * Get the ID of the chunk.
     *
     * @return The id of the chunk.
     */
    public UUID getId() {
        return chunkId;
    }

    /**
     * Get the 3D array for the voxel chunk.
     *
     * @return The 3D array containing the voxels.
     */
    public Voxel[][][] getVoxelArray() {
        return voxelArray;
    }

    public VoxelMesh getVoxelMesh() {
        return mesh;
    }

    /**
     * Get all of the visible voxels.
     *
     * @return The list of visible voxels.
     */
    public List<Voxel> calculateVisibleVoxels() {
        List<Voxel> output = new ArrayList<>();
        for (int x = 0; x < 16; x++) {
            for (int y = 0; y < 16; y++) {
                for (int z = 0; z < 16; z++) {
                    Voxel voxel = voxelArray[x][y][z];
                    if (voxel == null) continue;
                    voxel.clearFaces();
                    boolean found = false;

                    // Remove && block.isOpaque() to see other faces.
                    if (z + 1 > 15 || voxelArray[x][y][z + 1] == null || (!voxelArray[x][y][z + 1].isOpaque() && voxel.isOpaque())) {
                        voxel.addFace(Face.FRONT);
                        output.add(voxel);
                        found = true;
                    }
                    if (z - 1 < 0 || voxelArray[x][y][z - 1] == null || !voxelArray[x][y][z - 1].isOpaque() && voxel.isOpaque()) {
                        voxel.addFace(Face.BACK);
                        if (!found)
                            output.add(voxel);
                    }
                    if (y + 1 > 15 || voxelArray[x][y + 1][z] == null || !voxelArray[x][y + 1][z].isOpaque() && voxel.isOpaque()) {
                        voxel.addFace(Face.TOP);
                        if (!found)
                            output.add(voxel);
                        found = true;
                    }
                    if (y - 1 < 0 || voxelArray[x][y - 1][z] == null || !voxelArray[x][y - 1][z].isOpaque() && voxel.isOpaque()) {
                        voxel.addFace(Face.BOTTOM);
                        if (!found)
                            output.add(voxel);
                        found = true;
                    }
                    if (x + 1 > 15 || voxelArray[x + 1][y][z] == null || !voxelArray[x + 1][y][z].isOpaque() && voxel.isOpaque()) {
                        voxel.addFace(Face.RIGHT);
                        if (!found)
                            output.add(voxel);
                        found = true;
                    }
                    if (x - 1 < 0 || voxelArray[x - 1][y][z] == null || !voxelArray[x - 1][y][z].isOpaque() && voxel.isOpaque()) {
                        voxel.addFace(Face.LEFT);
                        if (!found)
                            output.add(voxel);
                    }
                }
            }
        }
        return output;
    }

    /**
     * Regenerate a Voxel Chunk.
     * <p>Be sure to check the documentation for {@link SyncMesh}, {@link AsyncMesh}, {@link MultiThreadMesh}, and {@link ModifiedAsyncMesh}
     * to see what thread this method should be called on.</p>
     *
     * @param atlas The texture atlas to use.
     * @param type  The type of mesh you want to use. (Check the documentation for more details).
     * @return The completable future of the mesh. ({@link SyncMesh} does not support this and will return null).
     */
    public CompletableFuture<? extends VoxelMesh> regenerateChunk(TextureAtlas atlas, MeshType type) {
        switch (type) {
            case SYNC:
                if (mesh != null)
                    mesh.cleanUp();
                this.mesh = new SyncMesh(this, atlas);
                break;
            case ASYNC:
                CompletableFuture<AsyncMesh> asyncFuture = new CompletableFuture<>();
                AsyncMesh m = new AsyncMesh(this, atlas, asyncFuture);
                asyncFuture.thenAccept(newmesh -> {
                    if (mesh != null)
                        mesh.cleanUp();
                    mesh = newmesh;
                });
                return asyncFuture;
            case MULTITHREAD:
                CompletableFuture<MultiThreadMesh> multiFuture = new CompletableFuture<>();
                MultiThreadMesh ma = new MultiThreadMesh(this, atlas, multiFuture);
                multiFuture.thenAccept(newmesh -> {
                    if (mesh != null)
                        mesh.cleanUp();
                    mesh = newmesh;
                });
                return multiFuture;
            case MODIFEDASYNC:
                CompletableFuture<ModifiedAsyncMesh> modifedFuture = new CompletableFuture<>();
                if (mesh != null)
                    mesh.cleanUp();
                this.mesh = new ModifiedAsyncMesh(this, atlas, modifedFuture);
                return modifedFuture;
        }
        return null;
    }

    /**
     * Regenerate the overlay textures.
     * <p>The method is thread safe.</p>
     *
     * @param atlas The texture atlas.
     */
    public void regenerateOverlayTextures(TextureAtlas atlas) {
        mesh.updateOverlay(calculateVisibleVoxels(), atlas);
    }

    /**
     * Get the number of blocks stored in the render chunk.
     *
     * @return The number of blocks stored.
     */
    public int getVoxelCount() {
        return voxelCount;
    }


    public void render() {
        if (mesh == null) return;
        mesh.render();
    }


    public void cleanup() {
        mesh.cleanUp();
    }
}
