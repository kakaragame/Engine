package org.kakara.engine.scene;

import org.kakara.engine.Camera;
import org.kakara.engine.GameHandler;
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
    ItemHandler getItemHandler();

    /**
     * Get the LightHandler for this scene.
     *
     * @return The light handler
     */
    LightHandler getLightHandler();

    /**
     * Get the HUD for this scene.
     *
     * @return The hud.
     */
    HUD getHUD();

    ParticleHandler getParticleHandler();

    void unload();

    SkyBox getSkyBox();

    void setSkyBox(SkyBox skyBox);

    Fog getFog();

    void setFog(Fog fog);

    Camera getCamera();
}
