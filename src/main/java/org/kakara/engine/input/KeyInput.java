package org.kakara.engine.input;

import org.kakara.engine.GameEngine;
import org.kakara.engine.events.event.KeyPressEvent;

import static org.lwjgl.glfw.GLFW.GLFW_PRESS;
import static org.lwjgl.glfw.GLFW.glfwSetKeyCallback;

/**
 * Handles Key input.
 */
public class KeyInput {

    private GameEngine engine;
    public KeyInput(GameEngine engine){
        this.engine = engine;
    }

    /**
     * When the engine is first started.
     */
    public void init(){
        glfwSetKeyCallback(engine.getWindow().getWindowHandler(), (window, key, scancode, action, mods)->{
            if(action != GLFW_PRESS) return;
            engine.getGameHandler().getEventManager().fireHandler(new KeyPressEvent(key));
        });
    }

    /**
     * If a key is currently pressed.
     * @param keycode The keycode value.
     * @return If it is pressed.
     */
    public boolean isKeyPressed(int keycode){
        return engine.getWindow().isKeyPressed(keycode);
    }
}
