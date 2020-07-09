package org.kakara.engine.gui;

import org.kakara.engine.resources.JarResource;
import org.kakara.engine.resources.Resource;
import org.lwjgl.system.MemoryStack;

import java.nio.ByteBuffer;
import java.nio.IntBuffer;

import static org.lwjgl.stb.STBImage.*;

/**
 * This class stores window icons.
 * @since 1.0-Pre2
 */
public class WindowIcon {
    private ByteBuffer image;
    private int width;
    private int height;

    /**
     * Create a window icon object.
     * @param resource The resource pointing to the desired image.
     */
    public WindowIcon(Resource resource){
        try(MemoryStack stack = MemoryStack.stackPush()) {
            IntBuffer comp = stack.mallocInt(1);
            IntBuffer w = stack.mallocInt(1);
            IntBuffer h = stack.mallocInt(1);

            if(resource instanceof JarResource) {
                image = stbi_load_from_memory(resource.getByteBuffer(), w, h, comp, 4);
            }else{
                image = stbi_load(resource.getPath(), w, h, comp, 4);
            }
            if (image == null) {
                throw new RuntimeException("Cannot load icon image! Error: " + stbi_failure_reason());
            }
            this.width = w.get();
            this.height = h.get();
        }
    }

    /**
     * Get the width of the image.
     * @return The width.
     */
    public int getWidth(){
        return width;
    }

    /**
     * Get the height of the image.
     * @return The height.
     */
    public int getHeight(){
        return height;
    }

    /**
     * Get the bytebuffer for the image.
     * @return The bytebuffer.
     */
    public ByteBuffer getImage(){
        return image;
    }

}
