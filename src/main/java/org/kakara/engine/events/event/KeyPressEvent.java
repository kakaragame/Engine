package org.kakara.engine.events.event;

import static org.lwjgl.glfw.GLFW.glfwGetKeyName;

/**
 * When a key is pressed on the keyboard.
 */
public class KeyPressEvent {
    private final int keyCode;

    public KeyPressEvent(int keyCode) {
        this.keyCode = keyCode;
    }

    /**
     * Check to see if a certain key is pressed.
     * <p>Note: This is only for the key that triggered the event. This will not work with keys that were
     * pressed previously and are being held down.</p>
     *
     * @param keycode The key code. (See https://www.glfw.org/docs/3.3.2/group__keys.html for keycode list)
     * @return If a key code is pressed.
     */
    public boolean isKeyPressed(int keycode) {
        return this.keyCode == keycode;
    }

    /**
     * Get the key code of the key that was pressed.
     *
     * @return Return the key code. (See https://www.glfw.org/docs/3.3.2/group__keys.html for keycode list).
     */
    public int getKeyCode() {
        return this.keyCode;
    }

    /**
     * Get the name of the key.
     *
     * @return The name of the key.
     */
    public String getKeyName() {
        return glfwGetKeyName(keyCode, 0);
    }
}
