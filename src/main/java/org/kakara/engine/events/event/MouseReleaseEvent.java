package org.kakara.engine.events.event;

import org.joml.Vector2d;
import org.kakara.engine.input.mouse.MouseClickType;

/**
 * When a mouse button is released.
 *
 * @since 1.0-Pre3
 */
public class MouseReleaseEvent {
    private final Vector2d position;
    private final MouseClickType mouseClickType;

    public MouseReleaseEvent(Vector2d position, MouseClickType mouseClickType) {
        this.position = position;
        this.mouseClickType = mouseClickType;
    }

    /**
     * Get the position of the mouse.
     *
     * @return The position of the mouse.
     */
    public Vector2d getMousePosition() {
        return this.position;
    }

    /**
     * Get type of mouse click.
     *
     * @return The type of mouse click.
     */
    public MouseClickType getMouseClickType() {
        return mouseClickType;
    }
}
