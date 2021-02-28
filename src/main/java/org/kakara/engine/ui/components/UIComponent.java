package org.kakara.engine.ui.components;

import org.jetbrains.annotations.Nullable;
import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.properties.Tagable;
import org.kakara.engine.ui.UICanvas;
import org.kakara.engine.ui.UIListener;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.constraints.Constraint;
import org.kakara.engine.ui.events.UActionEvent;

import java.util.List;

/**
 * Like the GameItem system, UIComponents are used to make up the User Interface.
 *
 * <p>UIComponents are attached to a Canvas instead of GameItems. Examples of UIComponents are: Text, Images,
 * Squares, etc. The basic function of a UIComponent is implemented in {@link GeneralUIComponent}.</p>
 *
 * <p>UIComponents can also be added to each other as a hierarchy. Components attached to each other will be
 * positioned and scale based upon their parent component.</p>
 *
 * <code>
 * ComponentCanvas cc = new ComponentCanvas(); <br>
 * <br>
 * Panel panel = new Panel(); <br>
 * Text text = new Text("This is my Text!", font); <br>
 * panel.add(text); <br>
 * cc.add(panel); <br>
 * <br>
 * add(cc); <br>
 * </code>
 *
 * <p>Then the position of Text is based upon the position of Panel. So if panel is at (500, 500), The GLOBAL position
 * of Text would also be (500, 500). Then setting text to a position of (10, 10) will make its GLOBAL position
 * (510, 510).</p>
 *
 * <p>The scaling of a UIComponent depends of the Canvas's scale mode. The engine allows you to set a canvas
 * to automatically scale with the size of the Window. This means that everything will scale up or down when the window
 * scales up or down. When a Canvas is in scale mode the top-left corner is (0, 0) and the bottom-right corner
 * is (1080, 720). That will ALWAYS be the case while in scale mode.</p>
 *
 * <p>If the Canvas's scale mode is set to none, then the UIComponents will not be automatically be scaled or repositioned
 * based upon the the size of the Window. As such the coordinate system is then based upon the current size of the window.
 * So if the size of the window is 1080 x 720, that means the top-left corner is (0, 0) and the bottom-right corner
 * is (1080, 720). If the size of the windows if 500 x 500, that means the bottom-right corner is (500, 500).</p>
 *
 * <p>Sometimes it is useful to position Components based upon the position of other Components. Constraints can
 * be used for that purpose. There are many types of constrains: {@link org.kakara.engine.ui.constraints.GeneralConstraint},
 * {@link org.kakara.engine.ui.constraints.GridConstraint}, {@link org.kakara.engine.ui.constraints.HorizontalCenterConstraint},
 * and {@link org.kakara.engine.ui.constraints.VerticalCenterConstraint}.</p>
 *
 * <p>{@link org.kakara.engine.ui.constraints.GeneralConstraint} is the major one that allows you to position a
 * UIComponent based upon another one.</p>
 */
public interface UIComponent extends Tagable, UIListener {

    /**
     * Please use {@link UIListener#addUActionEvent(Class, UActionEvent)}
     * Deprecated to use a consistent method
     *
     * @param uae   the action event to
     * @param clazz the clazz
     * @deprecated To be removed in the future.
     */
    @Deprecated
    default void addUActionEvent(UActionEvent uae, Class<? extends UActionEvent> clazz) {
        addUActionEvent(clazz, uae);
    }

    /**
     * Add a child component to this component
     *
     * @param component Child component
     */
    void add(UIComponent component);

    /**
     * Internal Use Only
     *
     * @param relativePosition the relative position
     * @param userInterface    the user interface
     * @param handler          handler
     */
    void render(Vector2 relativePosition, UserInterface userInterface, GameHandler handler);

    /**
     * Internal Use Only
     *
     * @param userInterface the user interface
     * @param handler       the handler
     */
    void init(UserInterface userInterface, GameHandler handler);

    /**
     * Internal Use Only
     *
     * @param handler gameHandler
     */
    void cleanup(GameHandler handler);

    /**
     * Set the position of the element
     *
     * @param x The x value
     * @param y The y value
     */
    void setPosition(float x, float y);

    /**
     * Get the position of the element
     *
     * @return The position
     */
    Vector2 getPosition();

    /**
     * Set the position of the element
     *
     * @param pos The Vector 2
     */
    void setPosition(Vector2 pos);

    /**
     * Set the scale of the element
     *
     * @param x x scale
     * @param y y scale
     */
    void setScale(float x, float y);

    /**
     * Get the scale
     *
     * @return The scale
     */
    Vector2 getScale();

    /**
     * Set the scale of the element
     *
     * @param scale The scale in a Vector2
     */
    void setScale(Vector2 scale);

    /**
     * Check if the element is currently visible
     *
     * @return If the element is currently visible.
     */
    boolean isVisible();

    /**
     * Set if the element is currently visible
     *
     * @param visible Visibility of the element.
     */
    void setVisible(boolean visible);

    /**
     * Get all child components
     *
     * @return The list of child components
     */
    List<UIComponent> getChildren();

    /**
     * Clear the component of all children.
     */
    void clearChildren();

    /**
     * Remove a component from the list of children.
     *
     * @param component The component to remove.
     */
    void remove(UIComponent component);

    /**
     * Get the parent of the component
     *
     * @return The parent of the component (Null if none or the parent is the canvas component).
     * @since 1.0-Pre1
     */
    UIComponent getParent();

    /**
     * Set the parent of the component
     * <p><b>Internal Use Only!</b></p>
     *
     * @param parent The parent.
     * @since 1.0-Pre1
     */
    void setParent(@Nullable UIComponent parent);

    /**
     * Get the parent canvas.
     *
     * @return The parent canvas.
     */
    UICanvas getParentCanvas();

    /**
     * Set the parent canvas.
     * <p>Internal use only.</p>
     *
     * @param canvas The parent canvas.
     */
    void setParentCanvas(UICanvas canvas);

    /**
     * Add a constraint to the component
     *
     * @param constraint The property to add.
     * @since 1.0-Pre1
     */
    void addConstraint(Constraint constraint);

    /**
     * Remove a constraint from the component
     *
     * @param constraint The property class to remove.
     * @since 1.0-Pre1
     */
    void removeConstraint(Class<Constraint> constraint);

}
