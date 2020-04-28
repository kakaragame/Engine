package org.kakara.engine.events.event;

import static org.lwjgl.glfw.GLFW.glfwGetKeyName;

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

    public int getKeyCode(){
        return this.keycode;
    }

    public String getKeyName(){
        return glfwGetKeyName(keycode, 0);
    }
}
