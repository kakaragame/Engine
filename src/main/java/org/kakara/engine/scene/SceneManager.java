package org.kakara.engine.scene;

import org.jetbrains.annotations.NotNull;
import org.kakara.engine.GameHandler;
import org.kakara.engine.models.TextureCache;

/**
 * This is the class that manages the scenes.
 */
public class SceneManager {
    private Scene currentScene;
    private final GameHandler handler;

    public SceneManager(GameHandler gameHandler) {
        this.handler = gameHandler;
    }

    /**
     * Set the current scene.
     *
     * @param scene The scene to set.
     */
    public void setScene(@NotNull Scene scene) {
        if (currentScene != null)
            this.cleanupScenes();
        currentScene = null;
        // TODO look further into this v
        System.gc();
        currentScene = scene;
        // Continue loading the next scene.
        try {
            scene.work();
        } catch (Exception e) {
            scene.handleException(e);
        }
        try {
            handler.getGameEngine().resetRender();
            try {
                scene.loadGraphics(handler);
            } catch (Exception e) {
                scene.handleException(e);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Render the current scene.
     */
    public void renderCurrentScene() {
        try {
            currentScene.render();
        } catch (Exception e) {
            currentScene.handleException(e);
        }
    }

    /**
     * Get the current scene.
     * <p>This value will never be null.</p>
     *
     * @return The current scene.
     */
    public @NotNull Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Cleanup the scene and clear the memory that way it is ready for the next scene to be loaded.
     */
    public void cleanupScenes() {
        currentScene.getUserInterface().cleanup();
        handler.getMouseInput().onSceneChange();
        TextureCache.getInstance(handler.getResourceManager()).cleanup(currentScene);
        if (getCurrentScene() instanceof AbstractMenuScene) return;
        currentScene.unload();
    }
}
