package org.kakara.engine.ui.components;

import org.kakara.engine.GameHandler;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.HUD;
import org.kakara.engine.ui.events.HUDClickEvent;
import org.kakara.engine.ui.events.HUDHoverEnterEvent;
import org.kakara.engine.ui.events.HUDHoverLeaveEvent;

/**
 * An empty UI Component to contain other components.
 */
public class Panel extends GeneralComponent {

    private boolean isHovering;
    public Panel(){
        super();
        isHovering = false;
    }

    @Override
    public void init(HUD hud, GameHandler handler) {
        pollInit(hud, handler);
        handler.getEventManager().registerHandler(this, hud.getScene());
    }

    @Override
    public void render(Vector2 relative, HUD hud, GameHandler handler){
        if(!isVisible()) return;

        pollRender(relative, hud, handler);

        boolean isColliding = HUD.isColliding(getTruePosition(), getTrueScale(), new Vector2(handler.getMouseInput().getPosition()));
        if(isColliding && !isHovering){
            isHovering = true;
            triggerEvent(HUDHoverEnterEvent.class, handler.getMouseInput().getCurrentPosition());
        }else if(!isColliding && isHovering){
            isHovering = false;
            triggerEvent(HUDHoverLeaveEvent.class, handler.getMouseInput().getCurrentPosition());
        }
    }

    @EventHandler
    public void onClick(MouseClickEvent evt){
        if(HUD.isColliding(position, scale, new Vector2(evt.getMousePosition()))){
            triggerEvent(HUDClickEvent.class, position, evt.getMouseClickType());
        }
    }
}
