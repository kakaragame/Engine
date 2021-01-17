package org.kakara.engine;

import org.kakara.engine.scene.Scene;

/**
 * An interface laying out how a Game should be laid out
 */
public interface Game {
    /**
     * What should happen on the start of the game
     * <p><b>Nothing graphical related can be done here. All of that must be handled by a scene.</b></p>
     *
     * @param gameHandler The Game Handler
     * @throws Exception Exception if something breaks
     */
    void start(GameHandler gameHandler) throws Exception;

    /**
     * The first scene of the game.
     *
     * @param handler The game handler.
     * @return The first scene that should be loaded in.
     * @throws Exception Exception if something breaks
     * @since 1.0-Pre1
     */
    Scene firstScene(GameHandler handler) throws Exception;

    /**
     * Updates to the game
     * <p><b>Nothing graphical related can be done here. All of that must be handled by a scene.</b></p>
     */
    void update();

    /**
     * Exits the game;
     */
    void exit();
}
