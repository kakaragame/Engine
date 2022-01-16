package org.kakara.engine.ui.components.shapes;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.components.GeneralUIComponent;
import org.kakara.engine.utils.RGBA;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * A rectangle with rounded corners.
 *
 * <code>
 * RoundedRectangle rect = new RoundedRectangle();
 * </code>
 *
 * @since 1.0-Pre1
 */
public class RoundedRectangle extends GeneralUIComponent {
    private final NVGColor nvgColor;
    private RGBA color;
    private float radius;

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
        this.radius = radius;
    }

    /**
     * Create a Rounded Rectangle with the default radius (1.0) and color.
     *
     * @param position The position of the rectangle.
     * @param scale    The scale of the rectangle.
     */
    public RoundedRectangle(Vector2 position, Vector2 scale) {
        this(position, scale, 1.0f, new RGBA());
    }

    /**
     * Create a rounded rectangle with the default values.
     * <p>Position starts at (0, 0) while scale starts at (40, 40). Radius starts at 1.0f.</p>
     */
    public RoundedRectangle() {
        this(new Vector2(0, 0), new Vector2(40, 40), 1.0f, new RGBA());
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


    @Override
    public void init(UserInterface userInterface, GameHandler handler) {
        pollInit(userInterface, handler);
        userInterface.getScene().getEventManager().registerHandler(this);
    }

    @Override
    public void render(Vector2 relative, UserInterface userInterface, GameHandler handler) {
        if (!isVisible()) return;

        nvgBeginPath(userInterface.getVG());
        nvgRoundedRect(userInterface.getVG(), getGlobalPosition().x, getGlobalPosition().y, getGlobalScale().x, getGlobalScale().y, radius);

        nvgRGBA((byte) color.r, (byte) color.g, (byte) color.b, (byte) color.aToNano(), nvgColor);
        nvgFillColor(userInterface.getVG(), nvgColor);
        nvgFill(userInterface.getVG());

        super.render(relative, userInterface, handler);
    }
}
