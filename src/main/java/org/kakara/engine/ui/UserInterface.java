package org.kakara.engine.ui;

import org.kakara.engine.GameHandler;
import org.kakara.engine.exceptions.InitializationException;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.ui.components.UIComponent;
import org.kakara.engine.ui.font.Font;
import org.kakara.engine.window.Window;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;
import static org.lwjgl.nanovg.NanoVGGL3.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Handles the UI.
 * This class is per scene.
 */
public class UserInterface {

    private final List<UICanvas> uiCanvas;
    private final List<Font> fonts;
    private final Scene scene;
    private long vg;


    public UserInterface(Scene scene) {
        uiCanvas = new ArrayList<>();
        fonts = new ArrayList<>();
        this.scene = scene;
    }

    /**
     * Check to see if a position is colliding with an object.
     *
     * @param position The position of the component
     * @param scale    The scale of the component
     * @param mouse    The 2D Vector you want to test for.
     * @return If the point is colliding with the area.
     */
    public static boolean isColliding(Vector2 position, Vector2 scale, Vector2 mouse) {
        boolean overx = position.x < mouse.x && mouse.x < position.x + scale.x;
        boolean overy = position.y < mouse.y && mouse.y < position.y + scale.y;
        return overx && overy;
    }

    /**
     * Internal Use Only
     *
     * @param window The window.
     * @throws InitializationException If the ui cannot be initialized.
     */
    public void init(Window window) throws InitializationException {
        this.vg = window.getOptions().isAntialiasing() ? nvgCreate(NVG_ANTIALIAS | NVG_STENCIL_STROKES) : nvgCreate(NVG_STENCIL_STROKES);

        if (this.vg == NULL) {
            throw new InitializationException("Could not initialize the user interface! Is the OpenGL context correct?");
        }

        for (UICanvas it : uiCanvas) {
            it.init(this, GameHandler.getInstance());
        }
        for (Font ft : fonts) {
            ft.init(this);
        }
    }

    /**
     * Internal Use Only
     *
     * @param window The window.
     */
    public void render(Window window) {
        nvgBeginFrame(vg, window.getWidth(), window.getHeight(), 1);
        for (UICanvas it : uiCanvas) {
            it.render(this, GameHandler.getInstance());
        }
        nvgEndFrame(vg);
        window.restoreState();
    }

    /**
     * Internal Use Only.
     */
    public void cleanup() {
        for (UICanvas item : uiCanvas) {
            item.cleanup(GameHandler.getInstance());
        }
    }

    /**
     * Get the context of NanoVG.
     *
     * @return The context of NanoVG.
     */
    public long getVG() {
        return vg;
    }

    /**
     * Add an item to the HUD.
     * <p>Components <b>do not</b> go here! They must go inside of a ComponentCanvas! See {@link org.kakara.engine.ui.canvases.ComponentCanvas#add(UIComponent)}</p>
     *
     * @param item The item to add.
     */
    public void addItem(UICanvas item) {
        Long check = this.vg;
        if (check != null) {
            item.init(this, GameHandler.getInstance());
        }
        uiCanvas.add(item);
    }

    /**
     * Remove an item from the hud.
     *
     * @param item The item to remove.
     * @since 1.0-Pre1
     */
    public void removeItem(UICanvas item) {
        uiCanvas.remove(item);
    }

    /**
     * Add a font to the HUD.
     * <p><b>Internal Use Only!</b></p>
     *
     * @param font The font toa add.
     */
    public void addFont(Font font) {
        Long check = this.vg;
        if (check != null) {
            font.init(this);
        }
        fonts.add(font);
    }

    /**
     * Get the Font via its name.
     *
     * @param name The name of the desired font.
     * @return The font.
     * @since 1.0-Pre3
     */
    public Font getFont(String name) {
        for (Font font : fonts) {
            if (font.getName().equals(name))
                return font;
        }
        return null;
    }

    /**
     * Get a list of the UI Canvases.
     *
     * @return The list of ui canvases.
     */
    public List<UICanvas> getUICanvases() {
        return uiCanvas;
    }

    /**
     * Get a font vis its name.
     *
     * @param name The name of the font.
     * @return The font optional.
     * @since 1.0-Pre3
     */
    public Optional<Font> getOptionalFont(String name) {
        return Optional.ofNullable(getFont(name));
    }

    /**
     * Get the scene that this hud belongs to.
     *
     * @return The scene.
     */
    public Scene getScene() {
        return scene;
    }
}
