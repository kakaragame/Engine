package org.kakara.engine.scene;

import org.jetbrains.annotations.Nullable;
import org.kakara.engine.Camera;
import org.kakara.engine.GameHandler;
import org.kakara.engine.collision.CollisionManager;
import org.kakara.engine.events.EventManager;
import org.kakara.engine.item.ItemHandler;
import org.kakara.engine.item.particles.ParticleHandler;
import org.kakara.engine.item.SkyBox;
import org.kakara.engine.lighting.LightHandler;
import org.kakara.engine.ui.HUD;
import org.kakara.engine.weather.Fog;

public interface Scene {
    /**
     * Any loading of non graphics that are needed
     */
    void work();

    /**
     * Load the graphics
     */
    void loadGraphics(GameHandler gameHandler) throws Exception;

    /**
     * Internal Use Only.
     * <p><b>DO NOT OVERRIDE</b></p>
     */
    void render();

    /**
     * Called every time the game updates.
     */
    void update(float interval);

    /**
     * Set if the cursor should be enabled.
     *
     * @param status Cursor enabled.
     */
    void setCurserStatus(boolean status);

    /**
     * Check if the cursor is enabled.
     *
     * @return If the cursor is enabled.
     */
    boolean getCurserStatus();

    /**
     * Get the ItemHandler
     *
     * @return The item handler for this scene.
     */
    @Nullable ItemHandler getItemHandler();

    /**
     * Get the LightHandler for this scene.
     *
     * @return The light handler
     */
    @Nullable LightHandler getLightHandler();

    /**
     * Get the HUD for this scene.
     *
     * @return The hud.
     */
    HUD getHUD();

    /**
     * Get the particle handler for the scene.
     * @return The particle handler.
     */
    @Nullable ParticleHandler getParticleHandler();

    /**
     *
     */
    void unload();

    /**
     * Get the skybox.
     * @return The skybox
     */
    SkyBox getSkyBox();

    /**
     * Set the skybox.
     * @param skyBox The skybox.
     */
    void setSkyBox(SkyBox skyBox);

    /**
     * get the fog
     * @return The fog
     */
    Fog getFog();

    /**
     * Set the fog
     * @param fog The fog
     */
    void setFog(Fog fog);

    /**
     * Get the camera
     * @return The camera
     */
    Camera getCamera();

    /**
     * Get the event manager for the scene.
     * @since 1.0-Pre1
     * @return The event manager.
     */
    EventManager getEventManager();

    /**
     * Get the collision manager.
     * @since 1.0-Pre1
     * @return The collision manager.
     */
    @Nullable CollisionManager getCollisionManager();
}
