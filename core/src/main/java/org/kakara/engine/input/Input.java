package org.kakara.engine.input;

import org.kakara.engine.GameHandler;
import org.kakara.engine.input.key.KeyCode;

/**
 * Simple utility class to have all input options in one area.
 *
 * <p>This should be you go to choice for simple user input.</p>
 *
 * <code>
 * if(Input.isKeyDown(KeyCode.W)) { /&#42; Code &#42;/}<br>
 * if(Input.isGamePadButtonDown(ControllerID.CONTROLLER_ONE, GamePadButton.X)) { /&#42; Code &#42;/}<br>
 * <br>
 * // Left Stick Example<br>
 * float valueX = Input.getGamePadAxis(ControllerID.CONTROLLER_ONE, GamePadAxis.LEFT_STICK_X);<br>
 * float valueY = Input.getGamePadAxis(ControllerID.CONTROLLER_ONE, GamePadAxis.LEFT_STICK_Y);<br>
 * </code>
 */
public final class Input {

    /**
     * Check if a key is being held down.
     *
     * <p>This method is the equivalent of
     * {@link org.kakara.engine.input.key.KeyInput#isKeyPressed(KeyCode)}.</p>
     *
     * @param keyCode The key code to check.
     * @return If the key is held down.
     */
    public static boolean isKeyDown(KeyCode keyCode) {
        return isKeyDown(keyCode.getID());
    }

    /**
     * Check if a key is being held down.
     *
     * <p>This method is the equivalent of
     * {@link org.kakara.engine.input.key.KeyInput#isKeyPressed(int)}</p>
     *
     * @param keyCode The key code.
     * @return If the key is held down.
     */
    public static boolean isKeyDown(int keyCode) {
        return GameHandler.getInstance().getWindow().isKeyPressed(keyCode);
    }

    /**
     * Check if a gamepad button is down.
     *
     * <p>This method is the equivalent of
     * {@link org.kakara.engine.input.controller.Controller#isButtonDown(int)}.</p>
     *
     * @param id     The ID of the controller to check. {@link org.kakara.engine.input.controller.ids.ControllerID}
     * @param button The ID of the button. {@link org.kakara.engine.input.controller.ids.GamePadButton}
     * @return If the gamepad is pressed down.
     */
    public static boolean isGamePadButtonDown(int id, int button) {
        if (!GameHandler.getInstance().getControllerManager().controllerExists(id))
            return false;
        return GameHandler.getInstance().getControllerManager().getControllerByID(id).isButtonDown(button);
    }

    /**
     * Get an axis of the Game Pad.
     *
     * <p>This method is the equivalent of
     * {@link org.kakara.engine.input.controller.Controller#getAxis(int)}.</p>
     *
     * @param id   The ID of the controller to get. {@link org.kakara.engine.input.controller.ids.ControllerID}
     * @param axis The ID of the axis. {@link org.kakara.engine.input.controller.ids.GamePadAxis}
     * @return The axis value.
     */
    public static float getGamePadAxis(int id, int axis) {
        if (!GameHandler.getInstance().getControllerManager().controllerExists(id))
            return 0.0f;
        return GameHandler.getInstance().getControllerManager().getControllerByID(id).getAxis(axis);
    }
}
