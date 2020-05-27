package org.kakara.engine.scene;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.kakara.engine.Camera;
import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.collision.CollisionManager;
import org.kakara.engine.events.EventManager;
import org.kakara.engine.item.GameItem;
import org.kakara.engine.item.ItemHandler;
import org.kakara.engine.item.particles.ParticleEmitter;
import org.kakara.engine.item.particles.ParticleHandler;
import org.kakara.engine.item.SkyBox;
import org.kakara.engine.lighting.LightHandler;
import org.kakara.engine.lighting.PointLight;
import org.kakara.engine.lighting.SpotLight;
import org.kakara.engine.renderobjects.RenderChunk;
import org.kakara.engine.ui.HUD;
import org.kakara.engine.ui.HUDImageCache;
import org.kakara.engine.ui.HUDItem;
import org.kakara.engine.weather.Fog;

/**
 * Primary Scene to derive from.
 * <p>This class does not take care of the rendering. If extending this class, then you must do that yourself.
 * See the source code of {@link AbstractGameScene} for assistance.</p>
 */
public abstract class AbstractScene implements Scene {
    private final ItemHandler itemHandler = new ItemHandler();
    private final LightHandler lightHandler = new LightHandler();
    private final ParticleHandler particleHandler = new ParticleHandler();
    private final EventManager eventManager = new EventManager();
    private SkyBox skyBox;

    protected final HUD hud = new HUD(this);
    private boolean mouseStatus;
    protected GameHandler gameHandler;

    private Fog fog;

    protected AbstractScene(GameHandler gameHandler) {
        this.gameHandler = gameHandler;
        this.eventManager.registerHandler(this);
        fog = Fog.NOFOG;
        try{
            hud.init(gameHandler.getWindow());
        }catch(Exception ex){
            GameEngine.LOGGER.error("Unable to load HUD", ex);
        }
    }

    @Override
    public void setCurserStatus(boolean status) {
        mouseStatus = status;
        gameHandler.getWindow().setCursorVisibility(status);
    }

    @Override
    public boolean getCurserStatus() {
        return mouseStatus;
    }

    @Override
    public ItemHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public LightHandler getLightHandler(){
        return lightHandler;
    }

    @Override
    public HUD getHUD(){
        return hud;
    }

    @Override
    public ParticleHandler getParticleHandler(){
        return particleHandler;
    }

    @Override
    public Camera getCamera(){
        return gameHandler.getCamera();
    }

    @Override
    public EventManager getEventManager(){
        return eventManager;
    }

    /**
     * Register an object to the scene event manager.
     * <p>Scenes are automatically added. Do not add the scene to this list.</p>
     * @since 1.0-Pre1
     * @param obj The object to handle.
     */
    public void registerEventObject(Object obj){
        if(obj instanceof Scene){
            GameEngine.LOGGER.warn("Unable to register event object: Scenes are automatically added to the event list.");
            return;
        }
        this.eventManager.registerHandler(obj);
    }

    /**
     * Add a game item to the scene.
     * <p>This does not work for RenderChunks. See {@link AbstractGameScene#add(RenderChunk)}</p>
     * <p>This functionality works the same as {@link org.kakara.engine.item.ItemHandler#addItem(GameItem)}</p>
     * @param gameItem The game item to add
     */
    public void add(GameItem gameItem){
        itemHandler.addItem(gameItem);
    }

    /**
     * Add a point light to the scene
     * <p>This functionality works the same as {@link org.kakara.engine.lighting.LightHandler#addPointLight(PointLight)}</p>
     * @param pointLight The point light to add
     */
    public void add(PointLight pointLight){
        lightHandler.addPointLight(pointLight);
    }

    /**
     * Add a spot light to the scene
     * <p>This functionality works the same as {@link org.kakara.engine.lighting.LightHandler#addSpotLight(SpotLight)}</p>
     * @param spotLight The spot light to add
     */
    public void add(SpotLight spotLight){
        lightHandler.addSpotLight(spotLight);
    }

    /**
     * Add a hud item to the scene.
     * <p>This functionality works the same as {@link org.kakara.engine.ui.HUD#addItem(HUDItem)}</p>
     * @param hudItem The hud item to add.
     */
    public void add(HUDItem hudItem){
        hud.addItem(hudItem);
    }

    /**
     * Add a particle to the scene.
     * <p>This functionality works the same as {@link org.kakara.engine.item.particles.ParticleHandler#addParticleEmitter(ParticleEmitter)}</p>
     * @param emitter The particle emitter to add.
     */
    public void add(ParticleEmitter emitter){
        particleHandler.addParticleEmitter(emitter);
    }

    /**
     * Remove an item from the scene
     * <p>This does not work for RenderChunks. See {@link AbstractGameScene#add(RenderChunk)}</p>
     * <p>This functionality works the same as {@link org.kakara.engine.item.ItemHandler#removeItem(GameItem)}</p>
     * @since 1.0-Pre1
     * @param item The item to remove.
     */
    public void remove(GameItem item){
        itemHandler.removeItem(item);
    }

    /**
     * Remove a point light from the scene.
     * <p>This functionality works the same as {@link org.kakara.engine.lighting.LightHandler#removePointLight(PointLight)}</p>
     * @since 1.0-Pre1
     * @param pointLight The point light to remove.
     */
    public void remove(PointLight pointLight){
        lightHandler.removePointLight(pointLight);
    }

    /**
     * Remove a spot light from the scene
     * <p>This functionality works the same as {@link org.kakara.engine.lighting.LightHandler#removeSpotLight(SpotLight)}</p>
     * @since 1.0-Pre1
     * @param spotLight The spot light to remove.
     */
    public void remove(SpotLight spotLight){
        lightHandler.removeSpotLight(spotLight);
    }

    /**
     * Remove a hud item from the scene
     * <p>This functionality works the same as {@link org.kakara.engine.ui.HUD#removeItem(HUDItem)}</p>
     * @since 1.0-Pre1
     * @param hudItem The hud item to remove.
     */
    public void remove(HUDItem hudItem){
        hud.removeItem(hudItem);
    }

    /**
     * Remove a particle emitter from the scene
     * <p>This functionality works the same as {@link org.kakara.engine.item.particles.ParticleHandler#removeParticleEmitter(ParticleEmitter)}</p>
     * @since 1.0-Pre1
     * @param particleEmitter The particle emitter to remove.
     */
    public void remove(ParticleEmitter particleEmitter){
        particleHandler.removeParticleEmitter(particleEmitter);
    }

    /**
     * Get the current skybox
     * @return The current skybox
     */
    public SkyBox getSkyBox(){
        return skyBox;
    }

    /**
     * Set the current skybox
     * @param skyBox The skybox
     */
    public void setSkyBox(@Nullable SkyBox skyBox){
        this.skyBox = skyBox;
    }

    /**
     * Get the current fog
     * @return The current fog
     */
    public Fog getFog(){
        return fog;
    }

    /**
     * Set the current fog
     * @param fog The fog (Use Fog.NOFOG to remove fog).
     */
    public void setFog(@NotNull Fog fog){
        this.fog = fog;
    }


}
