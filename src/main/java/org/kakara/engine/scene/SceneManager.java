package org.kakara.engine.scene;

import org.kakara.engine.GameHandler;
import org.kakara.engine.models.TextureCache;

/**
 * This is the class that manages the scenes.
 */
public class SceneManager {
    private Scene currentScene;
    private GameHandler handler;

    public SceneManager(GameHandler gameHandler) {
        this.handler = gameHandler;
    }

    /**
     * Set the current scene.
     * @param scene The scene to set.
     */
    public void setScene(Scene scene) {
        if(currentScene != null)
            this.cleanupScenes();
        scene.work();
        scene.unload();
        try {
            handler.getGameEngine().resetRender();
            scene.loadGraphics(handler);
        } catch (Exception e) {
            e.printStackTrace();
        }
        currentScene = scene;
    }

    /**
     * Render the current scene.
     */
    public void renderCurrentScene() {
        currentScene.render();
    }

    /**
     * Get the current scene.
     * @return The current scene.
     */
    public Scene getCurrentScene() {
        return currentScene;
    }

    /**
     * Cleanup the scene and clear the memory that way it is ready for the next scene to be loaded.
     */
    public void cleanupScenes(){
        currentScene.getHUD().cleanup();
        TextureCache.getInstance(handler.getResourceManager()).cleanup(currentScene);
        if(getCurrentScene() instanceof AbstractMenuScene) return;
        currentScene.getItemHandler().cleanup();
    }
}
