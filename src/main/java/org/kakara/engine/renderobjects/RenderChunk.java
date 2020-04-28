package org.kakara.engine.renderobjects;

import me.ryandw11.octree.Octree;
import org.kakara.engine.item.MeshGameItem;
import org.kakara.engine.math.Vector3;
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
     * <p><b>Note: </b> The chunk is not automatically generated. You must call {@link #regenerateChunk(TextureAtlas)} or
     * {@link #regenerateChunkAsync(TextureAtlas)} for the chunk to be generated.</p>
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
     * Generate the mesh of a chunk. This method must be called every time something in the chunk
     * is updated.
     * <p>For the asynchronous version of this method see {@link #regenerateChunkAsync(TextureAtlas)}</p>
     * <p>This method is <b>not</b> thread safe.</p>
     * @param atlas
     */
    public void regenerateChunk(TextureAtlas atlas){
        if(mesh != null){
            mesh.cleanUp();
        }
        List<RenderBlock> visBlocks = calculateVisibleBlocks(blocks);
        this.mesh = new RenderMesh(visBlocks, atlas, false);
    }

    /**
     * Generate the mesh of a chunk. This method must be called every time something in the chunk
     * is updated.
     * <p>The old mesh will not be deleted and cleaned up until the rendering of the mesh is completed. (Can take several frames).</p>
     * <p>For the synchronous version of this method see {@link #regenerateChunk(TextureAtlas)}</p>
     * <p>This method <b>is</b> thread safe.</p>
     * @param atlas The texture atlas
     * @return The completable future that can be used to run code after a render mesh is done generating.
     */
    public CompletableFuture<RenderMesh> regenerateChunkAsync(TextureAtlas atlas){
        List<RenderBlock> visBlocks = calculateVisibleBlocks(blocks);
        CompletableFuture<RenderMesh> completableFuture = new CompletableFuture<>();
        new RenderMesh(visBlocks, atlas, true, completableFuture);
        completableFuture.thenAccept(newmesh -> {
            if(mesh != null)
                mesh.cleanUp();
            mesh = newmesh;
        });
        return completableFuture;
    }


    @Override
    public void render(){
        if(mesh == null) return;
        mesh.render();
    }

}
