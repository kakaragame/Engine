package org.kakara.engine.gui;

import org.kakara.engine.GameEngine;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.opengl.GLUtil;
import org.lwjgl.system.Callback;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * Handles the physical GUI window.
 */
public class Window {

    private final String title;
    private int width;
    private int height;
    private boolean resized;
    private boolean vSync;
    private boolean resizable;

    private WindowOptions options;

    private boolean cursor;

    private long window;

    public final int initalWidth;
    public final int initalHeight;

    public Window(String title, int width, int height, boolean resizable, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
        this.resizable = resizable;
        cursor = true;
        this.options = new WindowOptions();

        this.initalWidth = width;
        this.initalHeight = height;
    }

    /**
     * Initialize the window.
     */
    public void init() {
        // Make OpenGL errors get printed in the console.
        GLFWErrorCallback.createPrint().set();

        if (!glfwInit()) {
            GameEngine.LOGGER.error("Could not initialize GLFW!");
            return;
        }

        glfwSetErrorCallback((error, description) -> {
            System.err.println("GLFW error [" + Integer.toHexString(error) + "]: " + GLFWErrorCallback.getDescription(description));
        });

        glfwDefaultWindowHints();
        glfwWindowHint(GLFW_VISIBLE, GL_FALSE);
        glfwWindowHint(GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MAJOR, 3);
        glfwWindowHint(GLFW_CONTEXT_VERSION_MINOR, 2);
        glfwWindowHint(GLFW_OPENGL_PROFILE, GLFW_OPENGL_CORE_PROFILE);
        glfwWindowHint(GLFW_OPENGL_FORWARD_COMPAT, GL_TRUE);
        glfwWindowHint(GLFW_OPENGL_DEBUG_CONTEXT, GLFW_TRUE);

        if (options.antialiasing) {
            glfwWindowHint(GLFW_SAMPLES, 4);
        }

        window = glfwCreateWindow(width, height, title, NULL, NULL);
        if (window == NULL) {
            GameEngine.LOGGER.error("Could not create GLFW window");
        }

        // On window resize.
        glfwSetFramebufferSizeCallback(window, (win, width, height) -> {
            this.width = width;
            this.height = height;
            this.resized = true;
        });


        // On Key Press
        glfwSetKeyCallback(window, (win, key, scancode, action, mods) -> {
            if (key == GLFW_KEY_ESCAPE && action == GLFW_RELEASE) {
                glfwSetWindowShouldClose(win, true);
            }
        });
        GLFWVidMode vidmode = glfwGetVideoMode(glfwGetPrimaryMonitor());
        glfwSetWindowPos(window, (vidmode.width() - width) / 2, (vidmode.height() - height) / 2);

        glfwMakeContextCurrent(window);
        if (this.vSync) {
            glfwSwapInterval(1);
        }

        glfwShowWindow(window);
        boolean y = true;
        GL.createCapabilities();
//        Callback debugProc = GLUtil.setupDebugMessageCallback();

        glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);

        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
    }

    /**
     * Set the clear color of the window.
     *
     * @param r Red
     * @param b Green
     * @param g Blue
     * @param a Alpha
     */
    public void setClearColor(float r, float b, float g, float a) {
        glClearColor(r, b, g, a);
    }

    /**
     * If a key is pressed
     *
     * @param keyCode The key code
     * @return If the key is currently pressed
     */
    public boolean isKeyPressed(int keyCode) {
        return glfwGetKey(window, keyCode) == GLFW_PRESS;
    }

    /**
     * If a mouse button is pressed
     *
     * @param mouseCode The mouse code
     * @return If the mouse is currently pressed.
     */
    public boolean isMousePressed(int mouseCode) {
        return glfwGetMouseButton(window, mouseCode) == GLFW_PRESS;
    }

    /**
     * If the GLFW has been instructed to close.
     *
     * @return If the window was told to close.
     */
    public boolean windowShouldClose() {
        return glfwWindowShouldClose(window);
    }

    /**
     * Get the title of the window.
     *
     * @return The title.
     */
    public String getTitle() {
        return title;
    }

    /**
     * The width of the window.
     *
     * @return The width
     */
    public int getWidth() {
        return width;
    }

    /**
     * The height of the window.
     *
     * @return The height
     */
    public int getHeight() {
        return height;
    }

    /**
     * Set if the window was resized
     *
     * @param resized If the window was resized
     */
    public void setResized(boolean resized) {
        this.resized = resized;
    }

    /**
     * If the window was resized
     *
     * @return if the window as resized.
     */
    public boolean isResized() {
        return resized;
    }

    /**
     * If vSync is enabled
     *
     * @return if vSync is enabled
     */
    public boolean isvSync() {
        return vSync;
    }

    /**
     * Set if vSync is enabled.
     *
     * @param vSync If vSync is enabled.
     */
    public void setvSync(boolean vSync) {
        this.vSync = vSync;
    }

    /**
     * If the cursor is disabled. (Might be moved)
     *
     * @param cursor If the cursor is enabled.
     */
    public void setCursorVisibility(boolean cursor) {
        this.cursor = cursor;
        glfwSetInputMode(window, GLFW_CURSOR, cursor ? GLFW_CURSOR_NORMAL : GLFW_CURSOR_DISABLED);
    }

    /**
     * If the cursor is currently enabled.
     *
     * @return If it is enabled.
     */
    public boolean isCursorVisable() {
        return cursor;
    }

    /**
     * Each update this should be called.
     */
    public void update() {
        glfwSwapBuffers(window);
        glfwPollEvents();
    }

    /**
     * Get the window handler.
     * <p>This is the window id used by GLFW.</p>
     * @return The window handler.
     */
    public long getWindowHandler() {
        return window;
    }

    /**
     * Get all of the options for the window
     * TODO Make this actually do something.
     * @return The options for the render.
     */
    public WindowOptions getOptions() {
        return options;
    }

    /**
     * Restore the state of the window for normal rendering.
     * This is mainly used by the UI.
     * {@link org.kakara.engine.ui.HUD}
     */
    public void restoreState() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glEnable(GL_CULL_FACE);
        glCullFace(GL_BACK);
        if (options.cullFace) {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
    }
}
