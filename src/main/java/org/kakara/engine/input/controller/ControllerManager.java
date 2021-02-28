package org.kakara.engine.input.controller;

import org.kakara.engine.GameHandler;

import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

import static org.lwjgl.glfw.GLFW.*;

/**
 * This manages the controllers for the Engine.
 *
 * <p>This class can be obtained through {@link GameHandler#getControllerManager()} and
 * {@link org.kakara.engine.scene.Scene}.</p>
 */
public class ControllerManager {
    private final List<Controller> controllers;

    public ControllerManager() {
        controllers = new CopyOnWriteArrayList<>();
    }

    /**
     * Init the controller manager.
     * <p>This is to be called internally.</p>
     */
    public void init() {
        for (int i = 0; i < 16; i++) {
            if (glfwJoystickPresent(i) && glfwJoystickIsGamepad(i)) {
                controllers.add(new Controller(i));
            }
        }

        glfwSetJoystickCallback((jid, event) -> {
            if (event == GLFW_CONNECTED) {
                if (glfwJoystickIsGamepad(jid)) {
                    controllers.add(new Controller(jid));
                }
            } else if (event == GLFW_DISCONNECTED) {
                if (getControllerByID(jid) == null) return;
                Controller controller = getControllerByID(jid);
                controllers.remove(controller);
            }
        });
    }

    /**
     * Update the controller manager for the event system.
     * <p>The method is called internally.</p>
     */
    public void update() {
        for (Controller controller : controllers) {
            controller.update();
        }
    }

    /**
     * Get the controller by its ID.
     * <p>See {@link org.kakara.engine.input.controller.ids.ControllerID} for a complete
     * list of valid IDS.</p>
     *
     * @param id The ID of the controller.
     * @return The instance of the controller object.
     */
    public Controller getControllerByID(int id) {
        for (Controller controller : controllers) {
            if (controller.getControllerID() == id)
                return controller;
        }
        return null;
    }

    /**
     * Check if a controller exists.
     *
     * @param id The Controller ID.
     * @return If the controller exists at a specific id.
     */
    public boolean controllerExists(int id) {
        return glfwJoystickPresent(id);
    }

    /**
     * Get the list of currently connected controllers.
     *
     * @return The list of currently connected controllers. (Unmodifiable).
     */
    public List<Controller> getControllers() {
        return Collections.unmodifiableList(controllers);
    }
}
