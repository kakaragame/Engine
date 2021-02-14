package org.kakara.engine.input.mouse;

import org.joml.Vector2d;
import org.kakara.engine.GameHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.events.event.MouseReleaseEvent;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.window.Window;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;
import java.util.ArrayList;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

/**
 * Handles mouse inputs.
 */
public class MouseInput {
    private final Vector2d previousPos;
    private final Vector2d currentPos;
    private boolean inWindow = false;
    private boolean leftButtonPressed = false;
    private boolean rightButtonPressed = false;

    private final GameHandler handler;

    // Callbacks prevent slowdown from reflection.
    private final List<ScrollInput> scrollCallback;

    public MouseInput(GameHandler handler) {
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        this.handler = handler;

        scrollCallback = new ArrayList<>();
    }

    /**
     * When the game engine first starts up
     *
     * @param window The physical window
     */
    public void init(Window window) {
        // Sets the default currentPosition so it doesn't throw off the mouse.
        double curX;
        double curY;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer x = stack.callocDouble(1);
            DoubleBuffer y = stack.callocDouble(1);
            glfwGetCursorPos(handler.getWindow().getWindowHandler(), x, y);
            curX = x.get(0);
            curY = y.get(0);
        }
        currentPos.x = curX;
        currentPos.y = curY;
        glfwSetCursorPosCallback(window.getWindowHandler(), (windowHandle, xpos, ypos) -> {

        });
        glfwSetCursorEnterCallback(window.getWindowHandler(), (windowHandle, entered) -> inWindow = entered);
        glfwSetMouseButtonCallback(window.getWindowHandler(), (windowHandle, button, action, mode) -> {
            if(action == GLFW_PRESS){
                if(button == GLFW_MOUSE_BUTTON_1)
                    leftButtonPressed = true;
                else if(button == GLFW_MOUSE_BUTTON_2)
                    rightButtonPressed = true;

                MouseClickType mct = MouseClickType.valueOf(button);
                handler.getSceneManager().getCurrentScene().getEventManager().fireHandler(new MouseClickEvent(this.getPosition(), mct));
            }else if(action == GLFW_RELEASE){
                if(button == GLFW_MOUSE_BUTTON_1)
                    leftButtonPressed = false;
                else if(button == GLFW_MOUSE_BUTTON_2)
                    rightButtonPressed = false;

                MouseClickType mct = MouseClickType.valueOf(button);
                handler.getSceneManager().getCurrentScene().getEventManager().fireHandler(new MouseReleaseEvent(this.getPosition(), mct));
            }
        });
        // This handles when the user uses the scrollbar.
        glfwSetScrollCallback(window.getWindowHandler(), (windowHandle, xoffset, yoffset) -> {
            for (ScrollInput scrollInput : scrollCallback) {
                scrollInput.onScrollInput(xoffset, yoffset);
            }
        });
    }

    /**
     * On update.
     */
    public void update() {
        previousPos.x = currentPos.x;
        previousPos.y = currentPos.y;
        double curX;
        double curY;
        try (MemoryStack stack = MemoryStack.stackPush()) {
            DoubleBuffer x = stack.callocDouble(1);
            DoubleBuffer y = stack.callocDouble(1);
            glfwGetCursorPos(handler.getWindow().getWindowHandler(), x, y);
            curX = x.get(0);
            curY = y.get(0);
        }
        currentPos.x = curX;
        currentPos.y = curY;
    }

    /**
     * If the left button is currently pressed.
     *
     * @return If the left button is currently pressed.
     */
    public boolean isLeftButtonPressed() {
        return leftButtonPressed;
    }

    /**
     * If the right button is pressed.
     *
     * @return If the right button is currently pressed.
     */
    public boolean isRightButtonPressed() {
        return rightButtonPressed;
    }

    /**
     * If the cursor is in the window.
     *
     * @return if the cursor is in the window.
     */
    public boolean inWindow() {
        return this.inWindow;
    }

    /**
     * Get the position of the mouse.
     *
     * @return The position of the mouse.
     */
    public Vector2d getPosition() {
        return currentPos;
    }

    /**
     * Get the change in position.
     *
     * @return The change in position.
     */
    public Vector2d getDeltaPosition() {
        return new Vector2d(currentPos.x - previousPos.x, currentPos.y - previousPos.y);
    }

    /**
     * Set the current cursor position.
     *
     * @param x The x position.
     * @param y The y position.
     */
    public void setCursorPosition(double x, double y) {
        glfwSetCursorPos(handler.getWindow().getWindowHandler(), x, y);
        this.currentPos.x = x;
        this.currentPos.y = y;
        this.previousPos.x = x;
        this.previousPos.y = y;
    }

    /**
     * Get the current position
     *
     * @return The current position
     */
    public Vector2 getCurrentPosition() {
        return new Vector2(currentPos);
    }

    /**
     * Get the previous position
     *
     * @return The previous position
     */
    public Vector2 getPreviousPosition() {
        return new Vector2(previousPos);
    }

    /**
     * This will cleanup the callbacks when the scenes are changed.
     * <p>Internal use only.</p>
     */
    public void onSceneChange() {
        scrollCallback.clear();
    }

    /**
     * Add a callback for when the mouse scroll wheel is used.
     *
     * @param scrollInput The callback.
     */
    public void addScrollCallback(ScrollInput scrollInput) {
        this.scrollCallback.add(scrollInput);
    }
}
