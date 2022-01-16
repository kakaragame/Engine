package org.kakara.engine.ui.components;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UserInterface;

import static org.lwjgl.nanovg.NanoVG.nvgResetScissor;
import static org.lwjgl.nanovg.NanoVG.nvgScissor;

/**
 * An empty UI Component to contain other components.
 *
 * <p>This is meant to be a container that can holder multiple UIComponents. It also
 * controls the overflow of the panel and can be used to hide content.</p>
 *
 * <code>
 * Panel panel = new Panel(); <br>
 * Rectangle rect = new Rectangle(); <br>
 * panel.add(rect); <br>
 * </code>
 */
public class Panel extends GeneralUIComponent {

    private boolean allowOverflow;

    /**
     * Create a panel using the default constructor.
     * <p>Default scale of (100, 100)</p>
     */
    public Panel() {
        this(new Vector2(0, 0), new Vector2(100, 100));
    }

    /**
     * Create a panel.
     *
     * @param position The position of the panel.
     * @param scale    The scale of the panel.
     */
    public Panel(Vector2 position, Vector2 scale) {
        super();
        allowOverflow = false;
        this.position = position;
        this.scale = scale;
    }

    /**
     * If the panel allows there to be overflow.
     *
     * @return If the panel allows overflow.
     */
    public boolean allowsOverflow() {
        return allowOverflow;
    }

    /**
     * Set if the panel allows overflow.
     *
     * @param allowOverflow If the panel allow overflow.
     */
    public void setAllowOverflow(boolean allowOverflow) {
        this.allowOverflow = allowOverflow;
    }

    @Override
    public void init(UserInterface userInterface, GameHandler handler) {
        pollInit(userInterface, handler);
        userInterface.getScene().getEventManager().registerHandler(this);
    }

    @Override
    public void render(Vector2 relative, UserInterface userInterface, GameHandler handler) {
        if (!isVisible()) return;

        if (!allowOverflow)
            nvgScissor(userInterface.getVG(), relative.x, relative.y, getGlobalScale().x, getGlobalScale().y);

        super.render(relative, userInterface, handler);

        if (!allowOverflow)
            nvgResetScissor(userInterface.getVG());
    }
}
