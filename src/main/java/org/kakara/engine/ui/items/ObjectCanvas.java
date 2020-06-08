package org.kakara.engine.ui.items;

import org.kakara.engine.GameHandler;
import org.kakara.engine.gui.Window;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.ui.HUD;
import org.kakara.engine.ui.HUDItem;
import org.kakara.engine.ui.components.Component;
import org.kakara.engine.ui.objectcanvas.UIObject;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;

/**
 * Allows 3d objects to be rendered to the UI.
 * @since 1.0-Pre1
 */
public class ObjectCanvas implements HUDItem {
    private List<UIObject> objects;
    boolean init = false;

    private Scene scene;

    /**
     * Create a new canvas component
     * @param scene The current scene.
     */
    public ObjectCanvas(Scene scene){
        objects = new ArrayList<>();
        this.scene = scene;
    }

    /**
     * Add a child component into the canvas
     * @param object The component to add.
     */
    public void add(UIObject object){
        objects.add(object);
    }

    @Override
    public void init(HUD hud, GameHandler handler) {
    }

    @Override
    public void render(HUD hud, GameHandler handler) {
        nvgEndFrame(hud.getVG());
        Window win = handler.getGameEngine().getWindow();
        win.restoreState();
        handler.getGameEngine().getRenderer().renderHUD(win, objects);
        nvgBeginFrame(hud.getVG(), win.getWidth(), win.getHeight(), 1);
    }

    @Override
    public void cleanup(GameHandler handler) {
        for(UIObject obj : objects){
            obj.getMesh().cleanUp();
        }
    }

//    /**
//     * Get a list of the child components
//     * @return The child components
//     */
//    public List<Component> getComponents(){
//        return components;
//    }
//
//    /**
//     * Clear all of the components
//     */
//    public void clearComponents(){
//        components.clear();
//    }
//
//    /**
//     * Remove a component from the list.
//     * @param c The component
//     */
//    public void removeComponent(Component c){
//        components.remove(c);
//    }
}
