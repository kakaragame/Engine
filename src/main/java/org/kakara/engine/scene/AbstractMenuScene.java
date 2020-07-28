package org.kakara.engine.scene;

import org.kakara.engine.Camera;
import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.physics.collision.CollisionManager;
import org.kakara.engine.events.EventManager;
import org.kakara.engine.gameitems.ItemHandler;
import org.kakara.engine.gameitems.particles.ParticleHandler;
import org.kakara.engine.gameitems.SkyBox;
import org.kakara.engine.gameitems.Texture;
import org.kakara.engine.lighting.LightHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.UICanvas;
import org.kakara.engine.ui.components.GeneralComponent;
import org.kakara.engine.ui.components.Sprite;
import org.kakara.engine.ui.items.ComponentCanvas;
import org.kakara.engine.utils.Time;
import org.kakara.engine.weather.Fog;

import static org.lwjgl.opengl.GL11.glViewport;

/**
 * This abstract scene is meant for the menus only.
 * <p>For other uses see {@link AbstractGameScene}</p>
 */
public abstract class AbstractMenuScene implements Scene {

    protected final UserInterface userInterface = new UserInterface(this);
    private final EventManager eventManager = new EventManager();
    private final Camera camera = new Camera();

    private boolean mouseStatus;
    protected GameHandler gameHandler;
    private ComponentCanvas cc;

    public AbstractMenuScene(GameHandler gameHandler){
        this.gameHandler = gameHandler;
        this.eventManager.registerHandler(this);
        try{
            userInterface.init(gameHandler.getWindow());
            cc = new ComponentCanvas(this);
            userInterface.addItem(cc);
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
        userInterface.render(gameHandler.getWindow());
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
    public UserInterface getHUD() {
        return userInterface;
    }

    @Override
    public void unload() {
        userInterface.cleanup();
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

    @Override
    public EventManager getEventManager(){
        return this.eventManager;
    }

    @Override
    public CollisionManager getCollisionManager(){
        GameEngine.LOGGER.warn("There is not collision manager in this implementation of scene! Did you mean to use AbstractGameScene?");
        return null;
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
     * Add a hud item to the scene.
     * @param UICanvas The hud item to add.
     */
    public void add(UICanvas UICanvas){
        userInterface.addItem(UICanvas);
    }

    /**
     * Set the background image of the scene.
     * @param texture the texture to use.
     */
    public void setBackground(Texture texture){
        if(cc.getComponents().size() > 0)
            cc.getComponents().get(0).cleanup(gameHandler);
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
        return camera;
    }

    @Override
    public float getDeltaTime(){
        return Time.getDeltaTime();
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
    public void init(UserInterface userInterface, GameHandler handler) {
        pollInit(userInterface, handler);
    }

    @Override
    public void render(Vector2 relative, UserInterface userInterface, GameHandler handler){
        pollRender(relative, userInterface, handler);
    }

    @Override
    public void cleanup(GameHandler handler){
        sprite.cleanup(handler);
    }
}
