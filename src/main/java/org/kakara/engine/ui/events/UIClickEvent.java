package org.kakara.engine.ui.events;

import org.kakara.engine.input.MouseClickType;
import org.kakara.engine.math.Vector2;

/**
 * When an item is clicked on the hud.
 */
public interface UIClickEvent extends UActionEvent {
    void OnHUDClick(Vector2 location, MouseClickType clickType);
}
