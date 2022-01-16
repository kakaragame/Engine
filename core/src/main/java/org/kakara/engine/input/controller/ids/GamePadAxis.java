package org.kakara.engine.input.controller.ids;

import static org.lwjgl.glfw.GLFW.*;

/**
 * All of the Axis IDs for Controllers.
 *
 * <p>This just wraps around GLFW static fields to provide ane easy way
 * to access values without importing lwjgl.</p>
 *
 * <p>Some values might be renamed from their GLFW counterpart to make more sense.</p>
 */
public final class GamePadAxis {
    public static final int LEFT_STICK_X = GLFW_GAMEPAD_AXIS_LEFT_X;
    public static final int LEFT_STICK_Y = GLFW_GAMEPAD_AXIS_LEFT_Y;
    public static final int RIGHT_STICK_X = GLFW_GAMEPAD_AXIS_RIGHT_X;
    public static final int RIGHT_STICK_Y = GLFW_GAMEPAD_AXIS_RIGHT_Y;
    public static final int LEFT_TRIGGER = GLFW_GAMEPAD_AXIS_LEFT_TRIGGER;
    public static final int RIGHT_TRIGGER = GLFW_GAMEPAD_AXIS_RIGHT_TRIGGER;
}
