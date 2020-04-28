package org.kakara.engine.scene;

import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.kakara.engine.GameHandler;
import org.kakara.engine.collision.Collidable;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.renderobjects.ChunkHandler;
import org.kakara.engine.renderobjects.RenderChunk;
import org.kakara.engine.renderobjects.TextureAtlas;

import java.util.UUID;

/**
 * This scene is to be used for the game.
 * <p>See {@link AbstractMenuScene} for a title scene.</p>
 */
public abstract class AbstractGameScene extends AbstractScene {

    private ChunkHandler chunkHandler;
    private TextureAtlas textureAtlas;

    public AbstractGameScene(GameHandler gameHandler) {
        super(gameHandler);
        this.chunkHandler = new ChunkHandler();
    }

    @Override
    public final void render( ) {
        gameHandler.getGameEngine().getRenderer().render(gameHandler.getWindow(), gameHandler.getCamera(), this);
        if(getSkyBox() != null)
            gameHandler.getGameEngine().getRenderer().renderSkyBox(gameHandler.getWindow(), gameHandler.getCamera(), this);
        hud.render(gameHandler.getWindow());
    }

    /**
     * Set the texture atlas that is to be used for the scene.
     * <p>This <b>must</b> be set in order to use Render Chunks.</p>
     * @param textureAtlas The texture atlas.
     */
    public void setTextureAtlas(TextureAtlas textureAtlas){
        this.textureAtlas = textureAtlas;
    }

    /**
     * Get the chunk handler.
     * <p>This manages the RenderChunks</p>
     * @return The chunk handler.
     */
    public ChunkHandler getChunkHandler(){
        return chunkHandler;
    }

    /**
     * Get the current texture atlas.
     * @return The texture atlas (Null if none).
     */
    public TextureAtlas getTextureAtlas(){
        return textureAtlas;
    }

    /**
     * Call this method to select a game item.
     * <p>This method also works with RenderBlocks as well with instanced and non-instanced game items</p>
     * <p>The maximum distance is set to 20 for performance reasons.</p>
     * @param distance The maximum distance that a block can be selected for.
     *                 <p>Note: This value is limited by the maximum distance set in {@link org.kakara.engine.collision.CollisionManager#getSelectionItems(Vector3)}</p>
     * @return The collidable that was found.
     */
    public Collidable selectGameItems(float distance){
        Collidable selectedGameItem = null;
        float closestDistance = distance;

        Vector3f dir = new Vector3f();

        dir = getCamera().getViewMatrix().positiveZ(dir).negate();

        Vector3f max = new Vector3f();
        Vector3f min = new Vector3f();
        Vector2f nearFar = new Vector2f();

        for(Collidable collidable : gameHandler.getCollisionManager().getSelectionItems(getCamera().getPosition())){
            collidable.setSelected(false);
            min.set(collidable.getColPosition().toJoml());
            max.set(collidable.getColPosition().toJoml());
            min.add(-collidable.getColScale()/2, -collidable.getColScale()/2, -collidable.getColScale()/2);
            max.add(collidable.getColScale()/2, collidable.getColScale()/2, collidable.getColScale()/2);
            if (Intersectionf.intersectRayAab(getCamera().getPosition().toJoml(), dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                selectedGameItem = collidable;
            }
        }

        if(selectedGameItem != null){
            selectedGameItem.setSelected(true);
        }
        return selectedGameItem;
    }

    /**
     * Add a chunk to the scene
     * <p>This does the same as {@link org.kakara.engine.renderobjects.ChunkHandler#addChunk(RenderChunk)}</p>
     * @since 1.0-Pre1
     * @param chunk The chunk to add
     */
    public void add(RenderChunk chunk){
        chunkHandler.addChunk(chunk);
    }

    /**
     * Remove a chunk from the scene
     * <p>This does the same as {@link org.kakara.engine.renderobjects.ChunkHandler#addChunk(RenderChunk)} and {@link #removeChunk(UUID)}</p>
     * @since 1.0-Pre1
     * @param chunk The chunk to remove.
     */
    public void remove(RenderChunk chunk){
        chunkHandler.removeChunk(chunk.getId());
    }

    /**
     * Remove a chunk from the scene.
     * <p>This does the same as {@link org.kakara.engine.renderobjects.ChunkHandler#addChunk(RenderChunk)} and {@link #remove(RenderChunk)}</p>
     * @since 1.0-Pre1
     * @param chunkId The chunk id to remove.
     */
    public void removeChunk(UUID chunkId){
        chunkHandler.removeChunk(chunkId);
    }

    @Override
    public void unload() {
        getItemHandler().cleanup();
    }
}
