package org.kakara.engine.renderobjects;

import org.jetbrains.annotations.Nullable;
import org.kakara.engine.physics.collision.Collidable;
import org.kakara.engine.physics.collision.Collider;
import org.kakara.engine.physics.collision.ObjectBoxCollider;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.renderobjects.mesh.MeshType;
import org.kakara.engine.renderobjects.renderlayouts.BlockLayout;
import org.kakara.engine.renderobjects.renderlayouts.Face;
import org.kakara.engine.renderobjects.renderlayouts.Layout;

import java.util.*;

/**
 * The individual blocks of the chunk.
 * <p>Any changes to the RenderBlock requires your to run {@link RenderChunk#regenerateChunk(TextureAtlas, MeshType)} in
 * order for the changes to be shown.</p>
 * <p>This class <b>is</b> thread safe.</p>
 */
public class RenderBlock implements Collidable {

    private Layout layout;
    private RenderTexture texture;
    private RenderTexture overlay;

    private Vector3 position;

    private RenderChunk parentChunk;
    private List<Face> visibleFaces;
    private boolean selected;

    private Collider collider;

    /**
     * Create a render block.
     * @param layout The layout to use
     * @param texture The texture to use.
     * @param position The position of the block. (Must be 0-16 for x, y, and z).
     */
    public RenderBlock(Layout layout, RenderTexture texture, Vector3 position){
        this.layout = layout;
        this.texture = texture;
        this.position = position;
        this.visibleFaces = new ArrayList<>();
        this.selected = false;
        collider = new ObjectBoxCollider(true);
        collider.onRegister(this);
    }

    public RenderBlock(RenderTexture texture, Vector3 position){
        this(new BlockLayout(), texture, position);
    }

    /**
     * Set the position of the block
     * @param position the position
     */
    public void setPosition(Vector3 position){
        this.position = position.clone();
    }

    /**
     * Get the list of visible faces.
     * @return The list of visible faces.
     */
    public List<Face> getVisibleFaces(){
        return this.visibleFaces;
    }

    /**
     * Get the position.
     * @return The position.
     */
    public Vector3 getPosition(){
        return position.clone();
    }

    /**
     * Get the parent chunk.
     * @return The parent chunk.
     */
    public RenderChunk getParentChunk(){
        return this.parentChunk;
    }

    /**
     * Set the parent chunk
     * @param chunk The parent chunk
     */
    protected void setParentChunk(RenderChunk chunk){
        this.parentChunk = chunk;
    }

    /**
     * Get the render texture
     * @return The render texture.
     */
    public RenderTexture getTexture(){
        return texture;
    }

    /**
     * Get the layout that is used.
     * @return The layout.
     */
    public Layout getLayout(){
        return layout;
    }

    /**
     * Add a face to the visible face list.
     * @param f The face to add
     */
    public void addFace(Face f){
        visibleFaces.add(f);
    }

    /**
     * Remove all faces from the visible face list.
     */
    public void clearFaces(){
        visibleFaces.clear();
    }

    /**
     * Add an overlay render texture.
     * @since 1.0-Pre2
     * @param texture The overlay render texture.
     */
    public void setOverlay(@Nullable RenderTexture texture){
        this.overlay = texture;
    }

    /**
     * Get the overlay render texture.
     * @since 1.0-Pre2
     * @return The overlay render texture.
     */
    public RenderTexture getOverlay(){
        return this.overlay;
    }

    /**
     * Get the vertex array from the visible faces
     * @param vertex The list to append the vertexes to.
     */
    public void getVertexFromFaces(List<Float> vertex){
        for(Face f : this.visibleFaces){
            List<Float> temp;
            switch(f){
                case FRONT:
                    temp = layout.getVertex(getPosition()).getFront();
                    break;
                case BACK:
                    temp = layout.getVertex(getPosition()).getBack();
                    break;
                case TOP:
                    temp = layout.getVertex(getPosition()).getTop();
                    break;
                case BOTTOM:
                    temp = layout.getVertex(getPosition()).getBottom();
                    break;
                case LEFT:
                    temp = layout.getVertex(getPosition()).getLeft();
                    break;
                case RIGHT:
                    temp = layout.getVertex(getPosition()).getRight();
                    break;
                default:
                    temp = new LinkedList<>();
                    break;
            }
            vertex.addAll(temp);
        }
    }

    /**
     * Get the texture coord array from the visible faces
     * @param vertex The list to append the textures to.
     * @param atlas The texture atlas
     */
    public void getTextureFromFaces(List<Float> vertex, TextureAtlas atlas){
        for(Face f : this.visibleFaces){
            List<Float> temp;
            switch(f){
                case FRONT:
                    temp = layout.getTextureCords().getFront(getTexture().getXOffset(), getTexture().getYOffset(), atlas.getNumberOfRows());
                    break;
                case BACK:
                    temp = layout.getTextureCords().getBack(getTexture().getXOffset(), getTexture().getYOffset(), atlas.getNumberOfRows());
                    break;
                case TOP:
                    temp = layout.getTextureCords().getTop(getTexture().getXOffset(), getTexture().getYOffset(), atlas.getNumberOfRows());
                    break;
                case BOTTOM:
                    temp = layout.getTextureCords().getBottom(getTexture().getXOffset(), getTexture().getYOffset(), atlas.getNumberOfRows());
                    break;
                case LEFT:
                    temp = layout.getTextureCords().getLeft(getTexture().getXOffset(), getTexture().getYOffset(), atlas.getNumberOfRows());
                    break;
                case RIGHT:
                    temp = layout.getTextureCords().getRight(getTexture().getXOffset(), getTexture().getYOffset(), atlas.getNumberOfRows());
                    break;
                default:
                    temp = new LinkedList<>();
                    break;
            }
            vertex.addAll(temp);
        }
    }

    /**
     * Get the overlay texture coordinates of the render blocks.
     * @param vertex The list of overlay textures to append to.
     * @param atlas The texture atlas.
     */
    public void getOverlayFromFaces(List<Float> vertex, TextureAtlas atlas){
        if(getOverlay() == null){
            vertex.addAll(Collections.nCopies(4 * 2 * this.visibleFaces.size(), 0f));
            return;
        }
        for(Face f : this.visibleFaces){
            if(getOverlay() == null){
                vertex.addAll(Arrays.asList(0f, 0f, 0f, 0f, 0f, 0f, 0f, 0f));
                continue;
            }
            List<Float> temp;
            switch(f){
                case FRONT:
                    temp = layout.getTextureCords().getFront(getOverlay().getXOffset(), getOverlay().getYOffset(), atlas.getNumberOfRows());
                    break;
                case BACK:
                    temp = layout.getTextureCords().getBack(getOverlay().getXOffset(), getOverlay().getYOffset(), atlas.getNumberOfRows());
                    break;
                case TOP:
                    temp = layout.getTextureCords().getTop(getOverlay().getXOffset(), getOverlay().getYOffset(), atlas.getNumberOfRows());
                    break;
                case BOTTOM:
                    temp = layout.getTextureCords().getBottom(getOverlay().getXOffset(), getOverlay().getYOffset(), atlas.getNumberOfRows());
                    break;
                case LEFT:
                    temp = layout.getTextureCords().getLeft(getOverlay().getXOffset(), getOverlay().getYOffset(), atlas.getNumberOfRows());
                    break;
                case RIGHT:
                    temp = layout.getTextureCords().getRight(getOverlay().getXOffset(), getOverlay().getYOffset(), atlas.getNumberOfRows());
                    break;
                default:
                    temp = new LinkedList<>();
                    break;
            }
            vertex.addAll(temp);
        }
    }

    /**
     * Get the normal array from the visible faces
     * @param vertex This list to append the normals to.
     */
    public void getNormalsFromFaces(List<Float> vertex){
        for(Face f : this.visibleFaces){
            List<Float> temp;
            switch(f){
                case FRONT:
                    temp = layout.getNormal().getFront();
                    break;
                case BACK:
                    temp = layout.getNormal().getBack();
                    break;
                case TOP:
                    temp = layout.getNormal().getTop();
                    break;
                case BOTTOM:
                    temp = layout.getNormal().getBottom();
                    break;
                case LEFT:
                    temp = layout.getNormal().getLeft();
                    break;
                case RIGHT:
                    temp = layout.getNormal().getRight();
                    break;
                default:
                    temp = new LinkedList<>();
                    break;
            }
            vertex.addAll(temp);
        }
    }

    /**
     * Get the indices array from the visible faces.
     * @param currentIndex The current index. (Starting number of the indices)
     * @param vertex The list to append the indices to.
     */
    public void getIndicesFromFaces(List<Integer> vertex, int currentIndex){
        int index = currentIndex;
        for(Face f : this.visibleFaces){
            List<Integer> temp;
            switch(f){
                case FRONT:
                    temp = layout.getIndices().getFront(index);
                    break;
                case BACK:
                    temp = layout.getIndices().getBack(index);
                    break;
                case TOP:
                    temp = layout.getIndices().getTop(index);
                    break;
                case BOTTOM:
                    temp = layout.getIndices().getBottom(index);
                    break;
                case LEFT:
                    temp = layout.getIndices().getLeft(index);
                    break;
                case RIGHT:
                    temp = layout.getIndices().getRight(index);
                    break;
                default:
                    temp = new LinkedList<>();
                    break;
            }
            //Increase the current index by five.
            index += 4;
            vertex.addAll(temp);
        }
    }

    @Override
    public String toString(){
        return "{RenderBlock: " + position.x + ", " + position.y + ", " + position.z + "}";
    }


    @Override
    public final Vector3 getColPosition() {
        return getPosition().clone().add(parentChunk.getPosition().clone());
    }

    @Override
    public float getColScale() {
        return 1;
    }

    @Override
    public void setCollider(Collider collider) {
        this.collider = collider;
        collider.onRegister(this);
    }

    @Override
    public void removeCollider() {
        this.collider = null;
    }

    @Override
    public void colTranslateBy(Vector3 vec) {
        this.position = this.position.add(vec);
    }

    @Override
    public void setColPosition(Vector3 vec) {
        setPosition(vec.clone());
    }

    @Override
    public Collider getCollider() {
        return collider;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public void setSelected(boolean selected) {
        this.selected = selected;
    }
}
