package org.kakara.engine.voxels;

import org.kakara.engine.resources.Resource;

/**
 * The VoxelTexture is similar to the Texture class.
 * This class handles information of block textures.
 * This class also states where the texture is within the texture atlas.
 */
public class VoxelTexture {
    private final Resource resource;

    private int id;

    private float xOffset;
    private float yOffset;

    /**
     * Create a voxel texture from a resource.
     *
     * @param resource The resource
     */
    public VoxelTexture(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return this.resource;
    }

    /**
     * Initializes the voxel texture.
     * This is for internal use only.
     *
     * @param id      The id
     * @param xOffset the xOffset
     * @param yOffset the yOffset
     */
    public void init(int id, float xOffset, float yOffset) {
        this.id = id;
        this.xOffset = xOffset;
        this.yOffset = yOffset;
    }

    /**
     * Grab the id of the texture.
     *
     * @return The id of the texture.
     */
    public int getId() {
        return id;
    }

    /**
     * Get the offset on the x side.
     *
     * @return The x offset.
     */
    public float getXOffset() {
        return xOffset;
    }

    /**
     * Get the offset on the y side.
     *
     * @return The y offset.
     */
    public float getYOffset() {
        return yOffset;
    }


}
