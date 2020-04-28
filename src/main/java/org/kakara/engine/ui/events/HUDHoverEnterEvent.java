package org.kakara.engine.ui.events;

import org.kakara.engine.math.Vector2;

/**
 * When a hud item is hovered over.
 */
public interface HUDHoverEnterEvent extends UActionEvent {
    void OnHudHoverEnter(Vector2 location);
}
