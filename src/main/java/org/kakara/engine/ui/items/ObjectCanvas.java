package org.kakara.engine.ui.items;

import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;
import org.kakara.engine.GameHandler;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.events.event.MouseReleaseEvent;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.scene.Scene;
import org.kakara.engine.ui.UICanvas;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.events.UIClickEvent;
import org.kakara.engine.ui.events.UIReleaseEvent;
import org.kakara.engine.ui.objectcanvas.UIObject;
import org.kakara.engine.window.Window;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.nanovg.NanoVG.nvgBeginFrame;
import static org.lwjgl.nanovg.NanoVG.nvgEndFrame;

/**
 * Allows 3d objects to be rendered to the UI.
 *
 * @since 1.0-Pre1
 */
public class ObjectCanvas implements UICanvas {
    private List<UIObject> objects;
    private Scene scene;
    /*
     * Tagable data
     */
    private List<Object> data;
    private String tag;

    /**
     * Create a new canvas component
     *
     * @param scene The current scene.
     */
    public ObjectCanvas(Scene scene) {
        objects = new ArrayList<>();
        this.scene = scene;
    }

    /**
     * Add a child component into the canvas
     *
     * @param object The component to add.
     */
    public void add(UIObject object) {
        objects.add(object);
    }

    @Override
    public void init(UserInterface userInterface, GameHandler handler) {
        userInterface.getScene().getEventManager().registerHandler(this);
    }

    @Override
    public void render(UserInterface userInterface, GameHandler handler) {
        nvgEndFrame(userInterface.getVG());
        Window win = handler.getGameEngine().getWindow();
        win.restoreState();
        handler.getGameEngine().getRenderer().renderHUD(win, objects, userInterface.isAutoScaled());
        nvgBeginFrame(userInterface.getVG(), win.getWidth(), win.getHeight(), 1);
    }

    @Override
    public void cleanup(GameHandler handler) {
        for (UIObject obj : objects) {
            obj.getMesh().cleanUp();
        }
    }

    /**
     * Get a list of the child objects
     *
     * @return The child objects
     */
    public List<UIObject> getObjects() {
        return objects;
    }

    /**
     * Clear all of the objects
     */
    public void clearObjects() {
        objects.clear();
    }

    /**
     * Remove an object from the list.
     *
     * @param o The object
     */
    public void removeObject(UIObject o) {
        objects.remove(o);
    }

    @EventHandler
    public void onClick(MouseClickEvent evt) {
        UIObject obj = selectGameItems(scene, new Vector3(evt.getMousePosition().x, evt.getMousePosition().y, 0));
        if (obj != null) {
            obj.triggerEvent(UIClickEvent.class, obj.getPosition(), evt.getMouseClickType());
        }
    }

    @EventHandler
    public void onRelease(MouseReleaseEvent evt){
        UIObject obj = selectGameItems(scene, new Vector3(evt.getMousePosition().x, evt.getMousePosition().y, 0));
        if (obj != null) {
            obj.triggerEvent(UIReleaseEvent.class, obj.getPosition(), evt.getMouseClickType());
        }
    }

    /**
     * Internally used to select game items.
     *
     * @param scene    The scene.
     * @param position The position.
     * @return The UIObject Selected.
     */
    private UIObject selectGameItems(Scene scene, Vector3 position) {
        UIObject selectedGameItem = null;
        float closestDistance = 10;

        Vector3f dir = new Vector3f();

        dir = scene.getCamera().getViewMatrix().positiveZ(dir).negate();

        Vector3f max = new Vector3f();
        Vector3f min = new Vector3f();
        Vector2f nearFar = new Vector2f();

        for (UIObject collidable : objects) {
            min.set(collidable.get3DPosition().toJoml());
            max.set(collidable.get3DPosition().toJoml());
            min.add(-collidable.getScale() / 2, -collidable.getScale() / 2, -collidable.getScale() / 2);
            max.add(collidable.getScale() / 2, collidable.getScale() / 2, collidable.getScale() / 2);
            if (Intersectionf.intersectRayAab(position.toJoml(), dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                selectedGameItem = collidable;
            }
        }
        return selectedGameItem;
    }

    @Override
    public List<Object> getData() {
        return data;
    }

    @Override
    public void setData(List<Object> data) {
        this.data = data;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }
}
