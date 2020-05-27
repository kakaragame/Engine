package org.kakara.engine.ui.components;

import org.kakara.engine.GameHandler;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.item.Texture;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.HUD;
import org.kakara.engine.ui.events.HUDClickEvent;
import org.kakara.engine.ui.events.HUDHoverEnterEvent;
import org.kakara.engine.ui.events.HUDHoverLeaveEvent;
import org.lwjgl.nanovg.NVGPaint;
import org.lwjgl.nanovg.NanoVGGL3;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Display an image onto the HUD.
 */
public class Sprite extends GeneralComponent {
    private float rotation;
    private byte alpha;
    private int image;
    private Texture texture;

    private boolean isHovering;

    /**
     * Create a sprite.
     * @param texture The texture of the sprite
     * @param position The position of the sprite
     * @param scale The scale of the sprite.
     */
    public Sprite(Texture texture, Vector2 position, Vector2 scale){
        this.alpha = (byte) 255;
        this.rotation = 0;
        this.position = position;
        this.scale = scale;
        this.texture = texture;

        isHovering = false;
    }

    @Override
    public void init(HUD hud, GameHandler handler) {
        pollInit(hud, handler);
        this.image = NanoVGGL3.nvglCreateImageFromHandle(hud.getVG(), texture.getId(), texture.getWidth(), texture.getHeight(), 0);
        hud.getImageCache().addImage(this.image);
        handler.getEventManager().registerHandler(this, hud.getScene());
    }

    @EventHandler
    public void onClick(MouseClickEvent evt){
        if(HUD.isColliding(getTruePosition(), scale, new Vector2(evt.getMousePosition()))){
            triggerEvent(HUDClickEvent.class, position, evt.getMouseClickType());
        }
    }

    public Sprite(Texture tex){
        this(tex, new Vector2(0, 0), new Vector2(1, 1));
    }

    /**
     * Change the image of the sprite
     * @param tex The texture to change it to.
     */
    public void setImage(Texture tex){
        this.texture = tex;
        GameHandler.getInstance().getSceneManager().getCurrentScene().getHUD().getImageCache().removeImage(this.image);
        nvgDeleteImage(GameHandler.getInstance().getSceneManager().getCurrentScene().getHUD().getVG(),
                image);
        this.image = NanoVGGL3.nvglCreateImageFromHandle(
                GameHandler.getInstance().getSceneManager().getCurrentScene().getHUD().getVG(),
                tex.getId(), texture.getWidth(),tex.getHeight(), 0);
        GameHandler.getInstance().getSceneManager().getCurrentScene().getHUD().getImageCache().addImage(this.image);
    }

    /**
     * Set the transparency of the sprite.
     * @param b Transparency level
     * @return The instance of the sprite.
     */
    public Sprite setAlpha(byte b){
        this.alpha = b;
        return this;
    }

    /**
     * Get the alpha value of the sprite
     * @return The alpha value.
     */
    public int getAlpha(){
        return this.alpha & 0xFF;
    }

    /**
     * Set the rotation of the image
     * @param rotation The rotation of the image in degrees.
     * @return The instance of the sprite.
     */
    public Sprite setRotation(float rotation){
        this.rotation = rotation;
        return this;
    }

    /**
     * Get the rotation of the sprite.
     * @return The rotation.
     */
    public float getRotation(){
        return this.rotation;
    }

    @Override
    public void render(Vector2 relative, HUD hud, GameHandler handler) {
        if(!isVisible()) return;
        boolean isColliding = HUD.isColliding(getTruePosition(), getTrueScale(), new Vector2(handler.getMouseInput().getPosition()));
        if(isColliding && !isHovering){
            isHovering = true;
            triggerEvent(HUDHoverEnterEvent.class, handler.getMouseInput().getCurrentPosition());
        }else if(!isColliding && isHovering){
            isHovering = false;
            triggerEvent(HUDHoverLeaveEvent.class, handler.getMouseInput().getCurrentPosition());
        }

        NVGPaint imagePaint = nvgImagePattern(hud.getVG(), getTruePosition().x, getTruePosition().y, getTrueScale().x, getTrueScale().y, rotation, image, 1.0f, NVGPaint.calloc());
        nvgBeginPath(hud.getVG());
        nvgRect(hud.getVG(), getTruePosition().x, getTruePosition().y, getTruePosition().x + getTrueScale().x, getTruePosition().y + getTrueScale().y);
        nvgFillPaint(hud.getVG(), imagePaint);
        nvgFill(hud.getVG());
        imagePaint.free();
        pollRender(relative, hud, handler);
    }

    @Override
    public void cleanup(GameHandler handler){
        super.cleanup(handler);
//        nvgDeleteImage(handler.getSceneManager().getCurrentScene().getHUD().getVG(),
//                image);
        image = 0;
    }
}
