package org.kakara.engine.input.controller.ids;

import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * All of the Button IDs for Controllers.
 *
 * <p>This just wraps around GLFW static fields to provide ane easy way
 * to access values without importing lwjgl.</p>
 *
 * <p>Some values might be renamed from their GLFW counterpart to make more sense.</p>
 */
public final class GamePadButton {
    public static final int A = GLFW_GAMEPAD_BUTTON_A;
    public static final int B = GLFW_GAMEPAD_BUTTON_B;
    public static final int X = GLFW_GAMEPAD_BUTTON_X;
    public static final int Y = GLFW_GAMEPAD_BUTTON_Y;
    public static final int LEFT_BUMPER = GLFW_GAMEPAD_BUTTON_LEFT_BUMPER;
    public static final int RIGHT_BUMPER = GLFW_GAMEPAD_BUTTON_RIGHT_BUMPER;
    public static final int BACK = GLFW_GAMEPAD_BUTTON_BACK;
    public static final int START = GLFW_GAMEPAD_BUTTON_START;
    public static final int GUIDE = GLFW_GAMEPAD_BUTTON_GUIDE;
    public static final int LEFT_STICK_PUSH = GLFW_GAMEPAD_BUTTON_LEFT_THUMB;
    public static final int RIGHT_STICK_PUSH = GLFW_GAMEPAD_BUTTON_RIGHT_THUMB;
    public static final int DPAD_UP = GLFW_GAMEPAD_BUTTON_DPAD_UP;
    public static final int DPAD_RIGHT = GLFW_GAMEPAD_BUTTON_DPAD_RIGHT;
    public static final int DPAD_DOWN = GLFW_GAMEPAD_BUTTON_DPAD_DOWN;
    public static final int DPAD_LEFT = GLFW_GAMEPAD_BUTTON_DPAD_LEFT;
    public static final int LAST = DPAD_LEFT;
    public static final int CROSS = A;
    public static final int CIRCLE = B;
    public static final int SQUARE = X;
    public static final int TRIANGLE = Y;

    private static final List<String> names = Arrays.asList("A", "B", "X", "Y", "Left Bumper",
            "Right Bumper", "Back", "Start", "Guide", "Left Stick Push", "Right Stick Push",
            "DPAD Up", "DPAD Right", "DPAD Down", "DPAD Left");

    /**
     * Get the name of the button from the ID.
     *
     * @param id The id of the button.
     * @return The name of the button.
     */
    public static String getNameFromID(int id) {
        return names.get(id);
    }
}
