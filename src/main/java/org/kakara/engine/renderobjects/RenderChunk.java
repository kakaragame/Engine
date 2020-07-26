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

    private List<RenderBlock> blocks;
    private RenderMesh mesh;
    private Octree<RenderBlock> octChunk;
    private UUID chunkId;

    /**
     * Creates a new render chunk.
     * <p><b>Note: </b> The chunk is not automatically generated. You must call {@link #regenerateChunk(TextureAtlas, MeshType)}
     * for the chunk to be generated.</p>
     * @param blocks The list of blocks
     * @param atlas The texture atlas to use.
     */
    public RenderChunk(List<RenderBlock> blocks, TextureAtlas atlas){
        super();
        this.setPosition(new Vector3(0, 0, 0));
        this.octChunk = new Octree<>(0,0,0,17,17,17);
        this.blocks = blocks;
        for(RenderBlock blck : blocks){
            blck.setParentChunk(this);
            octChunk.insert((int)blck.getPosition().x, (int)blck.getPosition().y, (int)blck.getPosition().z, blck);
        }
        chunkId = UUID.randomUUID();
    }

    /**
     * Get the list of blocks.
     * @return The list of blocks.
     */
    public List<RenderBlock> getBlocks(){
        return blocks;
    }

    /**
     * Add a render block
     * <p>This does not check to see if the block already exists. An error will be thrown if it does.</p>
     * <p>A block cannot be in more than chunk!</p>
     * @param block The render block
     */
    public void addBlock(RenderBlock block){
        if(block.getParentChunk() != null)
            throw new RuntimeException("Error: This block already has a parent!");
        block.setParentChunk(this);
        blocks.add(block);
        octChunk.insert(Math.round(block.getPosition().x), Math.round(block.getPosition().y), Math.round(block.getPosition().z), block);
    }

    /**
     * Remove a render block
     * @param block The block to remove.
     */
    public void removeBlock(RenderBlock block){
        blocks.remove(block);
        octChunk.remove((int)block.getPosition().x, (int)block.getPosition().y, (int)block.getPosition().z);
        block.setParentChunk(null);
    }

    /**
     * Get the ID of the chunk.
     * @return The id
     */
    public UUID getId(){
        return chunkId;
    }

    /**
     * Get the octree for the render blocks.
     * <p>This is how you would quickly find a block based upon a location.</p>
     * <p>Use this method when trying to find a block with a certain location DO NOT USE {@link #getBlocks()}</p>
     * @return The octree.
     */
    public Octree<RenderBlock> getOctChunk(){
        return octChunk;
    }

    /**
     * Get all of the visible blocks
     * @param blocks The list of blocks to work with
     * @return The list of visible blocks.
     */
    public List<RenderBlock> calculateVisibleBlocks(List<RenderBlock> blocks){
        List<RenderBlock> output = new ArrayList<>();
        for(RenderBlock block : blocks){
            Vector3 pos = block.getPosition();
            int x = (int) pos.x;
            int y = (int) pos.y;
            int z = (int) pos.z;
            block.clearFaces();
            boolean found = false;
            if(!octChunk.find(x, y, z + 1)) {
                block.addFace(Face.FRONT);
                output.add(block);
                found = true;
            }
            if(!octChunk.find(x, y, z - 1)) {
                block.addFace(Face.BACK);
                if(!found)
                    output.add(block);
            }
            if(!octChunk.find(x, y + 1, z)) {
                block.addFace(Face.TOP);
                if(!found)
                    output.add(block);
                found = true;
            }
            if(!octChunk.find(x, y -1, z)) {
                block.addFace(Face.BOTTOM);
                if(!found)
                    output.add(block);
                found = true;
            }
            if(!octChunk.find(x + 1, y, z)) {
                block.addFace(Face.RIGHT);
                if(!found)
                    output.add(block);
                found = true;
            }
            if(!octChunk.find(x - 1, y, z)) {
                block.addFace(Face.LEFT);
                if(!found)
                    output.add(block);
            }
        }
        return output;
    }

    /**
     * Regenerate a Render Chunk.
     * <p>Be sure to check the documentation for {@link SyncMesh}, {@link AsyncMesh}, {@link MultiThreadMesh}, and {@link ModifiedAsyncMesh}
     * to see what thread this method should be called on.</p>
     * @param atlas The texture atlas to use.
     * @param type The type of mesh you want to use. (Check the documentation for more details).
     * @return The completable future of the mesh. ({@link SyncMesh} does not support this and will return null).
     */
    public CompletableFuture<? extends RenderMesh> regenerateChunk(TextureAtlas atlas, MeshType type){
        switch(type){
            case SYNC:
                if(mesh != null)
                    mesh.cleanUp();
                this.mesh = new SyncMesh(blocks, this, atlas);
                break;
            case ASYNC:
                CompletableFuture<AsyncMesh> asyncFuture = new CompletableFuture<>();
                AsyncMesh m = new AsyncMesh(blocks, this, atlas, asyncFuture);
                asyncFuture.thenAccept(newmesh -> {
                    if(mesh != null)
                        mesh.cleanUp();
                    mesh = newmesh;
                });
                return asyncFuture;
            case MULTITHREAD:
                CompletableFuture<MultiThreadMesh> multiFuture = new CompletableFuture<>();
                MultiThreadMesh ma = new MultiThreadMesh(blocks, this, atlas, multiFuture);
                multiFuture.thenAccept(newmesh -> {
                    if(mesh != null)
                        mesh.cleanUp();
                    mesh = newmesh;
                });
                return multiFuture;
            case MODIFEDASYNC:
                CompletableFuture<ModifiedAsyncMesh> modifedFuture = new CompletableFuture<>();
                if(mesh != null)
                    mesh.cleanUp();
                this.mesh = new ModifiedAsyncMesh(blocks, this, atlas, modifedFuture);
                return modifedFuture;
        }
        return null;
    }

    /**
     * Regenerate the overlay textures.
     * <p>The method is thread safe.</p>
     * @param atlas The texture atlas.
     */
    public void regenerateOverlayTextures(TextureAtlas atlas){
        mesh.updateOverlay(calculateVisibleBlocks(blocks), atlas);
    }


    @Override
    public void render(){
        if(mesh == null) return;
        mesh.render();
    }

}
