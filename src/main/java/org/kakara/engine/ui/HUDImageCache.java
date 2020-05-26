package org.kakara.engine.ui;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.nanovg.NanoVG.nvgDeleteImage;

/**
 * Caches the images that way memory can be cleared up when the image is no longer being used.
 * <p>Primarily Internal Use Only</p>
 * @deprecated Replaced by internal cleanup calls.
 */
public class HUDImageCache {
    private List<Integer> image;
    private HUD hud;
    public HUDImageCache(HUD hud){
        image = new ArrayList<>();
        this.hud = hud;
    }

    /**
     * Add an image to the cache
     * @param id The image it to add.
     */
    public void addImage(int id){
        this.image.add(id);
    }

    /**
     * Remove an image from the cache
     * @param id The image id to remove
     */
    public void removeImage(int id){
        this.image.remove(id);
    }

    /**
     * Cleanup all of the images.
     */
    public void cleanup(){
        for(int i : image){
            nvgDeleteImage(hud.getVG(), i);
        }
    }
}
