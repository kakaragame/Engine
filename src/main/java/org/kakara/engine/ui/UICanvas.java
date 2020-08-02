package org.kakara.engine.ui;

import org.kakara.engine.GameHandler;
import org.kakara.engine.properties.Tagable;

public interface UICanvas extends Tagable {
    /**
     * Internal use only
     */
    void init(UserInterface userInterface, GameHandler handler);

    /**
     * Internal use only
     */
    void render(UserInterface userInterface, GameHandler handler);


    /**
     * Internal use only.
     */
    void cleanup(GameHandler handler);
}
