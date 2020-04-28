package org.kakara.engine.events.event;

/**
 * When a key is pressed on the keyboard.
 */
public class KeyPressEvent {
    private int keycode;
    public KeyPressEvent(int keycode){
        this.keycode = keycode;
    }

    public boolean isKeyPressed(int keycode){
        return this.keycode == keycode;
    }
}
