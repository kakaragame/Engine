package org.kakara.engine.gameitems;


import org.apache.commons.lang3.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.kakara.engine.exceptions.GenericLoadException;
import org.kakara.engine.resources.JarResource;
import org.kakara.engine.resources.Resource;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.utils.Utils;
import org.lwjgl.system.MemoryStack;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL12.GL_CLAMP_TO_EDGE;
import static org.lwjgl.opengl.GL30.glGenerateMipmap;
import static org.lwjgl.stb.STBImage.*;
import static org.lwjgl.system.MemoryStack.stackPush;

/**
 * Handles textures for the Meshes.
 * Do not use this class for RenderChunk Textures. Use {@link org.kakara.engine.renderobjects.RenderTexture}
 *
 * <p>This class is <b>not</b> thread safe.</p>
 */
public class Texture {

    private final int id;

    private final int width;

    private final int height;

    private int numRows = 1;

    private int numCols = 1;

    private Scene scene;

    /**
     * Creates an empty texture.
     *
     * @param width       Width of the texture
     * @param height      Height of the texture
     * @param pixelFormat Specifies the format of the pixel data (GL_RGBA, etc.)
     */
    public Texture(int width, int height, int pixelFormat) {
        this.id = glGenTextures();
        this.width = width;
        this.height = height;
        glBindTexture(GL_TEXTURE_2D, this.id);
        glTexImage2D(GL_TEXTURE_2D, 0, GL_DEPTH_COMPONENT, this.width, this.height, 0, pixelFormat, GL_FLOAT, (ByteBuffer) null);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
    }

    /**
     * Create a texture using a file name.
     * <p>This is primarily used for the particle system.</p>
     *
     * @param fileName     The name of the file.
     * @param numCols      THe number of columns.
     * @param numRows      The number of rows.
     * @param currentScene The current scene.
     * @throws IOException Throws IOException if the file cannot be found.
     */
    public Texture(String fileName, int numCols, int numRows, Scene currentScene) throws IOException {
        this(fileName, currentScene);
        this.numCols = numCols;
        this.numRows = numRows;
    }

    /**
     * Create a texture using a resource.
     * <p>This is primarily used for the particle system.</p>
     *
     * @param fileName     The resource.
     * @param numCols      The number of columns.
     * @param numRows      The number of rows.
     * @param currentScene The current scene.
     */
    public Texture(Resource fileName, int numCols, int numRows, Scene currentScene) {
        this(fileName, currentScene);
        this.numCols = numCols;
        this.numRows = numRows;
    }

    /**
     * Create a new texture using the file name.
     *
     * @param fileName     The file name.
     * @param currentScene The current scene.
     * @throws IOException Throws an IOException if the file cannot be found.
     */
    public Texture(@NotNull String fileName, @NotNull Scene currentScene) throws IOException {
        this(Utils.ioResourceToByteBuffer(fileName, 1024));
        this.scene = currentScene;
    }

    /**
     * Create a new texture using a resource.
     *
     * @param resource     The resource.
     * @param currentScene The current scene.
     */
    public Texture(@NotNull Resource resource, @NotNull Scene currentScene) {
        this.scene = currentScene;

        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer avChannels = stack.mallocInt(1);

            // Decode texture image into a byte buffer

            ByteBuffer decodedImage;
            if (resource instanceof JarResource) {
                decodedImage = stbi_load_from_memory(resource.getByteBuffer(), w, h, avChannels, 4);
            } else {
                // The path needs to be corrected on windows.
                decodedImage = stbi_load(correctPath(resource.getPath()), w, h, avChannels, 4);

            }
            if (decodedImage == null) {
                throw new GenericLoadException("Error: Cannot load specified image. " + stbi_failure_reason());
            }
            this.width = w.get();
            this.height = h.get();

            // Create a new OpenGL texture
            this.id = glGenTextures();
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, this.id);

            // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            // Upload the texture data
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, decodedImage);
            // Generate Mip Map
            glGenerateMipmap(GL_TEXTURE_2D);
        }
    }

    /**
     * Correct the file path on windows.
     *
     * @param path The path.
     * @return The corrected path.
     */
    private String correctPath(String path) {
        // Remove an extra / in front of the drive on windows.
        if (SystemUtils.IS_OS_WINDOWS && path.startsWith("/")) {
            return path.substring(1);
        }
        return path;
    }

    /**
     * Create a texture from a byte buffer.
     *
     * @param imageData The byte buffer.
     */
    public Texture(@NotNull ByteBuffer imageData) {
        try (MemoryStack stack = stackPush()) {
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);
            IntBuffer avChannels = stack.mallocInt(1);

            // Decode texture image into a byte buffer

            ByteBuffer decodedImage = stbi_load_from_memory(imageData, w, h, avChannels, 4);

            if(decodedImage == null){
                throw new GenericLoadException("Error: Cannot load specified image. " + stbi_failure_reason());
            }

            this.width = w.get();
            this.height = h.get();

            // Create a new OpenGL texture
            this.id = glGenTextures();
            // Bind the texture
            glBindTexture(GL_TEXTURE_2D, this.id);

            // Tell OpenGL how to unpack the RGBA bytes. Each component is 1 byte size
            glPixelStorei(GL_UNPACK_ALIGNMENT, 1);

            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST);
            glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST);
            // Upload the texture data
            glTexImage2D(GL_TEXTURE_2D, 0, GL_RGBA, this.width, this.height, 0, GL_RGBA, GL_UNSIGNED_BYTE, decodedImage);
            // Generate Mip Map
            glGenerateMipmap(GL_TEXTURE_2D);

            w.clear();
            h.clear();
            avChannels.clear();
            decodedImage.clear();
        }
    }

    /**
     * Get the number of columns
     * <p>For use with particles</p>
     *
     * @return The number of columns
     */
    public int getNumCols() {
        return numCols;
    }

    /**
     * Get the number of rows.
     * <p>For use with particles</p>
     *
     * @return The number of rows.
     */
    public int getNumRows() {
        return numRows;
    }

    /**
     * Get the width of the texture.
     *
     * @return The width of the texture.
     */
    public int getWidth() {
        return this.width;
    }

    /**
     * Get the height of the texture.
     *
     * @return The height of the texture.
     */
    public int getHeight() {
        return this.height;
    }

    /**
     * Bind the texture.
     *
     * @deprecated This is now handled by the engine.
     */
    @Deprecated
    public void bind() {
        glBindTexture(GL_TEXTURE_2D, id);
    }

    /**
     * Get the id of the texture.
     *
     * @return Get the id of the texture. (OpenGL id)
     */
    public int getId() {
        return id;
    }

    /**
     * Cleanup the textures
     */
    public void cleanup() {
        glDeleteTextures(id);
    }

    /**
     * Grabs the scene this texture is for
     *
     * @return The scene.
     */
    public Scene getCurrentScene() {
        return scene;
    }
}
