package org.kakara.engine.ui.events;

import org.kakara.engine.math.Vector2;

/**
 * When the mouse leaves a hud element.
 */
public interface HUDHoverLeaveEvent extends UActionEvent {
    void OnHudHoverLeave(Vector2 location);
}
