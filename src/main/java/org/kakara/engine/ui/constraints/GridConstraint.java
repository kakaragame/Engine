package org.kakara.engine.ui.constraints;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.components.Component;
import org.kakara.engine.window.Window;

/**
 * Positions the component based upon a grid. (The grid is relative to the scale of the parent component).
 * <code>
 * component.addConstraint(new GridConstraint(5, 5, 2, 2));
 * </code>
 * The example above will center the component in a 5 x 5 grid.
 *
 * @since 1.0-Pre1
 */
public class GridConstraint implements Constraint {

    private int rows;
    private int columns;
    private int xpos;
    private int ypos;
    private Window window;
    private UserInterface userInterface;

    /**
     * Create a new grid constraint
     *
     * @param columns The number of columns.
     * @param rows    The number of rows.
     * @param xpos    The x position.
     *                <p>Ranges from 0 (inclusive) to the columns value (exclusive)</p>
     * @param ypos    The y position.
     *                <p>Ranges from 0 (inclusive) to the rows value (exclusive)</p>
     */
    public GridConstraint(int columns, int rows, int xpos, int ypos) {
        this.rows = rows - 1;
        this.columns = columns - 1;
        this.xpos = xpos;
        this.ypos = ypos;
    }

    @Override
    public void onAdd(Component component) {
        window = GameHandler.getInstance().getGameEngine().getWindow();
        userInterface = GameHandler.getInstance().getCurrentScene().getUserInterface();
    }

    @Override
    public void onRemove(Component component) {
    }

    @Override
    public void update(Component component) {
        Vector2 scale = component.getParent() != null ? component.getParent().getScale()
                : getWindowSize();
        component.setPosition((scale.x / columns) * xpos - component.getScale().x / 2, (scale.y / rows) * ypos - component.getScale().y / 2);
    }

    private Vector2 getWindowSize() {
        return userInterface.isAutoScaled() ? new Vector2(window.initalWidth, window.initalHeight)
                : new Vector2(window.getWidth(), window.getHeight());
    }
}
