package org.kakara.engine.ui.components.shapes;

import org.kakara.engine.GameHandler;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.events.event.MouseReleaseEvent;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.events.UIReleaseEvent;
import org.kakara.engine.utils.RGBA;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.components.GeneralComponent;
import org.kakara.engine.ui.events.UIClickEvent;
import org.kakara.engine.ui.events.UIHoverEnterEvent;
import org.kakara.engine.ui.events.UIHoverLeaveEvent;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * Base Rectangle Component
 */
public class Rectangle extends GeneralComponent {
    private RGBA color;
    private NVGColor nvgColor;

    private boolean isHovering;

    public Rectangle() {
        this(new Vector2(0, 0), new Vector2(40, 40), new RGBA());
    }

    /**
     * Create a rectangle
     *
     * @param position The position of the rectangle
     * @param scale    The scale of the rectangle
     * @param color    The color of the rectangle.
     */
    public Rectangle(Vector2 position, Vector2 scale, RGBA color) {
        this.position = position;
        this.scale = scale;
        this.color = color;
        this.nvgColor = NVGColor.create();
        this.isHovering = false;
    }

    public Rectangle(Vector2 position, Vector2 scale) {
        this(position, scale, new RGBA());
    }

    /**
     * Get the color of the rectangle.
     *
     * @return The color of the rectangle.
     */
    public RGBA getColor() {
        return color;
    }

    /**
     * Set the color of the rectangle.
     *
     * @param color The color value
     */
    public void setColor(RGBA color) {
        this.color = color;
    }

    @EventHandler
    public void onClick(MouseClickEvent evt) {
        if (UserInterface.isColliding(getTruePosition(), getTrueScale(), new Vector2(evt.getMousePosition()))) {
            triggerEvent(UIClickEvent.class, new Vector2(evt.getMousePosition()), evt.getMouseClickType());
        }
    }

    @EventHandler
    public void onRelease(MouseReleaseEvent evt){
        if(UserInterface.isColliding(getTruePosition(), scale, new Vector2(evt.getMousePosition()))){
            triggerEvent(UIReleaseEvent.class, position, evt.getMouseClickType());
        }
    }


    @Override
    public void init(UserInterface userInterface, GameHandler handler) {
        pollInit(userInterface, handler);
        userInterface.getScene().getEventManager().registerHandler(this);
    }

    @Override
    public void render(Vector2 relative, UserInterface userInterface, GameHandler handler) {
        if (!isVisible()) return;
        boolean isColliding = UserInterface.isColliding(getTruePosition(), getTrueScale(), new Vector2(handler.getMouseInput().getPosition()));
        if (isColliding && !isHovering) {
            isHovering = true;
            triggerEvent(UIHoverEnterEvent.class, handler.getMouseInput().getCurrentPosition());
        } else if (!isColliding && isHovering) {
            isHovering = false;
            triggerEvent(UIHoverLeaveEvent.class, handler.getMouseInput().getCurrentPosition());
        }

        nvgBeginPath(userInterface.getVG());
        nvgRect(userInterface.getVG(), getTruePosition().x, getTruePosition().y, getTrueScale().x, getTrueScale().y);

        nvgRGBA((byte) color.r, (byte) color.g, (byte) color.b, (byte) color.aToNano(), nvgColor);
        nvgFillColor(userInterface.getVG(), nvgColor);
        nvgFill(userInterface.getVG());

        pollRender(relative, userInterface, handler);
    }
}
