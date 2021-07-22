package org.kakara.engine.ui.components.shapes;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.components.GeneralUIComponent;
import org.kakara.engine.utils.RGBA;
import org.lwjgl.nanovg.NVGColor;

import static org.lwjgl.nanovg.NanoVG.*;

/**
 * The base Ellipse shape.
 * <p>The scale is used as the x and y radius.</p>
 * <p>the position of this component is in the center</p>
 * <code>
 * Ellipse el = new Ellipse(); <br>
 * // Sets the x radius to 40 <br>
 * // and the y radius to 50. <br>
 * el.setScale(40, 50); <br>
 * </code>
 *
 * @since 1.0-Pre1
 */
public class Ellipse extends GeneralUIComponent {
    private final NVGColor nvgColor;
    private RGBA color;

    /**
     * Create an Ellipse.
     *
     * @param position The position of the ellipse.
     * @param radii    The x and y radii of the ellipse
     * @param color    The color of the ellipse.
     */
    public Ellipse(Vector2 position, Vector2 radii, RGBA color) {
        this.position = position;
        this.scale = radii;
        this.color = color;
        this.nvgColor = NVGColor.create();
    }

    /**
     * Create an Ellipse.
     *
     * @param position The position of the Ellipse.
     * @param radii    The x and y radii of the Ellipse.
     */
    public Ellipse(Vector2 position, Vector2 radii) {
        this(position, radii, new RGBA());
    }

    /**
     * Create an Ellipse.
     *
     * <p>The default X and Y Radii are 40.</p>
     */
    public Ellipse() {
        this(new Vector2(0, 0), new Vector2(40, 40), new RGBA());
    }

    /**
     * Get the color of the ellipse.
     *
     * @return The color of the ellipse.
     */
    public RGBA getColor() {
        return color;
    }

    /**
     * Set the color of the ellipse.
     *
     * @param color The color value
     */
    public void setColor(RGBA color) {
        this.color = color;
    }

    /**
     * Set the Radii of the Ellipse.
     *
     * @param radii The radii to set.
     */
    public void setRadii(Vector2 radii) {
        this.scale.set(radii);
    }

    /**
     * Get the radii of the Ellipse.
     *
     * @return The radii of the Ellipse.
     */
    public Vector2 getRadii() {
        return scale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void init(UserInterface userInterface, GameHandler handler) {
        pollInit(userInterface, handler);
        userInterface.getScene().getEventManager().registerHandler(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void render(Vector2 relative, UserInterface userInterface, GameHandler handler) {
        if (!isVisible()) return;

        nvgBeginPath(userInterface.getVG());
        nvgEllipse(userInterface.getVG(), getGlobalPosition().x, getGlobalPosition().y, getGlobalScale().x, getGlobalScale().y);

        nvgRGBA((byte) color.r, (byte) color.g, (byte) color.b, (byte) color.aToNano(), nvgColor);
        nvgFillColor(userInterface.getVG(), nvgColor);
        nvgFill(userInterface.getVG());

        super.render(relative, userInterface, handler);
    }
}
