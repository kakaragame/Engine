package org.kakara.engine;

import org.kakara.engine.collision.Collidable;
import org.kakara.engine.gui.Window;
import org.kakara.engine.render.Renderer;
import org.kakara.engine.scene.AbstractMenuScene;
import org.kakara.engine.utils.Time;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Primary class of the engine.
 * <p>Handles the backend, such as the window.</p>
 */
public class GameEngine implements Runnable {
    public final int TARGET_FPS = 75;
    public final int TARGET_UPS = 30;
    //WE will change this to the games logger in the impl.
    public static Logger LOGGER = LoggerFactory.getLogger(GameEngine.class);
    private final Window window;
    private final Time time;

    private final Game game;
    private Renderer renderer;
    private final GameHandler gameHandler;
    protected boolean running = true;

    private Queue<Runnable> mainThreadQueue = new LinkedList<>();

    /**
     * Create a new game.
     * @param windowTitle The title of the window.
     * @param width The width of the window
     * @param height The height of the window
     * @param vSync If the game is to use vsync.
     * @param game The main game class.
     */
    public GameEngine(String windowTitle, int width, int height, boolean vSync, Game game) {
        this.window = new Window(windowTitle, width, height, true, vSync);
        time = new Time();
        this.game = game;
        this.renderer = new Renderer();
        this.gameHandler = new GameHandler(this);
    }

    /**
     * The main game loop.
     */
    @Override
    public void run() {
        try {
            init();
            gameLoop();
        } finally {
            cleanup();
        }

    }

    /**
     * When the engine is first started
     */
    protected void init() {
        window.init();
        try {
            renderer.init();
            time.init();
            game.start(gameHandler);
            gameHandler.init();
        } catch (Exception ex) {
            ex.printStackTrace();
        }

    }

    /**
     * The actual game loop.
     */
    protected void gameLoop() {
        float elapsedTime;
        float accumulator = 0f;
        float interval = 1f / TARGET_UPS;

        while (running && !window.windowShouldClose()) {
            elapsedTime = time.getElapsedTime();
            Time.deltaTime = elapsedTime;
            accumulator += elapsedTime;

            input();

            while (accumulator >= interval) {
                update(interval);
                accumulator -= interval;
            }

            render();

            if (!window.isvSync()) {
                sync();
            }
        }
    }

    private void sync() {
        float loopSlot = 1f / TARGET_FPS;
        double endTime = time.getLastLoopTime() + loopSlot;
        while (time.getTime() < endTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ie) {
                ie.printStackTrace();
            }
        }
    }

    protected void input() {
    }

    /**
     * Updates for the game logic.
     * @param interval
     */
    protected void update(float interval) {
        gameHandler.update();
        gameHandler.getSceneManager().getCurrentScene().update(interval);
        game.update();
        collide();
    }

    /**
     * Updates for rendering.
     */
    protected void render() {
        gameHandler.getSceneManager().renderCurrentScene();
        window.update();
        /*
            Check 3 times every frame that way the cpu is not overloaded, and the queue moves faster.
            TODO Find a better way to do this.
         */
        if(!mainThreadQueue.isEmpty())
            mainThreadQueue.poll().run();
        if(!mainThreadQueue.isEmpty())
            mainThreadQueue.poll().run();
        if(!mainThreadQueue.isEmpty())
            mainThreadQueue.poll().run();
    }

    /**
     * Cleanup all of the memory
     * TODO This needs to be improved in the future.
     */
    protected void cleanup() {
        if(gameHandler.getSceneManager().getCurrentScene() instanceof AbstractMenuScene) return;
        if(gameHandler.getSceneManager().getCurrentScene() == null) return;
        renderer.cleanup();
        if(gameHandler.getSceneManager().getCurrentScene()== null) return;
        gameHandler.getSceneManager().getCurrentScene().getItemHandler().cleanup();
    }

    /**
     * Handles collision updates.
     */
    protected void collide() {
        for (Collidable gi : gameHandler.getCollisionManager().getCollidngItems(null)) {
            gi.getCollider().update();
        }
    }

    /**
     * Get the window for the game.
     * @return The window
     */
    public final Window getWindow() {
        return window;
    }

    /**
     * Get the renderer for the game.
     * @return The renderer
     */
    public final Renderer getRenderer() {
        return renderer;
    }

    /**
     * Get the gamehandler for the engine
     * @return
     */
    public GameHandler getGameHandler() {
        return gameHandler;
    }

    /**
     * Reset the render. (To be used when the scene is changed).
     * @throws Exception
     */
    public void resetRender() throws Exception {
        renderer = new Renderer();
        renderer.init();
    }

    /**
     * Add an item to the main thread queue.
     * <p>This is used heavily by the engine, it is recommended you create your own version of this for use.</p>
     * @param run The runnable to be executed.
     */
    public void addQueueItem(Runnable run){
        mainThreadQueue.add(run);
    }


}
