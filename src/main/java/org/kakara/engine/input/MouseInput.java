package org.kakara.engine.input;

import org.joml.Vector2d;
import org.kakara.engine.GameHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.gui.Window;
import org.kakara.engine.math.Vector2;
import org.lwjgl.system.MemoryStack;

import java.nio.DoubleBuffer;

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

    private GameHandler handler;

    public MouseInput(GameHandler handler){
        previousPos = new Vector2d(-1, -1);
        currentPos = new Vector2d(0, 0);
        this.handler = handler;
    }

    /**
     * When the game engine first starts up
     * @param window The physical window
     */
    public void init(Window window){
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
        glfwSetCursorEnterCallback(window.getWindowHandler(), (windowHandle, entered) -> {
            inWindow = entered;
        });
        glfwSetMouseButtonCallback(window.getWindowHandler(), (windowHandle, button, action, mode) -> {
            leftButtonPressed = button == GLFW_MOUSE_BUTTON_1 && action == GLFW_PRESS;
            rightButtonPressed = button == GLFW_MOUSE_BUTTON_2 && action == GLFW_PRESS;
            if(action != GLFW_PRESS) return;
            MouseClickType mct;
            switch (button){
                case GLFW_MOUSE_BUTTON_1:
                    mct = MouseClickType.LEFT_CLICK;
                    break;
                case GLFW_MOUSE_BUTTON_2:
                    mct = MouseClickType.RIGHT_CLICK;
                    break;
                case GLFW_MOUSE_BUTTON_3:
                    mct = MouseClickType.MIDDLE_CLICK;
                    break;
                default:
                    mct = MouseClickType.OTHER;
                    break;
            }
            handler.getSceneManager().getCurrentScene().getEventManager().fireHandler(new MouseClickEvent(this.getPosition(), mct));
        });
    }

    /**
     * On update.
     */
    public void update(){
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
     * @return If the left button is currently pressed.
     */
    public boolean isLeftButtonPressed(){
        return leftButtonPressed;
    }

    /**
     * If the right button is pressed.
     * @return If the right button is currently pressed.
     */
    public boolean isRightButtonPressed(){
        return rightButtonPressed;
    }

    /**
     * If the cursor is in the window.
     * @return if the cursor is in the window.
     */
    public boolean inWindow(){
        return this.inWindow;
    }

    /**
     * Get the position of the mouse.
     * @return The position of the mouse.
     */
    public Vector2d getPosition(){
        return currentPos;
    }

    /**
     * Get the change in position.
     * @return The change in position.
     */
    public Vector2d getDeltaPosition(){
        return new Vector2d(currentPos.x - previousPos.x, currentPos.y - previousPos.y);
//        return currentPos.min(previousPos);
    }

    /**
     * Set the current cursor position.
     * @param x The x position.
     * @param y The y position.
     */
   public void setCursorPosition(double x, double y){
        glfwSetCursorPos(handler.getWindow().getWindowHandler(), x, y);
        this.currentPos.x = x;
        this.currentPos.y = y;
        this.previousPos.x = x;
        this.previousPos.y = y;
   }

    /**
     * Get the current position
     * @return The current position
     */
   public Vector2 getCurrentPosition(){
       return new Vector2(currentPos);
   }

    /**
     * Get the previous position
     * @return The previous position
     */
   public Vector2 getPreviousPosition(){
       return new Vector2(previousPos);
   }
}
