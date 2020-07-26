package org.kakara.engine.ui;

import org.kakara.engine.GameHandler;
import org.kakara.engine.window.Window;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.ui.components.Component;
import org.kakara.engine.ui.text.Font;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Handles the HUD.
 * This class is per scene.
 */
public class HUD {

    private long vg;

    private List<HUDItem> hudItems;
    private List<Font> fonts;

    private HUDImageCache imageCache;
    private Scene scene;

    public HUD(Scene scene){
        hudItems = new ArrayList<>();
        fonts = new ArrayList<>();
        imageCache = new HUDImageCache(this);
        this.scene = scene;
    }

    /**
     * Internal Use Only
     */
    public void init(Window window) throws Exception{
        this.vg = window.getOptions().isAntialiasing() ? nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES) : nvgCreate(NVG_STENCIL_STROKES);

        if(this.vg == NULL){
            throw new Exception("Could not init hud");
        }

        for(HUDItem it : hudItems){
            it.init(this, GameHandler.getInstance());
        }
        for(Font ft : fonts){
            ft.init(this);
        }
    }

    /**
     * Internal Use Only
     */
    public void render(Window window){
        nvgBeginFrame(vg, window.getWidth(), window.getHeight(), 1);
        for(HUDItem it : hudItems){
            it.render(this, GameHandler.getInstance());
        }
        nvgEndFrame(vg);
        window.restoreState();
    }

    /**
     * Internal Use Only.
     */
    public void cleanup(){
        for(HUDItem item : hudItems){
            item.cleanup(GameHandler.getInstance());
        }
    }


    /**
     * Get the context of NanoVG.
     * @return The context of NanoVG.
     */
    public long getVG(){
        return vg;
    }

    /**
     * Add an item to the HUD.
     * <p>Components <b>do not</b> go here! They must go inside of a ComponentCanvas! See {@link org.kakara.engine.ui.items.ComponentCanvas#add(Component)}</p>
     * @param item The item to add.
     */
    public void addItem(HUDItem item){
        Long check = this.vg;
        if(check != null){
            item.init(this, GameHandler.getInstance());
        }
        hudItems.add(item);
    }

    /**
     * Remove an item from the hud.
     * @since 1.0-Pre1
     * @param item The item to remove.
     */
    public void removeItem(HUDItem item){
        hudItems.remove(item);
    }

    /**
     * Add a font to the HUD.
     * <p><b>Internal Use Only!</b></p>
     * @param font The font toa add.
     */
    public void addFont(Font font){
        Long check = this.vg;
        if(check != null){
            font.init(this);
        }
        fonts.add(font);
    }

    /**
     * Get the Image Cache.
     * <p>Primarily Internal Use Only</p>
     * @deprecated Replaced by internal cleanup calls.
     * @return The image cache.
     */
    public HUDImageCache getImageCache(){
        return imageCache;
    }

    /**
     * Get the scene that this hud belongs to.
     * @return The scene.
     */
    public Scene getScene(){
        return scene;
    }

    /**
     * Check to see if a position is colliding with an object.
     * @param position The position of the component
     * @param scale The scale of the component
     * @param mouse The 2D Vector you want to test for.
     * @return If the point is colliding with the area.
     */
    public static boolean isColliding(Vector2 position, Vector2 scale, Vector2 mouse){
        boolean overx = position.x < mouse.x && mouse.x < position.x + scale.x;
        boolean overy = position.y < mouse.y && mouse.y < position.y + scale.y;
        return overx && overy;
    }
}
