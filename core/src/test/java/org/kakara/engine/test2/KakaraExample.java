package org.kakara.engine.test2;

import org.kakara.engine.Game;
import org.kakara.engine.GameHandler;
import org.kakara.engine.scene.Scene;

public class KakaraExample implements Game {

    private GameHandler gameHandler;

    @Override
    public void start(GameHandler gameHandler) throws Exception {
        this.gameHandler = gameHandler;
    }

    @Override
    public Scene firstScene(GameHandler gameHandler) throws Exception {
        return new MainScene(gameHandler);
    }

    @Override
    public void update() {

    }

    @Override
    public void exit() {
        gameHandler.exit();
    }
}
