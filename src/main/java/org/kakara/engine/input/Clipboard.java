package org.kakara.engine.input;

import org.kakara.engine.window.Window;

import static org.lwjgl.glfw.GLFW.glfwGetClipboardString;
import static org.lwjgl.glfw.GLFW.glfwSetClipboardString;

/**
 * This class provides the ability to access the clipboard of the user.
 *
 * @since 1.0-Pre3
 */
public class Clipboard {
    private Window window;

    public Clipboard(Window window) {
        this.window = window;
    }

    /**
     * Get the user's clipboard.
     * <p>This method will work as long as the user's clipboard can be converted to UTF-8</p>
     *
     * @return The user's clipboard.
     */
    public String getClipboard() {
        return glfwGetClipboardString(window.getWindowHandler());
    }

    /**
     * Set the user's clipboard.
     *
     * @param text What to set the clipboard to.
     */
    public void setClipboard(String text) {
        glfwSetClipboardString(window.getWindowHandler(), text);
    }
}
