package org.kakara.engine.ui.font;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.resources.Resource;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.ui.UserInterface;

import java.nio.ByteBuffer;
import java.util.Optional;

import static org.lwjgl.nanovg.NanoVG.nvgCreateFontMem;

/**
 * Handles the font for the UI.
 *
 * <p>Note: Fonts are Scene dependent, meaning you cannot get a font defined in a previous scene. You will need to redfine
 * the font with every new Scene.</p>
 */
public class Font {

    private final String name;
    private final Resource fileName;
    private int font;
    private ByteBuffer thisNeedsToBeHereSoTheGarbageCollectorDoesNotComeAndGetMeTMDotCom;

    /**
     * Create a new font
     *
     * @param name         the name of the font.
     *                     <p>It doesn't matter what it is, as long as you don't use the same name twice</p>
     * @param fileName     The font resource.
     * @param currentScene the active scene.
     */
    public Font(String name, Resource fileName, Scene currentScene) {
        this.name = name;
        this.fileName = fileName;
        currentScene.getUserInterface().addFont(this);
    }

    /**
     * Internal Use Only
     *
     * @param userInterface the user interface
     */
    public void init(UserInterface userInterface) {
        try {
            ByteBuffer bb = fileName.getByteBuffer();
            font = nvgCreateFontMem(userInterface.getVG(), name, bb, 1);
            this.thisNeedsToBeHereSoTheGarbageCollectorDoesNotComeAndGetMeTMDotCom = bb;
        } catch (Exception ex) {
            GameEngine.LOGGER.error("Error: Could not load font: " + name);
        }
    }

    /**
     * Get the id of the font for use with NVG static functions.
     *
     * @return The id of the font.
     */
    public int getFontId() {
        return font;
    }

    /**
     * Get the name of the font.
     *
     * @return The name of the font.
     * @since 1.0-Pre3
     */
    public String getName() {
        return name;
    }

    /**
     * Internal Use Only
     *
     * @return the bytebuffer for the font
     */
    public ByteBuffer getByteBuffer() {
        return thisNeedsToBeHereSoTheGarbageCollectorDoesNotComeAndGetMeTMDotCom;
    }

    /**
     * Get a Font by name.
     *
     * <p>Note: Fonts are Scene dependent, meaning you cannot get a font defined in a previous scene.</p>
     *
     * @param name The name of the font to get.
     * @return The font. (Can be null if not found.)
     */
    @Nullable
    public static Font getFont(String name) {
        return GameHandler.getInstance().getCurrentScene().getUserInterface().getFont(name);
    }

    /**
     * Get a Font by name.
     *
     * <p>Note: Fonts are Scene dependent, meaning you cannot get a font defined in a previous scene.</p>
     *
     * @param name The name of the font to get.
     * @return An optional that may contain the retrieved font.
     */
    @NotNull
    public static Optional<Font> getFontOptional(String name) {
        return GameHandler.getInstance().getCurrentScene().getUserInterface().getOptionalFont(name);
    }
}
