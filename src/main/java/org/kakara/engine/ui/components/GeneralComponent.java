package org.kakara.engine.ui.components;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.HUD;
import org.kakara.engine.ui.events.UActionEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The standard component template.
 */
public abstract class GeneralComponent implements Component {

    protected Map<UActionEvent, Class<? extends UActionEvent>> events;
    protected List<Component> components;

    boolean init = false;

    private Vector2 truePosition;
    private Vector2 trueScale;

    public Vector2 position;
    public Vector2 scale;

    private boolean isVisible;

    public GeneralComponent(){
        events = new HashMap<>();
        components = new ArrayList<>();
        position = new Vector2(0, 0);
        scale = new Vector2(0, 0);
        truePosition = new Vector2(0, 0);
        trueScale = new Vector2(0,0);
        isVisible = true;
    }

    /**
     * Add an event to a component.
     * @param uae An anonymous class.
     * @param clazz The event interface that the event is for.
     */
    @Override
    public void addUActionEvent(UActionEvent uae, Class<? extends UActionEvent> clazz){
        events.put(uae, clazz);
    }

    @Override
    public void render(Vector2 relative, HUD hud, GameHandler handler){
        for(Component c : components){
            c.render(relative.add(position), hud, handler);
        }
    }

    @Override
    public void add(Component component){
        this.components.add(component);
        if(init)
            component.init(GameHandler.getInstance().getSceneManager().getCurrentScene().getHUD(), GameHandler.getInstance());
    }

    @Override
    public void setPosition(float x, float y){
        this.position.x = x;
        this.position.y = y;
    }

    @Override
    public void setPosition(Vector2 pos){
        setPosition(pos.x, pos.y);
    }

    @Override
    public Vector2 getPosition(){
        return position.clone();
    }

    @Override
    public void setScale(float x, float y){
        this.scale.x = x;
        this.scale.y = y;
    }

    @Override
    public void setScale(Vector2 scale){
        setScale(scale.x, scale.y);
    }

    @Override
    public Vector2 getScale(){
        return scale;
    }

    /**
     * Tells the engine to update crucial information of the object for you.
     * Not calling this means certain things, like events, won't work.
     * Call this in the render method first.
     * @param relative
     * @param hud
     * @param handler
     */
    public void pollRender(Vector2 relative, HUD hud, GameHandler handler){
        this.truePosition = position.clone().add(relative);
        this.truePosition =  new Vector2(truePosition.x * ((float) handler.getWindow().getWidth()/ (float)handler.getWindow().initalWidth),
                truePosition.y * ((float) handler.getWindow().getHeight()/(float)handler.getWindow().initalHeight));
        this.trueScale = new Vector2(scale.x * ((float) handler.getWindow().getWidth()/ (float)handler.getWindow().initalWidth),
                scale.y * ((float) handler.getWindow().getHeight()/(float)handler.getWindow().initalHeight));

        for(Component cc : components){
            cc.render(relative.clone().add(position), hud, handler);
        }
    }

    /**
     * Tells the engine that the object was inited.
     * This allows the engine to handle a lot of the component hassle for you.
     */
    public void pollInit(HUD hud, GameHandler handler){
        init = true;
        for(Component cc : components){
            cc.init(hud, handler);
        }
    }

    /**
     * Get the true position of the object.
     * @return
     */
    public Vector2 getTruePosition(){
        return this.truePosition.clone();
    }

    /**
     * Get the true scale of an object.
     * @return
     */
    public Vector2 getTrueScale(){
        return this.trueScale;
    }

    /**
     * Process and call events.
     * @param clazz The type of event
     * @param objs The parameters
     */
    public <T> void triggerEvent(Class<? extends UActionEvent> clazz, T... objs){
        try{
            for (Map.Entry<UActionEvent,Class<? extends UActionEvent>> event : events.entrySet()){
                if(clazz != event.getValue()) continue;
                if(event.getValue().getMethods().length > 1) continue;
                event.getValue().getMethods()[0].invoke(event.getKey(), objs);
            }
        }
        catch (InvocationTargetException | IllegalAccessException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void setVisible(boolean visible){
        this.isVisible = visible;
    }

    @Override
    public boolean isVisible(){
        return isVisible;
    }

    @Override
    public List<Component> getChildren(){
        return components;
    }

    @Override
    public void clearChildren(){
        components.clear();
    }

    @Override
    public void remove(Component component){
        components.remove(component);
    }

}
