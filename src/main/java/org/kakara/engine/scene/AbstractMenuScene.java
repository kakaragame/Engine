package org.kakara.engine.scene;

import org.kakara.engine.Camera;
import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.item.ItemHandler;
import org.kakara.engine.item.particles.ParticleHandler;
import org.kakara.engine.item.SkyBox;
import org.kakara.engine.item.Texture;
import org.kakara.engine.lighting.LightHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.HUD;
import org.kakara.engine.ui.HUDItem;
import org.kakara.engine.ui.components.GeneralComponent;
import org.kakara.engine.ui.components.Sprite;
import org.kakara.engine.ui.items.ComponentCanvas;
import org.kakara.engine.weather.Fog;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * This abstract scene is meant for the menus only.
 * <p>For other uses see {@link AbstractGameScene}</p>
 */
public abstract class AbstractMenuScene implements Scene {

    protected HUD hud = new HUD(this);
    private boolean mouseStatus;
    protected GameHandler gameHandler;
    private ComponentCanvas cc;

    public AbstractMenuScene(GameHandler gameHandler){
        this.gameHandler = gameHandler;
        try{
            hud.init(gameHandler.getWindow());
            cc = new ComponentCanvas(this);
            hud.addItem(cc);
        }catch(Exception ex){
            GameEngine.LOGGER.error("Unable to load HUD", ex);
        }
    }

    @Override
    public void render() {
        gameHandler.getGameEngine().getRenderer().clear();
        if (gameHandler.getWindow().isResized()) {
            glViewport(0, 0, gameHandler.getWindow().getWidth(), gameHandler.getWindow().getHeight());
            gameHandler.getWindow().setResized(false);
        }
        hud.render(gameHandler.getWindow());
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
        GameEngine.LOGGER.warn("There is no item handling in this scene implementation! Did you mean to use AbstractGameScene?");
        return null;
    }

    @Override
    public LightHandler getLightHandler() {
        GameEngine.LOGGER.warn("There is no lighting in this scene implementation! Did you mean to use AbstractGameScene?");
        return null;
    }

    @Override
    public HUD getHUD() {
        return hud;
    }

    @Override
    public void unload() {
        hud.cleanup();
    }

    @Override
    public SkyBox getSkyBox() {
        GameEngine.LOGGER.warn("There is no skybox in this implementation of scene! Did you mean to use AbstractGameScene?");
        return null;
    }

    @Override
    public void setSkyBox(SkyBox skyBox) {
        GameEngine.LOGGER.warn("There is no skybox in this implementation of scene! Did you mean to use AbstractGameScene?");
    }

    /**
     * Add a hud item to the scene.
     * @param hudItem The hud item to add.
     */
    public void add(HUDItem hudItem){
        hud.addItem(hudItem);
    }

    /**
     * Set the background image of the scene.
     * @param texture the texture to use.
     */
    public void setBackground(Texture texture){
        if(cc.getComponents().size() > 0)
            ((BackgroundImage)cc.getComponents().get(0)).cleanup();
        cc.clearComponents();
        cc.add(new BackgroundImage(texture, gameHandler));
    }


    @Override
    public void setFog(Fog fog){
        GameEngine.LOGGER.warn("There is no fog in this implementation of scene! Did you mean to use AbstractGameScene?");
    }

    @Override
    public Fog getFog(){
        GameEngine.LOGGER.warn("There is no fog in this implementation of scene! Did you mean to use AbstractGameScene?");
        return null;
    }

    @Override
    public ParticleHandler getParticleHandler(){
        GameEngine.LOGGER.warn("There are no particle in this implementation of scene! Did you mean to use AbstractGameScene?");
        return null;
    }

    @Override
    public Camera getCamera(){
        return gameHandler.getCamera();
    }
}

/**
 * INTERNAL USE ONLY
 */
class BackgroundImage extends GeneralComponent{

    private Sprite sprite;

    public BackgroundImage(Texture texture, GameHandler handler){
        sprite = new Sprite(texture, new Vector2(0, 0), new Vector2(handler.getWindow().getWidth(), handler.getWindow().getHeight()));
        this.add(sprite);
    }

    @Override
    public void init(HUD hud, GameHandler handler) {
        pollInit(hud, handler);
    }

    @Override
    public void render(Vector2 relative, HUD hud, GameHandler handler){
        pollRender(relative, hud, handler);
    }

    public void cleanup(){
        sprite.cleanup();
    }
}
