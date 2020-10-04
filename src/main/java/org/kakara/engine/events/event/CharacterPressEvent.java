package org.kakara.engine.events.event;

/**
 * This event allows you to get the UTF-8 code points of pressed keys.
 *
 * <p>Represents the following event from GLFW: https://www.glfw.org/docs/3.3.2/group__input.html#gab25c4a220fd8f5717718dbc487828996</p>
 */
public class CharacterPressEvent {

    private int codePoint;

    public CharacterPressEvent(int codePoint) {
        this.codePoint = codePoint;
    }

    /**
     * Grab the UTF-8 code of the pressed key.
     *
     * @return The UTF-8 code of the pressed key.
     */
    public int getCodePoint() {
        return codePoint;
    }
}
