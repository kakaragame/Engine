package org.kakara.engine.ui.items;

import org.kakara.engine.GameHandler;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.window.Window;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.ui.UICanvas;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;

/**
 * Allows 3d objects to be rendered to the UI.
 * @since 1.0-Pre1
 */
public class ObjectCanvas implements UICanvas {
    private List<org.kakara.engine.ui.objectcanvas.UIObject> objects;

    /**
     * Create a new canvas component
     * @param scene The current scene.
     */
    public ObjectCanvas(Scene scene){
        objects = new ArrayList<>();
    }

    /**
     * Add a child component into the canvas
     * @param object The component to add.
     */
    public void add(org.kakara.engine.ui.objectcanvas.UIObject object){
        objects.add(object);
    }

    @Override
    public void init(UserInterface userInterface, GameHandler handler) {}

    @Override
    public void render(UserInterface userInterface, GameHandler handler) {
        nvgEndFrame(userInterface.getVG());
        Window win = handler.getGameEngine().getWindow();
        win.restoreState();
        handler.getGameEngine().getRenderer().renderHUD(win, objects);
        nvgBeginFrame(userInterface.getVG(), win.getWidth(), win.getHeight(), 1);
    }

    @Override
    public void cleanup(GameHandler handler) {
        for(org.kakara.engine.ui.objectcanvas.UIObject obj : objects){
            obj.getMesh().cleanUp();
        }
    }

    /**
     * Get a list of the child objects
     * @return The child objects
     */
    public List<org.kakara.engine.ui.objectcanvas.UIObject> getObjects(){
        return objects;
    }

    /**
     * Clear all of the objects
     */
    public void clearObjects(){
        objects.clear();
    }

    /**
     * Remove an object from the list.
     * @param o The object
     */
    public void removeObject(org.kakara.engine.ui.objectcanvas.UIObject o){
        objects.remove(o);
    }
}
