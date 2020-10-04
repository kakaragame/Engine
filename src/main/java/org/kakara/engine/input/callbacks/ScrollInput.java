package org.kakara.engine.input.callbacks;

/**
 * This is a callback for the ScrollInput.
 * This is not an event due to the fact that Reflection can be slow and this event might
 * be called often.
 *
 * @since 1.0-Pre3
 */
public interface ScrollInput {
    /**
     * The callback for when the scroll wheel on the mouse is used.
     *
     * @param xoffset The x offset.
     * @param yoffset The y offset.
     */
    void onScrollInput(double xoffset, double yoffset);
}
