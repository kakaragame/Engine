package org.kakara.engine.ui.components.shapes;

import org.kakara.engine.GameHandler;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.RGBA;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.components.GeneralComponent;
import org.kakara.engine.ui.events.UIClickEvent;
import org.kakara.engine.ui.events.UIHoverEnterEvent;
import org.kakara.engine.ui.events.UIHoverLeaveEvent;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * A rectangle with rounded corners.
 *
 * @since 1.0-Pre1
 */
public class RoundedRectangle extends GeneralComponent {
    private RGBA color;
    private NVGColor nvgColor;
    private float radius;

    private boolean isHovering;

    public RoundedRectangle() {
        this(new Vector2(0, 0), new Vector2(40, 40), 1.0f, new RGBA());
    }

    /**
     * Create a rectangle
     *
     * @param position The position of the rectangle
     * @param scale    The scale of the rectangle
     * @param radius   The radius of the corners.
     * @param color    The color of the rectangle.
     */
    public RoundedRectangle(Vector2 position, Vector2 scale, float radius, RGBA color) {
        this.position = position;
        this.scale = scale;
        this.color = color;
        this.nvgColor = NVGColor.create();
        this.isHovering = false;
        this.radius = radius;
    }

    public RoundedRectangle(Vector2 position, Vector2 scale) {
        this(position, scale, 1.0f, new RGBA());
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

    /**
     * Get the radius of the corners.
     *
     * @return The radius of the corners.
     */
    public float getRadius() {
        return radius;
    }

    /**
     * Set the radius of the corners.
     *
     * @param radius The radius.
     */
    public void setRadius(float radius) {
        this.radius = radius;
    }

    @EventHandler
    public void onClick(MouseClickEvent evt) {
        if (UserInterface.isColliding(getTruePosition(), getTrueScale(), new Vector2(evt.getMousePosition()))) {
            triggerEvent(UIClickEvent.class, new Vector2(evt.getMousePosition()), evt.getMouseClickType());
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
        nvgRoundedRect(userInterface.getVG(), getTruePosition().x, getTruePosition().y, getTrueScale().x, getTrueScale().y, radius);

        nvgRGBA((byte) color.r, (byte) color.g, (byte) color.b, (byte) color.aToNano(), nvgColor);
        nvgFillColor(userInterface.getVG(), nvgColor);
        nvgFill(userInterface.getVG());

        pollRender(relative, userInterface, handler);
    }
}
