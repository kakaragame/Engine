package org.kakara.engine.input.controller;

/**
 * The event that occurs when a button is pressed on the GamePad.
 *
 * <p>Register this event through the {@link org.kakara.engine.events.EventManager}.</p>
 */
public class GamePadButtonEvent {
    private final int buttonID;
    private final Controller controller;

    /**
     * The GamePad controller event.
     *
     * @param controller The controller.
     * @param buttonID   The button id.
     */
    protected GamePadButtonEvent(Controller controller, int buttonID) {
        this.controller = controller;
        this.buttonID = buttonID;
    }

    /**
     * Get the id of the button.
     *
     * @return The button id.
     */
    public int getButtonID() {
        return buttonID;
    }

    /**
     * Get the controller from the event.
     *
     * @return The controller for the event.
     */
    public Controller getController() {
        return controller;
    }
}
