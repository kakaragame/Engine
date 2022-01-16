package org.kakara.engine.ui;

import org.kakara.engine.GameHandler;
import org.kakara.engine.properties.Tagable;

/**
 * A UICanvas holds elements that are meant to be seen on the UI.
 *
 * <p>See {@link org.kakara.engine.ui.canvases.ComponentCanvas} and {@link org.kakara.engine.ui.canvases.ObjectCanvas}
 * for more information.</p>
 */
public interface UICanvas extends Tagable {
    /**
     * Initialize the UICanvas
     *
     * @param userInterface The user interface.
     * @param handler       The game handler.
     */
    void init(UserInterface userInterface, GameHandler handler);

    /**
     * Render the UICanvas.
     *
     * @param userInterface The user interface.
     * @param handler       The game handler.
     */
    void render(UserInterface userInterface, GameHandler handler);


    /**
     * Cleanup the UICanvas
     *
     * @param handler The game handler.
     */
    void cleanup(GameHandler handler);

    /**
     * If the canvas should automatically scale.
     *
     * <p>The auto scale bases the position cords on the default size of the window. So if by default the window size is 1080x720,
     * then the position values will go from 0 - 1080 in the x direction and 0 - 720 in the y direction. With auto scale enabled the position
     * value always point to the same area of the screen.</p>
     *
     * @return If the canvas is automatically scaled.
     */
    boolean isAutoScale();

    /**
     * Sets if the Canvas should automatically scale according to the size of the window.
     *
     * <p>The auto scale bases the position cords on the default size of the window. So if by default the window size is 1080x720,
     * then the position values will go from 0 - 1080 in the x direction and 0 - 720 in the y direction. With auto scale enabled the position
     * value always point to the same area of the screen.</p>
     *
     * @param autoScale If the canvas should automatically scale.
     */
    void setAutoScale(boolean autoScale);
}
