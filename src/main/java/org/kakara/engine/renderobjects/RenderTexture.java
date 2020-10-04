package org.kakara.engine.renderobjects;

import org.kakara.engine.resources.Resource;

/**
 * The RenderTexture is similar to the Texture class.
 * This class handles information of block textures.
 * This class also states where the texture is within the texture atlas.
 */
public class RenderTexture {
    private final Resource resource;

    private int id;

    private float xOffset;
    private float yOffset;

    /**
     * Create a render texture from a resource.
     *
     * @param resource The resource
     */
    public RenderTexture(Resource resource) {
        this.resource = resource;
    }

    public Resource getResource() {
        return this.resource;
    }

    /**
     * Initializes the render texture.
     * This is for internal use only.
     *
     * @param id The id
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
     * @return
     */
    public float getXOffset() {
        return xOffset;
    }

    /**
     * Get the offset on the y side.
     *
     * @return
     */
    public float getYOffset() {
        return yOffset;
    }


}
