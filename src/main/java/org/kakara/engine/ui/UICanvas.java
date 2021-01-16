package org.kakara.engine.ui;

import org.kakara.engine.GameHandler;
import org.kakara.engine.properties.Tagable;

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
