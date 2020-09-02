package org.kakara.engine.events.event;

/**
 * This event allows you to get the UTF-8 code points of pressed keys.
 */
public class CharacterPressEvent {

    private int codepoint;

    public CharacterPressEvent(int codepoint) {
        this.codepoint = codepoint;
    }

    public int getCodePoint() {
        return codepoint;
    }
}
