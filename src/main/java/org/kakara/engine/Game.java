package org.kakara.engine;

/**
 * An interface laying out how a Game should be layed out
 */
public interface Game {
    /**
     * What should happen on the start of the game
     *
     * @param gameHandler The Game Handler
     * @throws Exception Exception if something breaks
     */
    void start(GameHandler gameHandler) throws Exception;

    /**
     * Updates to the game
     */
    void update();

    /**
     * Exits the game;
     */
    void exit();
}
