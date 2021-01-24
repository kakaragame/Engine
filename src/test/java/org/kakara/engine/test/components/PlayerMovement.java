package org.kakara.engine.test.components;

import org.kakara.engine.GameHandler;
import org.kakara.engine.components.Component;
import org.kakara.engine.input.KeyInput;
import org.kakara.engine.physics.collision.PhysicsComponent;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.glfw.GLFW.GLFW_KEY_M;

/**
 * This component handles player movement.
 *
 * <p>Here is an example of the use of a component.</p>
 */
public class PlayerMovement extends Component {
    private PhysicsComponent physComp;
    private KeyInput ki;

    @Override
    public void start() {
        physComp = getComponent(PhysicsComponent.class);
        ki = GameHandler.getInstance().getKeyInput();
    }

    @Override
    public void update() {
        physComp.setVelocityX(0);
        physComp.setVelocityZ(0);
        if (ki.isKeyPressed(GLFW_KEY_RIGHT)) {
            physComp.setVelocityX(3f);
        }
        if (ki.isKeyPressed(GLFW_KEY_LEFT)) {
            physComp.setVelocityX(-3f);
        }
        if (ki.isKeyPressed(GLFW_KEY_UP)) {
            physComp.setVelocityZ(-3f);
        }
        if (ki.isKeyPressed(GLFW_KEY_DOWN)) {
            physComp.setVelocityZ(3f);
        }
        if(ki.isKeyPressed(GLFW_KEY_N)){
            getGameItem().transform.translateBy(0, 0.1f,0);
        }
        if(ki.isKeyPressed(GLFW_KEY_M)){
            getGameItem().transform.translateBy(0, -0.1f,0);
        }
    }
}
