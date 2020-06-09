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
 * The base Ellipse shape.
 * <p>The scale is used as the x and y radius.</p>
 * <p>the position of this component is in the center</p>
 * <code>
 *     Ellipse el = new Ellipse();
 *     // Sets the x radius to 40
 *     // and the y radius to 50.
 *     el.setScale(40, 50);
 * </code>
 * @since 1.0-Pre1
 */
public class Ellipse extends GeneralComponent {
    private RGBA color;
    private NVGColor nvgColor;

    private boolean isHovering;

    public Ellipse(){
        this(new Vector2(0, 0), new Vector2(40, 40), new RGBA());
    }

    /**
     * Create a rectangle
     * @param position The position of the ellipse.
     * @param scale The x and y radius of the ellipse
     * @param color The color of the ellipse.
     */
    public Ellipse(Vector2 position, Vector2 scale, RGBA color){
        this.position = position;
        this.scale = scale;
        this.color = color;
        this.nvgColor = NVGColor.create();
        this.isHovering = false;
    }

    public Ellipse(Vector2 position, Vector2 scale){
        this(position, scale, new RGBA());
    }


    /**
     * Set the color of the ellipse.
     * @param color The color value
     */
    public void setColor(RGBA color){
        this.color = color;
    }

    /**
     * Get the color of the ellipse.
     * @return The color of the ellipse.
     */
    public RGBA getColor(){
        return color;
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
        nvgEllipse(hud.getVG(), getTruePosition().x, getTruePosition().y, getTrueScale().x, getTrueScale().y);

        nvgRGBA((byte) color.r, (byte) color.g, (byte) color.b, (byte) color.aToNano(), nvgColor);
        nvgFillColor(hud.getVG(), nvgColor);
        nvgFill(hud.getVG());

        pollRender(relative, hud, handler);
    }
}
