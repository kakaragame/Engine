package org.kakara.engine.test2;

import org.kakara.engine.GameEngine;

/**
 * A basic test class to test basic functionality.
 */
public class Main {
    public static void main(String[] args) {
        KakaraExample kakaraExample = new KakaraExample();
        GameEngine gameEngine = new GameEngine("Kakara Example Game", 1080, 720, true, kakaraExample);
        gameEngine.run();
    }
}
