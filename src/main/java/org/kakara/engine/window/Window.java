package org.kakara.engine.window;

import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UserInterface;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.glfw.GLFWImage;
import org.lwjgl.glfw.GLFWVidMode;
import org.lwjgl.opengl.GL;
import org.lwjgl.system.MemoryStack;

import java.nio.IntBuffer;

import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL14.glBlendFuncSeparate;
import static org.lwjgl.system.MemoryUtil.NULL;

/**
 * This class represents the actual window created by the engine.
 *
 * <p>Use {@link GameHandler#getWindow()} or {@link GameEngine#getWindow()} to obtain the instance of this class.</p>
 */
public final class Window {

    public final int initialWidth;
    public final int initialHeight;
    private String title;
    private int width;
    private int height;
    private boolean resized;
    private boolean vSync;
    private boolean resizable;
    private final WindowOptions options;
    private boolean cursor;
    private long window;

    /**
     * Create a GLFW window.
     *
     * <p>This is constructed internally by the Kakara Engine. Get the instance of this
     * class from {@link GameHandler#getWindow()} or {@link GameEngine#getWindow()}.</p>
     *
     * @param title     The title of the window.
     * @param width     The width of the window.
     * @param height    The height of the window.
     * @param resizable If the window is resizable.
     * @param vSync     If vsync is enabled.
     */
    public Window(String title, int width, int height, boolean resizable, boolean vSync) {
        this.title = title;
        this.width = width;
        this.height = height;
        this.vSync = vSync;
        this.resized = false;
        this.resizable = resizable;
        this.cursor = true;
        this.options = new WindowOptions();

        this.initialWidth = width;
        this.initialHeight = height;
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
     * @param r Red (0-1)
     * @param b Green (0-1)
     * @param g Blue (0-1)
     * @param a Alpha (0-1)
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
     * Set the title of the window.
     *
     * @param title What the title should be.
     */
    public void setTile(String title) {
        this.title = title;
        glfwSetWindowTitle(window, title);
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
     * Set the size of the window.
     * <p>This method calls {@link #setResized(boolean)}</p>
     *
     * @param width  The width of the window. (In screen coordinates).
     * @param height The height of the window. (In screen coordinates).
     */
    public void setWindowSize(int width, int height) {
        glfwSetWindowSize(window, width, height);
        setResized(true);
    }

    /**
     * Set the size limit of the window.
     *
     * <p>See {@link #resetWindowSizeLimit()} to remove any limits.</p>
     *
     * @param minWidth  The minimum width.
     * @param minHeight The minimum height.
     * @param maxWidth  The maximum width.
     * @param maxHeight The maximum height.
     */
    public void setWindowSizeLimit(int minWidth, int minHeight, int maxWidth, int maxHeight) {
        glfwSetWindowSizeLimits(window, minWidth, minHeight, maxWidth, maxHeight);
    }

    /**
     * Reset the size limit of the window.
     */
    public void resetWindowSizeLimit() {
        glfwSetWindowSizeLimits(window, GLFW_DONT_CARE, GLFW_DONT_CARE, GLFW_DONT_CARE, GLFW_DONT_CARE);
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
     * If the window was resized
     *
     * @return if the window as resized.
     */
    public boolean isResized() {
        return resized;
    }

    /**
     * Set if the window was resized
     *
     * <p>This method is set automatically when the window is resized.</p>
     *
     * @param resized If the window was resized
     */
    public void setResized(boolean resized) {
        this.resized = resized;
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
    public boolean isCursorVisible() {
        return cursor;
    }

    /**
     * Set if the window is resizable.
     *
     * @param resizable If the window is resizable.
     */
    public void setResizable(boolean resizable) {
        this.resizable = resizable;
        glfwSetWindowAttrib(window, GLFW_RESIZABLE, resizable ? GLFW_TRUE : GLFW_FALSE);
    }

    /**
     * Get if the window is resizable.
     *
     * @return If the window is resizable.
     */
    public boolean isResizable() {
        return this.resizable;
    }

    /**
     * Set the position of the window.
     *
     * @param x The x position.
     * @param y The y position.
     */
    public void setWindowPosition(int x, int y) {
        glfwSetWindowPos(window, x, y);
    }

    /**
     * Get the position of the window.
     *
     * @return The position of the window.
     */
    public Vector2 getWindowPosition() {
        try (MemoryStack memoryStack = MemoryStack.stackPush()) {
            IntBuffer xPos = memoryStack.mallocInt(1);
            IntBuffer yPos = memoryStack.mallocInt(1);
            glfwGetWindowPos(window, xPos, yPos);

            return new Vector2(xPos.get(), yPos.get());
        }
    }

    /**
     * Set the opacity of the window.
     *
     * @param opacity The opacity of the window. (0-1)
     */
    public void setWindowOpacity(float opacity) {
        glfwSetWindowOpacity(window, opacity);
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
     *
     * @return The window handler.
     */
    public long getWindowHandler() {
        return window;
    }

    /**
     * Get all of the options for the window
     *
     * @return The options for the render.
     */
    public WindowOptions getOptions() {
        return options;
    }

    /**
     * Set the window icon.
     *
     * @param icon The icon to set.
     * @since 1.0-Pre2
     */
    public void setIcon(WindowIcon icon) {
        GLFWImage iconImage = GLFWImage.malloc();
        GLFWImage.Buffer iconBuffer = GLFWImage.malloc(1);
        iconImage.set(icon.getWidth(), icon.getHeight(), icon.getImage());
        iconBuffer.put(0, iconImage);
        glfwSetWindowIcon(window, iconBuffer);
    }

    /**
     * Set the window icon to the default.
     *
     * @since 1.0-Pre2
     */
    public void setIcon() {
        glfwSetWindowIcon(window, null);
    }

    /**
     * Restore the state of the window for normal rendering.
     * This is mainly used by the UI.
     * {@link UserInterface}
     */
    public void restoreState() {
        glEnable(GL_DEPTH_TEST);
        glEnable(GL_STENCIL_TEST);
        glEnable(GL_BLEND);
        //glBlendFunc(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA);
        glBlendFuncSeparate(GL_SRC_ALPHA, GL_ONE_MINUS_SRC_ALPHA, GL_ONE, GL_ONE);
        if (options.cullFace) {
            glEnable(GL_CULL_FACE);
            glCullFace(GL_BACK);
        }
    }
}
