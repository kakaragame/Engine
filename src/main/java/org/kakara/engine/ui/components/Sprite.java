package org.kakara.engine.ui.components;

import org.kakara.engine.GameHandler;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.events.event.MouseReleaseEvent;
import org.kakara.engine.gameitems.Texture;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.events.UIClickEvent;
import org.kakara.engine.ui.events.UIReleaseEvent;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVGGL3;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * The Sprite Component is used to display an image (or sprite) on the
 * UserInterface.
 */
public class Sprite extends GeneralUIComponent {
    private float rotation;
    private byte alpha;
    private int image;
    private Texture texture;

    /**
     * Create a sprite.
     *
     * @param texture  The texture of the sprite
     * @param position The position of the sprite
     * @param scale    The scale of the sprite.
     */
    public Sprite(Texture texture, Vector2 position, Vector2 scale) {
        this.alpha = (byte) 255;
        this.rotation = 0;
        this.position = position;
        this.scale = scale;
        this.texture = texture;
    }

    /**
     * Create a sprite.
     * <p>The scale of the sprite is automatically calculated by the system.</p>
     *
     * @param texture  The texture of the sprite.
     * @param position The position of the sprite.
     */
    public Sprite(Texture texture, Vector2 position) {
        this.alpha = (byte) 255;
        this.rotation = 0;
        this.position = position;
        this.scale = new Vector2(-1, -1);
        this.texture = texture;
    }

    /**
     * Create a sprite.
     * <p>The scale of the sprite is automatically calculated by the system.</p>
     *
     * @param tex The texture of the sprite.
     */
    public Sprite(Texture tex) {
        this(tex, new Vector2(0, 0));
    }

    @Override
    public void init(UserInterface userInterface, GameHandler handler) {
        pollInit(userInterface, handler);
        this.image = NanoVGGL3.nvglCreateImageFromHandle(userInterface.getVG(), texture.getId(), texture.getWidth(), texture.getHeight(), 0);
        if (scale.equals(new Vector2(-1, -1))) {
            int[] w = new int[1], h = new int[1];
            nvgImageSize(userInterface.getVG(), image, w, h);
            this.setScale(w[0], h[0]);
        }
        userInterface.getScene().getEventManager().registerHandler(this);
    }

    @EventHandler
    public void onClick(MouseClickEvent evt) {
        if (UserInterface.isColliding(getGlobalPosition(), scale, new Vector2(evt.getMousePosition()))) {
            triggerEvent(UIClickEvent.class, position, evt.getMouseClickType());
        }
    }

    @EventHandler
    public void onRelease(MouseReleaseEvent evt) {
        if (UserInterface.isColliding(getGlobalPosition(), scale, new Vector2(evt.getMousePosition()))) {
            triggerEvent(UIReleaseEvent.class, position, evt.getMouseClickType());
        }
    }

    /**
     * Change the image of the sprite
     *
     * @param tex The texture to change it to.
     */
    public void setImage(Texture tex) {
        this.texture = tex;
        nvgDeleteImage(GameHandler.getInstance().getSceneManager().getCurrentScene().getUserInterface().getVG(),
                image);
        this.image = NanoVGGL3.nvglCreateImageFromHandle(
                GameHandler.getInstance().getSceneManager().getCurrentScene().getUserInterface().getVG(),
                tex.getId(), texture.getWidth(), tex.getHeight(), 0);
    }

    /**
     * Set the scale of the image to the size of the image itself.
     */
    public void setScaleToImageSize() {
        int[] w = new int[1], h = new int[1];
        nvgImageSize(GameHandler.getInstance().getSceneManager().getCurrentScene().getUserInterface().getVG(), image, w, h);
        this.setScale(w[0], h[0]);
    }

    /**
     * Get the alpha value of the sprite
     *
     * @return The alpha value.
     */
    public int getAlpha() {
        return this.alpha & 0xFF;
    }

    /**
     * Set the transparency of the sprite.
     *
     * @param b Transparency level
     * @return The instance of the sprite.
     */
    public Sprite setAlpha(byte b) {
        this.alpha = b;
        return this;
    }

    /**
     * Get the rotation of the sprite.
     *
     * @return The rotation.
     */
    public float getRotation() {
        return this.rotation;
    }

    /**
     * Set the rotation of the image
     *
     * @param rotation The rotation of the image in degrees.
     * @return The instance of the sprite.
     */
    public Sprite setRotation(float rotation) {
        this.rotation = rotation;
        return this;
    }

    @Override
    public void render(Vector2 relative, UserInterface userInterface, GameHandler handler) {
        if (!isVisible()) return;
        NVGPaint imagePaint = nvgImagePattern(userInterface.getVG(), getGlobalPosition().x, getGlobalPosition().y, getGlobalScale().x, getGlobalScale().y, rotation, image, 1.0f, NVGPaint.calloc());
        nvgBeginPath(userInterface.getVG());
        nvgRect(userInterface.getVG(), getGlobalPosition().x, getGlobalPosition().y, getGlobalScale().x, getGlobalScale().y);
        nvgFillPaint(userInterface.getVG(), imagePaint);
        nvgFill(userInterface.getVG());
        nvgClosePath(userInterface.getVG());
        imagePaint.free();

        super.render(relative, userInterface, handler);
    }

    @Override
    public void cleanup(GameHandler handler) {
        super.cleanup(handler);
        image = 0;
    }
}
