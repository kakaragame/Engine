package org.kakara.engine.ui.events;

import org.kakara.engine.input.MouseClickType;
import org.kakara.engine.math.Vector2;

/**
 * When an item is clicked on the hud.
 *
 * @since 1.0-Pre3
 */
public interface UIReleaseEvent extends UActionEvent {
    void onHUDRelease(Vector2 location, MouseClickType clickType);
}
