package org.kakara.engine.input.controller;

import org.kakara.engine.GameHandler;
import org.lwjgl.glfw.GLFWGamepadState;

import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * The representation of a physical video game controller.
 *
 * <p>A controller can be obtained through {@link ControllerManager}.</p>
 *
 * <p>Note: A bug currently exists where the Nintendo Switch Pro-Controller does not work
 * properly.</p>
 */
public class Controller {
    private final int controllerID;
    private final String controllerName;
    private final List<Integer> buttonDown;
    private final GLFWGamepadState state;

    /**
     * Constructing a controller.
     *
     * @param controllerID The controller id. (A number 0 - 15).
     */
    protected Controller(int controllerID) {
        this.controllerID = controllerID;
        this.controllerName = glfwGetGamepadName(controllerID);
        this.buttonDown = new ArrayList<>();
        this.state = GLFWGamepadState.create();
    }

    /**
     * Update the controller so the event system works.
     */
    protected void update() {
        glfwGetGamepadState(controllerID, state);
        for (int i = 0; i < 15; i++) {
            int st = state.buttons(i);
            if (st == GLFW_PRESS && !buttonDown.contains(i)) {
                buttonDown.add(i);
                GameHandler.getInstance().getCurrentScene().getEventManager()
                        .fireHandler(new GamePadButtonEvent(this, i));
            } else if (st == GLFW_RELEASE && buttonDown.contains(i)) {
                buttonDown.remove((Integer) i);
            }
        }
    }

    /**
     * Get the controller ID.
     * <p>This is a value between 0-15</p>
     *
     * @return The controller ID.
     */
    public int getControllerID() {
        return controllerID;
    }

    /**
     * Get the controller name.
     * <p>This depends based upon the controller plugged in. It might
     * not return the official name.</p>
     *
     * @return The controller name.
     */
    public String getControllerName() {
        return controllerName;
    }

    /**
     * Check if a button is held down.
     * <p>Note: triggers are considered an axis, not button.</p>
     *
     * @param buttonID The ID of the button.
     * @return If the button is being held down.
     */
    public boolean isButtonDown(int buttonID) {
        glfwGetGamepadState(controllerID, state);
        return state.buttons(buttonID) == GLFW_PRESS;
    }

    /**
     * Get an axis from the controller.
     *
     * @param axisID The id of an axis.
     * @return The value of the axis. (|value| is always > 0.05 and < 1. In other conditions it is
     * perfectly 0).
     */
    public float getAxis(int axisID) {
        glfwGetGamepadState(controllerID, state);
        float axis = state.axes(axisID);
        // GLFW has a bad time with the axis not being perfectly 0.
        return axis > -0.05 && axis < 0.05 ? 0 : axis;
    }
}
