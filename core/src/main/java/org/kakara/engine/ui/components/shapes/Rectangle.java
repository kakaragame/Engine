package org.kakara.engine.ui.components.shapes;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.components.GeneralUIComponent;
import org.kakara.engine.utils.RGBA;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * The general Rectangle component for the User Interface.
 */
public class Rectangle extends GeneralUIComponent {
    private final NVGColor nvgColor;
    private RGBA color;

    /**
     * Create a rectangle
     *
     * @param position The position of the rectangle
     * @param scale    The scale of the rectangle
     * @param color    The color of the rectangle.
     */
    public Rectangle(Vector2 position, Vector2 scale, RGBA color) {
        super();
        this.position = position;
        this.scale = scale;
        this.color = color;
        this.nvgColor = NVGColor.create();
    }

    /**
     * Construct a Rectangle with only a position and scale.
     *
     * @param position The position of the Rectangle.
     * @param scale    THe scale of the Rectangle.
     */
    public Rectangle(Vector2 position, Vector2 scale) {
        this(position, scale, new RGBA());
    }

    /**
     * Construct a Rectangle with the default values.
     *
     * <p>Default scale is 40 x 40.</p>
     */
    public Rectangle() {
        this(new Vector2(0, 0), new Vector2(40, 40), new RGBA());
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
     * {@inheritDoc}
     */
    @Override
    public void init(UserInterface userInterface, GameHandler handler) {
        pollInit(userInterface, handler);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Vector2 relative, UserInterface userInterface, GameHandler handler) {
        if (!isVisible()) return;

        nvgBeginPath(userInterface.getVG());
        nvgRect(userInterface.getVG(), getGlobalPosition().x, getGlobalPosition().y, getGlobalScale().x, getGlobalScale().y);
        nvgRGBA((byte) color.r, (byte) color.g, (byte) color.b, (byte) color.aToNano(), nvgColor);
        nvgFillColor(userInterface.getVG(), nvgColor);
        nvgFill(userInterface.getVG());
        nvgClosePath(userInterface.getVG());

        super.render(relative, userInterface, handler);
    }
}
