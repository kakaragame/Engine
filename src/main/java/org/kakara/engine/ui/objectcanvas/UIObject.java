package org.kakara.engine.ui.objectcanvas;

import org.joml.Quaternionf;
import org.kakara.engine.gameitems.mesh.IMesh;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.properties.Tagable;
import org.kakara.engine.ui.events.UActionEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is an object that is to be displayed on the UI.
 * <p>This is to be used with {@link org.kakara.engine.ui.items.ObjectCanvas}</p>
 *
 * @since 1.0-Pre1
 */
public class UIObject implements Tagable {
    private final Vector2 position;
    private final Quaternionf rotation;
    private IMesh mesh;
    private float scale;

    private Map<UActionEvent, Class<? extends UActionEvent>> events;

    /*
     * Tagable data
     */
    private List<Object> data;
    private String tag;

    /**
     * Create a new UIObject.
     * <p>Default position of (0,0)</p>
     * <p>Default rotation of none.</p>
     * <p>Default scale of 10.</p>
     *
     * @param mesh The mesh to use.
     */
    public UIObject(IMesh mesh) {
        this(mesh, new Vector2(0, 0), new Quaternionf(), 10);
    }

    /**
     * Create a new UIObject.
     *
     * @param mesh     The mesh
     * @param position The position of the object
     * @param rotation The rotation of the object
     * @param scale    The scale of the object.
     */
    public UIObject(IMesh mesh, Vector2 position, Quaternionf rotation, float scale) {
        this.mesh = mesh;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.rotation.rotateZ((float) Math.toRadians(180));
        this.rotation.rotateY((float) Math.toRadians(180));

        this.events = new HashMap<>();
    }

    /**
     * Get the position of the object.
     *
     * @param x The X position
     * @param y The Y position
     */
    public void setPosition(float x, float y) {
        position.x = x;
        position.y = y;
    }

    /**
     * Get the position of the object
     *
     * @return The position.
     */
    public Vector2 getPosition() {
        return position.clone();
    }

    /**
     * Set the position of the object
     *
     * @param vec The position.
     */
    public void setPosition(Vector2 vec) {
        setPosition(vec.x, vec.y);
    }

    /**
     * Apply a rotation to an object.
     * <p>This adds to the current rotation.</p>
     *
     * @param x The x value in radians
     * @param y The y value in radians
     * @param z The z value in radians
     */
    public void applyRotation(float x, float y, float z) {
        rotation.rotateZ(z);
        rotation.rotateY(y);
        rotation.rotateX(x);
    }

    /**
     * Apply a rotation to an object using degrees.
     * <p>This adds to the current rotation.</p>
     *
     * @param x The x value.
     * @param y The y value.
     * @param z The z value.
     */
    public void applyRotationDEG(float x, float y, float z) {
        rotation.rotateZ((float) Math.toRadians(z));
        rotation.rotateY((float) Math.toRadians(y));
        rotation.rotateX((float) Math.toRadians(x));
    }

    /**
     * Get the rotation of object.
     *
     * @return The rotation of the object.
     */
    public Quaternionf getRotation() {
        return rotation;
    }

    /**
     * Get the scale of the object.
     *
     * @return The scale.
     */
    public float getScale() {
        return this.scale;
    }

    /**
     * Set the scale of the object.
     *
     * @param scale The scale.
     */
    public void setScale(float scale) {
        this.scale = scale;
    }

    /**
     * Add supported UAction events.
     *
     * @param clazz The UAction event class.
     * @param uae   The event.
     * @since 1.0-Pre3
     */
    public void addUActionEvent(Class<? extends UActionEvent> clazz, UActionEvent uae) {
        events.put(uae, clazz);
    }

    /**
     * Process and call events.
     *
     * @param clazz The type of event
     * @param objs  The parameters
     */
    public <T> void triggerEvent(Class<? extends UActionEvent> clazz, T... objs) {
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
     * Grab the position of the object in 3d.
     *
     * @return The 3D vector.
     * @since 1.0-Pre3
     */
    public Vector3 get3DPosition() {
        return new Vector3(getPosition().x, getPosition().y, 0);
    }

    /**
     * Get the mesh of the object.
     *
     * @return The mesh.
     */
    public IMesh getMesh() {
        return mesh;
    }

    @Override
    public List<Object> getData() {
        return data;
    }

    @Override
    public void setData(List<Object> data) {
        this.data = data;
    }

    @Override
    public String getTag() {
        return tag;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }
}
