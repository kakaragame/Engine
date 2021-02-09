package org.kakara.engine.ui;

import org.kakara.engine.GameHandler;
import org.kakara.engine.properties.Tagable;

/**
 * A UICanvas holds elements that are meant to be seen on the UI.
 *
 * <p>See {@link org.kakara.engine.ui.items.ComponentCanvas} and {@link org.kakara.engine.ui.items.ObjectCanvas}
 * for more information.</p>
 */
public interface UICanvas extends Tagable {
    /**
     * Initialize the UICanvas
     *
     * @param userInterface The user interface.
     * @param handler       The game handler.
     */
    void init(UserInterface userInterface, GameHandler handler);

    /**
     * Render the UICanvas.
     *
     * @param userInterface The user interface.
     * @param handler       The game handler.
     */
    void render(UserInterface userInterface, GameHandler handler);


    /**
     * Cleanup the UICanvas
     *
     * @param handler The game handler.
     */
    void cleanup(GameHandler handler);
}
