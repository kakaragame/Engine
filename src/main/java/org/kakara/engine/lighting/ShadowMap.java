package org.kakara.engine.lighting;

import org.kakara.engine.exceptions.render.GenericRenderException;
import org.kakara.engine.gameitems.Texture;

import static org.lwjgl.opengl.GL30.*;

/**
 * Handles the shadows of the game.
 */
public class ShadowMap {
    public static final int SHADOW_MAP_WIDTH = 1024;

    public static final int SHADOW_MAP_HEIGHT = 1024;

    private final int depthMapFBO;

    private final Texture depthMap;

    public ShadowMap() throws GenericRenderException {
        // Create a FBO to render the depth map
        depthMapFBO = glGenFramebuffers();

        // Create the depth map texture
        depthMap = new Texture(SHADOW_MAP_WIDTH, SHADOW_MAP_HEIGHT, GL_DEPTH_COMPONENT);

        // Attach the the depth map texture to the FBO
        glBindFramebuffer(GL_FRAMEBUFFER, depthMapFBO);
        glFramebufferTexture2D(GL_FRAMEBUFFER, GL_DEPTH_ATTACHMENT, GL_TEXTURE_2D, depthMap.getId(), 0);
        // Set only depth
        glDrawBuffer(GL_NONE);
        glReadBuffer(GL_NONE);

        if (glCheckFramebufferStatus(GL_FRAMEBUFFER) != GL_FRAMEBUFFER_COMPLETE) {
            throw new GenericRenderException("Could not create FrameBuffer");
        }

        // Unbind
        glBindFramebuffer(GL_FRAMEBUFFER, 0);
    }

    /**
     * Get the texture of the depth mpa
     *
     * @return The texture.
     */
    public Texture getDepthMapTexture() {
        return depthMap;
    }

    /**
     * Get the depth map fbo.
     *
     * @return The fbo.
     */
    public int getDepthMapFBO() {
        return depthMapFBO;
    }

    /**
     * Cleanup the depth map.
     */
    public void cleanup() {
        glDeleteFramebuffers(depthMapFBO);
        depthMap.cleanup();
    }
}
