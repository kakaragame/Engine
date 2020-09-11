package org.kakara.engine.renderobjects;

import me.ryandw11.octree.Octree;
import org.kakara.engine.gameitems.MeshGameItem;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.renderobjects.mesh.*;
import org.kakara.engine.renderobjects.renderlayouts.Face;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

/**
 * A singular 16 x 16 x 16 render chunk.
 * <p>This class <b>is</b> thread safe.</p>
 */
public class RenderChunk extends MeshGameItem {
    private RenderMesh mesh;
    private RenderBlock[][][] octChunk;
    private UUID chunkId;

    /**
     * Creates a new render chunk.
     * <p><b>Note: </b> The chunk is not automatically generated. You must call {@link #regenerateChunk(TextureAtlas, MeshType)}
     * for the chunk to be generated.</p>
     *
     * @param blocks The list of blocks
     * @param atlas  The texture atlas to use.
     */
    public RenderChunk(List<RenderBlock> blocks, TextureAtlas atlas) {
        super();
        this.setPosition(new Vector3(0, 0, 0));
        this.octChunk = new RenderBlock[16][16][16];
        for (RenderBlock blck : blocks) {
            blck.setParentChunk(this);
            octChunk[(int) blck.getPosition().x][(int) blck.getPosition().y][(int) blck.getPosition().z] = blck;
        }
        chunkId = UUID.randomUUID();
    }

    /**
     * Add a render block
     * <p>This does not check to see if the block already exists. An error will be thrown if it does.</p>
     * <p>A block cannot be in more than chunk!</p>
     *
     * @param block The render block
     */
    public void addBlock(RenderBlock block) {
        if (block.getParentChunk() != null)
            throw new RuntimeException("Error: This block already has a parent!");
        block.setParentChunk(this);
        octChunk[(int) block.getPosition().x][(int) block.getPosition().y][(int) block.getPosition().z] = block;
    }

    /**
     * Remove a render block
     *
     * @param block The block to remove.
     */
    public void removeBlock(RenderBlock block) {
        octChunk[(int) block.getPosition().x][(int) block.getPosition().y][(int) block.getPosition().z] = null;
        block.setParentChunk(null);
    }

    /**
     * Get the ID of the chunk.
     *
     * @return The id
     */
    public UUID getId() {
        return chunkId;
    }

    /**
     * Get the octree for the render blocks.
     * <p>This is how you would quickly find a block based upon a location.</p>
     * <p>Use this method when trying to find a block with a certain location</p>
     *
     * @return The octree.
     */
    public RenderBlock[][][] getOctChunk() {
        return octChunk;
    }

    public RenderMesh getRenderMesh(){
        return mesh;
    }

    /**
     * Get all of the visible blocks
     *
     * @return The list of visible blocks.
     */
    public List<RenderBlock> calculateVisibleBlocks() {
        List<RenderBlock> output = new ArrayList<>();
        for (int x = 0; x < 16; x++) {
            for(int y = 0; y < 16; y++){
                for(int z = 0; z < 16; z++){
                    RenderBlock block = octChunk[x][y][z];
                    if(block == null) continue;
                    block.clearFaces();
                    boolean found = false;
                    if (z + 1 > 15 || octChunk[x][y][z + 1] == null) {
                        block.addFace(Face.FRONT);
                        output.add(block);
                        found = true;
                    }
                    if (z - 1 < 0 || octChunk[x][y][z - 1] == null) {
                        block.addFace(Face.BACK);
                        if (!found)
                            output.add(block);
                    }
                    if (y + 1 > 15 || octChunk[x][y+1][z] == null) {
                        block.addFace(Face.TOP);
                        if (!found)
                            output.add(block);
                        found = true;
                    }
                    if (y - 1 < 0 || octChunk[x][y - 1][z] == null) {
                        block.addFace(Face.BOTTOM);
                        if (!found)
                            output.add(block);
                        found = true;
                    }
                    if (x + 1 > 15 || octChunk[x + 1][y][z] == null) {
                        block.addFace(Face.RIGHT);
                        if (!found)
                            output.add(block);
                        found = true;
                    }
                    if (x - 1 < 0 || octChunk[x - 1][y][z] == null) {
                        block.addFace(Face.LEFT);
                        if (!found)
                            output.add(block);
                    }
                }
            }
        }
        return output;
    }

    /**
     * Regenerate a Render Chunk.
     * <p>Be sure to check the documentation for {@link SyncMesh}, {@link AsyncMesh}, {@link MultiThreadMesh}, and {@link ModifiedAsyncMesh}
     * to see what thread this method should be called on.</p>
     *
     * @param atlas The texture atlas to use.
     * @param type  The type of mesh you want to use. (Check the documentation for more details).
     * @return The completable future of the mesh. ({@link SyncMesh} does not support this and will return null).
     */
    public CompletableFuture<? extends RenderMesh> regenerateChunk(TextureAtlas atlas, MeshType type) {
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
        mesh.updateOverlay(calculateVisibleBlocks(), atlas);
    }


    @Override
    public void render() {
        if (mesh == null) return;
        mesh.render();
    }

}
