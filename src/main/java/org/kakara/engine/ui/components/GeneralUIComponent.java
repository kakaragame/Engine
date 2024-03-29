package org.kakara.engine.ui.components;

import org.jetbrains.annotations.Nullable;
import org.kakara.engine.GameEngine;
import org.kakara.engine.GameHandler;
import org.kakara.engine.events.EventHandler;
import org.kakara.engine.events.event.MouseClickEvent;
import org.kakara.engine.events.event.MouseReleaseEvent;
import org.kakara.engine.exceptions.ui.HierarchyException;
import org.kakara.engine.input.mouse.MouseClickType;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.ui.UICanvas;
import org.kakara.engine.ui.UserInterface;
import org.kakara.engine.ui.constraints.Constraint;
import org.kakara.engine.ui.events.*;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * This is the standard implementation of the {@link UIComponent} interface. You can extend
 * this class to make your own components.
 *
 * <p>For a definition of what a UIComponent is see {@link UIComponent}.</p>
 *
 * <p>Generic events like, {@link UIClickEvent}, {@link UIReleaseEvent}, {@link UIHoverEnterEvent}, and {@link UIHoverLeaveEvent} are all automatically handled
 * by this class.</p>
 *
 * <p>When overriding the render method you want to call super.render() in order to draw child objects.</p>
 * <code>
 *
 * <br>
 * public void render(Vector2 relative, UserInterface userInterface, GameHandler handler) {<br>
 * super.render(relative, userInterface, handler);<br>
 * }<br>
 * </code>
 */
public abstract class GeneralUIComponent implements UIComponent {

    public Vector2 position;
    public Vector2 scale;

    protected Map<UActionEvent, Class<? extends UActionEvent>> events;
    protected List<UIComponent> components;
    protected List<Constraint> constraints;

    private boolean init = false;
    private UICanvas parentCanvas;
    private UIComponent parent;
    private Vector2 globalPosition;
    private Vector2 globalScale;
    private boolean isVisible;
    private boolean isHovering;

    /*
     * Tagable data
     */
    private List<Object> data;
    private String tag;

    /**
     * The standard constructor for the GeneralUIComponent.
     *
     * <p>Both position and scale are initialized to 0,0.</p>
     */
    public GeneralUIComponent() {
        events = new HashMap<>();
        components = new ArrayList<>();
        constraints = new ArrayList<>();
        position = new Vector2(0, 0);
        scale = new Vector2(0, 0);
        globalPosition = new Vector2(0, 0);
        globalScale = new Vector2(0, 0);
        isVisible = true;
        isHovering = false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void addUActionEvent(Class<? extends UActionEvent> clazz, UActionEvent uae) {
        events.put(uae, clazz);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Map<UActionEvent, Class<? extends UActionEvent>> getEvents() {
        return events;
    }

    /**
     * The default renderer for the UI Components.
     *
     * <p>If you override this then you should call this method at the top by doing super.render().</p>
     * <p>This method will render all sub-components and will take into account visibility. It will also call
     * {@link #pollRender(Vector2, UserInterface, GameHandler)} on all sub-components.</p>
     *
     * @param relative      The absolute position of the UIComponent (This is relative to the current mode).
     * @param userInterface The user interface.
     * @param handler       The game handler.
     */
    @Override
    public void render(Vector2 relative, UserInterface userInterface, GameHandler handler) {
        if (!isVisible) return;

        for (UIComponent c : components) {
            if (c instanceof GeneralUIComponent generalUIComponent)
                generalUIComponent.pollRender(relative.add(position), userInterface, handler);
            c.render(relative.add(position), userInterface, handler);
        }
    }

    /**
     * When overriding the cleanup method {@link #pollCleanup(GameHandler)} should be used.
     *
     *
     *
     * @param handler The instance of the game handler.
     *                <br>
     *                <code>
     *                public void cleanup(GameHandler handler) {<br>
     *                pollCleanup(handler);<br>
     *                }<br>
     *                </code>
     */
    @Override
    public void cleanup(GameHandler handler) {
        for (UIComponent c : components) {
            c.cleanup(handler);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void add(UIComponent component) {
        if (component.getParent() != null)
            throw new HierarchyException("That UI component already has a parent!");
        this.components.add(component);
        component.setParentCanvas(getParentCanvas());
        component.setParent(this);
        if (init)
            component.init(GameHandler.getInstance().getSceneManager().getCurrentScene().getUserInterface(), GameHandler.getInstance());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setPosition(float x, float y) {
        this.position.x = x;
        this.position.y = y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector2 getPosition() {
        return position.clone();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setPosition(Vector2 pos) {
        setPosition(pos.x, pos.y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setScale(float x, float y) {
        this.scale.x = x;
        this.scale.y = y;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final Vector2 getScale() {
        return scale;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setScale(Vector2 scale) {
        setScale(scale.x, scale.y);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final UIComponent getParent() {
        return parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setParent(@Nullable UIComponent parent) {
        this.parent = parent;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final UICanvas getParentCanvas() {
        return parentCanvas;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final void setParentCanvas(UICanvas canvas) {
        this.parentCanvas = canvas;

        for (UIComponent component : components) {
            if (component.getParentCanvas() == null)
                component.setParentCanvas(getParentCanvas());
        }
    }

    /**
     * Operates the internals of a General Component.
     *
     * <p>This method: Updates the Global Position, Global Scale, and handles constraints.</p>
     *
     * <p>This method is called for you by the ComponentCanvas and other GeneralUIComponents, there is not need to call it manually.</p>
     *
     * @param relative      The global position of the item.
     * @param userInterface The user interface instance.
     * @param handler       The game handler.
     */
    public final void pollRender(Vector2 relative, UserInterface userInterface, GameHandler handler) {
        if (parentCanvas.isAutoScale()) {
            this.globalPosition = position.clone().add(relative);
            this.globalPosition = new Vector2(globalPosition.x * ((float) handler.getWindow().getWidth() / (float) handler.getWindow().initialWidth),
                    globalPosition.y * ((float) handler.getWindow().getHeight() / (float) handler.getWindow().initialHeight));
            this.globalScale = new Vector2(scale.x * ((float) handler.getWindow().getWidth() / (float) handler.getWindow().initialWidth),
                    scale.y * ((float) handler.getWindow().getHeight() / (float) handler.getWindow().initialHeight));
        } else {
            this.globalPosition = position.clone().add(relative);
            this.globalScale = scale;
        }

        // Handle hovering events.
        boolean isColliding = UserInterface.isColliding(getGlobalPosition(), getGlobalScale(), new Vector2(handler.getMouseInput().getPosition()));
        if (isColliding && !isHovering) {
            isHovering = true;
            triggerEvent(UIHoverEnterEvent.class, handler.getMouseInput().getCurrentPosition());
        } else if (!isColliding && isHovering) {
            isHovering = false;
            triggerEvent(UIHoverLeaveEvent.class, handler.getMouseInput().getCurrentPosition());
        }

        for (Constraint cc : constraints) {
            try {
                cc.update(this);
            } catch (Exception e) {
                GameEngine.LOGGER.error("Unable to run constraint " + cc.getClass().getName() + ". In component " + toString(), e);
                // This is probably not the best and should be changed.
                throw e;
            }
        }
    }

    /**
     * Tells the engine that the object was initialized.
     * This allows the engine to handle a lot of the component hassle for you.
     *
     * @param userInterface the user interface
     * @param handler       the game handler
     */
    public final void pollInit(UserInterface userInterface, GameHandler handler) {
        init = true;
        for (UIComponent cc : components) {
            cc.init(userInterface, handler);
            if (cc.getParentCanvas() == null) {
                cc.setParentCanvas(getParentCanvas());
            }
        }

        userInterface.getScene().getEventManager().registerHandler(this);
    }

    /**
     * Allows the engine to handle the cleanup of sub components.
     *
     * @param handler The handler.
     */
    public final void pollCleanup(GameHandler handler) {
        for (UIComponent cc : components) {
            cc.cleanup(handler);
        }
    }

    /**
     * Get the GLOBAL position of the object. The global position is the true screen
     * position of the component. Even if auto-scale is enabled. This will differ from
     * {@link #getPosition()} and should be used for rendering with NanoVG.
     *
     * <p>Note: The global position is not calculated until after 1 render cycle.</p>
     *
     * @return The GLOBAL position of the object.
     */
    public final Vector2 getGlobalPosition() {
        return this.globalPosition.clone();
    }

    /**
     * Get the GLOBAL scale of an object. The global scale is the true screen scale
     * of the component. Even if auto-scale is enabled. This will differ from
     * {@link #getScale()} and should be used for rendering with NanoVG.
     *
     * <p>Note: The global scale is not calculated until after 1 render cycle</p>
     *
     * @return The GLOBAL scale of an object.
     */
    public final Vector2 getGlobalScale() {
        return this.globalScale;
    }

    /**
     * Process and call events.
     *
     * @param clazz The type of event
     * @param objs  The parameters
     */
    @SafeVarargs
    public final <T> void triggerEvent(Class<? extends UActionEvent> clazz, T... objs) {
        try {
            for (Map.Entry<UActionEvent, Class<? extends UActionEvent>> event : events.entrySet()) {
                if (clazz != event.getValue()) continue;
                if (event.getValue().getMethods().length > 1) continue;
                event.getValue().getMethods()[0].invoke(event.getKey(), objs);
            }
        } catch (InvocationTargetException | IllegalAccessException ex) {
            ex.printStackTrace();
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean isVisible() {
        return isVisible;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setVisible(boolean visible) {
        this.isVisible = visible;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<UIComponent> getChildren() {
        return components;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void clearChildren() {
        components.clear();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void remove(UIComponent component) {
        component.setParent(null);
        components.remove(component);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void addConstraint(Constraint constraint) {
        constraints.add(constraint);
        constraint.onAdd(this);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void removeConstraint(Class<Constraint> constraintClass) {
        List<Constraint> props = constraints.stream().filter(prop -> prop.getClass() == constraintClass).collect(Collectors.toList());
        if (!props.isEmpty()) {
            Constraint prop = props.get(0);
            prop.onRemove(this);
            constraints.remove(prop);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> getData() {
        return data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setData(List<Object> data) {
        this.data = data;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getTag() {
        return tag;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String toString() {
        return "GeneralUIComponent{" +
                "position=" + position +
                ", scale=" + scale +
                ", init=" + init +
                ", globalPosition=" + globalPosition +
                ", globalScale=" + globalScale +
                ", isVisible=" + isVisible +
                ", tag='" + tag + '\'' +
                ", parentcanvas='" + parentCanvas.getTag() + "\'" +
                '}';
    }

    /**
     * Detects when the UIComponent is clicked.
     * <p>Default behavior is to trigger the UIClickEvent. Override to add additional checks.</p>
     *
     * @param mousePosition  The mouse position.
     * @param mouseClickType The mouse click type.
     */
    public void onMouseClick(Vector2 mousePosition, MouseClickType mouseClickType) {
        triggerEvent(UIClickEvent.class, mousePosition, mouseClickType);
    }

    /**
     * Detects when a click on the UIComponent is released.
     * <p>Default behavior is to trigger the UIReleaseEvent. Override to add additional checks.</p>
     *
     * @param mousePosition  The mouse position.
     * @param mouseClickType The mouse click type.
     */
    public void onMouseRelease(Vector2 mousePosition, MouseClickType mouseClickType) {
        triggerEvent(UIReleaseEvent.class, mousePosition, mouseClickType);
    }

    /**
     * Detects the click event.
     *
     * @param evt The click event.
     */
    @EventHandler
    public void onEventClick(MouseClickEvent evt) {
        Vector2 mousePosition = new Vector2(evt.getMousePosition());
        if (UserInterface.isColliding(getGlobalPosition(), getGlobalScale(), mousePosition)) {
            onMouseClick(mousePosition, evt.getMouseClickType());
        }
    }

    /**
     * Detects the release event.
     *
     * @param evt The release event.
     */
    @EventHandler
    public void onEventRelease(MouseReleaseEvent evt) {
        Vector2 mousePosition = new Vector2(evt.getMousePosition());
        if (UserInterface.isColliding(getGlobalPosition(), getGlobalScale(), mousePosition)) {
            onMouseRelease(mousePosition, evt.getMouseClickType());
        }
    }
}
