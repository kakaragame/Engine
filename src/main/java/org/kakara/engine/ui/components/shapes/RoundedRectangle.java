package org.kakara.engine.ui.components.shapes;

import org.kakara.engine.GameHandler;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.HUD;
import org.kakara.engine.ui.RGBA;
import org.kakara.engine.ui.components.GeneralComponent;
import org.kakara.engine.ui.events.HUDClickEvent;
import org.kakara.engine.ui.events.HUDHoverEnterEvent;
import org.kakara.engine.ui.events.HUDHoverLeaveEvent;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * A rectangle with rounded corners.
 * @since 1.0-Pre1
 */
public class RoundedRectangle extends GeneralComponent {
    private RGBA color;
    private NVGColor nvgColor;
    private float radius;

    private boolean isHovering;

    public RoundedRectangle(){
        this(new Vector2(0, 0), new Vector2(40, 40), 1.0f, new RGBA());
    }

    /**
     * Create a rectangle
     * @param position The position of the rectangle
     * @param scale The scale of the rectangle
     * @param radius The radius of the corners.
     * @param color The color of the rectangle.
     */
    public RoundedRectangle(Vector2 position, Vector2 scale, float radius, RGBA color){
        this.position = position;
        this.scale = scale;
        this.color = color;
        this.nvgColor = NVGColor.create();
        this.isHovering = false;
        this.radius = radius;
    }

    public RoundedRectangle(Vector2 position, Vector2 scale){
        this(position, scale, 1.0f, new RGBA());
    }


    /**
     * Set the color of the rectangle.
     * @param color The color value
     */
    public void setColor(RGBA color){
        this.color = color;
    }

    /**
     * Get the color of the rectangle.
     * @return The color of the rectangle.
     */
    public RGBA getColor(){
        return color;
    }

    /**
     * Get the radius of the corners.
     * @return The radius of the corners.
     */
    public float getRadius(){
        return radius;
    }

    /**
     * Set the radius of the corners.
     * @param radius The radius.
     */
    public void setRadius(float radius){
        this.radius = radius;
    }

    @EventHandler
    public void onClick(MouseClickEvent evt){
        if(HUD.isColliding(getTruePosition(), getTrueScale(), new Vector2(evt.getMousePosition()))){
            triggerEvent(HUDClickEvent.class, new Vector2(evt.getMousePosition()), evt.getMouseClickType());
        }
    }


    @Override
    public void init(HUD hud, GameHandler handler) {
        pollInit(hud, handler);
        hud.getScene().getEventManager().registerHandler(this);
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

        nvgBeginPath(hud.getVG());
        nvgRoundedRect(hud.getVG(), getTruePosition().x, getTruePosition().y, getTrueScale().x, getTrueScale().y, radius);

        nvgRGBA((byte) color.r, (byte) color.g, (byte) color.b, (byte) color.aToNano(), nvgColor);
        nvgFillColor(hud.getVG(), nvgColor);
        nvgFill(hud.getVG());

        pollRender(relative, hud, handler);
    }

    @Override
    public void cleanup(GameHandler handler){
        super.cleanup(handler);
        nvgColor.free();
    }
}
