package org.kakara.engine.scene;

import org.jetbrains.annotations.Nullable;
import org.kakara.engine.Camera;
import org.kakara.engine.GameHandler;
import org.kakara.engine.events.EventManager;
import org.kakara.engine.gameitems.ItemHandler;
import org.kakara.engine.gameitems.SkyBox;
import org.kakara.engine.gameitems.particles.ParticleHandler;
import org.kakara.engine.lighting.LightHandler;
import org.kakara.engine.physics.collision.CollisionManager;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.weather.Fog;

public interface Scene {
    /**
     * Any loading of non graphics that are needed
     */
    void work();

    /**
     * Load the graphics
     *
     * @param gameHandler The game handler.
     * @throws Exception If an error occurs.
     */
    void loadGraphics(GameHandler gameHandler) throws Exception;

    /**
     * Internal Use Only.
     * <p><b>DO NOT OVERRIDE</b></p>
     */
    void render();

    /**
     * Called every time the game updates.
     *
     * @param interval The update interval.
     */
    void update(float interval);

    /**
     * Check if the cursor is enabled.
     *
     * @return If the cursor is enabled.
     */
    boolean getCurserStatus();

    /**
     * Set if the cursor should be enabled.
     *
     * @param status Cursor enabled.
     */
    void setCurserStatus(boolean status);

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
     * Get the UserInterface for this scene.
     *
     * @return The user interface for the scene.
     */
    UserInterface getUserInterface();

    /**
     * Get the particle handler for the scene.
     *
     * @return The particle handler.
     */
    @Nullable ParticleHandler getParticleHandler();


    void handleException(Exception exception);

    /**
     *
     */
    void unload();

    /**
     * Get the skybox.
     *
     * @return The skybox
     */
    SkyBox getSkyBox();

    /**
     * Set the skybox.
     *
     * @param skyBox The skybox.
     */
    void setSkyBox(SkyBox skyBox);

    /**
     * get the fog
     *
     * @return The fog
     */
    Fog getFog();

    /**
     * Set the fog
     *
     * @param fog The fog
     */
    void setFog(Fog fog);

    /**
     * Get the camera
     *
     * @return The camera
     */
    Camera getCamera();

    /**
     * Get the event manager for the scene.
     *
     * @return The event manager.
     * @since 1.0-Pre1
     */
    EventManager getEventManager();

    /**
     * Get the collision manager.
     *
     * @return The collision manager.
     * @since 1.0-Pre1
     */
    @Nullable CollisionManager getCollisionManager();

    /**
     * Get the delta time.
     *
     * @return The delta time.
     * @since 1.0-Pre3
     */
    float getDeltaTime();
}
