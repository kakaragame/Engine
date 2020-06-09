package org.kakara.engine.ui;

import org.kakara.engine.GameHandler;

public interface HUDItem {
    /**
     * Internal use only
     */
    void init(HUD hud, GameHandler handler);

    /**
     * Internal use only
     */
    void render(HUD hud, GameHandler handler);


    /**
     * Internal use only.
     */
    void cleanup(GameHandler handler);
}
