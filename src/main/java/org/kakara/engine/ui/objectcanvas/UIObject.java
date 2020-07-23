package org.kakara.engine.ui.objectcanvas;

import org.joml.Quaternionf;
import org.kakara.engine.item.Tagable;
import org.kakara.engine.item.mesh.IMesh;
import org.kakara.engine.math.Vector2;

import java.util.List;

/**
 * This is an object that is to be displayed on the UI.
 * <p>This is to be used with {@link org.kakara.engine.ui.items.ObjectCanvas}</p>
 * @since 1.0-Pre1
 */
public class UIObject implements Tagable {
    private IMesh mesh;
    private final Vector2 position;
    private final Quaternionf rotation;
    private float scale;

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
     * @param mesh The mesh to use.
     */
    public UIObject(IMesh mesh){
        this(mesh, new Vector2(0, 0), new Quaternionf(), 10);
    }

    /**
     * Create a new UIObject.
     * @param mesh The mesh
     * @param position The position of the object
     * @param rotation The rotation of the object
     * @param scale The scale of the object.
     */
    public UIObject(IMesh mesh, Vector2 position, Quaternionf rotation, float scale){
        this.mesh = mesh;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.rotation.rotateZ((float)Math.toRadians(180));
        this.rotation.rotateY((float)Math.toRadians(180));
    }

    /**
     * Set the position of the object
     * @param vec The position.
     */
    public void setPosition(Vector2 vec){
        setPosition(vec.x, vec.y);
    }

    /**
     * Get the position of the object.
     * @param x The X position
     * @param y The Y position
     */
    public void setPosition(float x, float y){
        position.x = x;
        position.y = y;
    }

    /**
     * Get the position of the object
     * @return The position.
     */
    public Vector2 getPosition(){
        return position.clone();
    }

    /**
     * Apply a rotation to an object.
     * <p>This adds to the current rotation.</p>
     * @param x The x value in radians
     * @param y The y value in radians
     * @param z The z value in radians
     */
    public void applyRotation(float x, float y, float z){
        rotation.rotateZ(z);
        rotation.rotateY(y);
        rotation.rotateX(x);
    }

    /**
     * Apply a rotation to an object using degrees.
     * <p>This adds to the current rotation.</p>
     * @param x The x value.
     * @param y The y value.
     * @param z The z value.
     */
    public void applyRotationDEG(float x, float y, float z){
        rotation.rotateZ((float) Math.toRadians(z));
        rotation.rotateY((float) Math.toRadians(y));
        rotation.rotateX((float) Math.toRadians(x));
    }

    /**
     * Get the rotation of object.
     * @return The rotation of the object.
     */
    public Quaternionf getRotation(){
        return rotation;
    }

    /**
     * Get the scale of the object.
     * @return The scale.
     */
    public float getScale(){
        return this.scale;
    }

    /**
     * Set the scale of the object.
     * @param scale The scale.
     */
    public void setScale(float scale){
        this.scale = scale;
    }

    /**
     * Get the mesh of the object.
     * @return The mesh.
     */
    public IMesh getMesh(){
        return mesh;
    }

    @Override
    public void setData(List<Object> data) {
        this.data = data;
    }

    @Override
    public List<Object> getData() {
        return data;
    }

    @Override
    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getTag() {
        return tag;
    }
}
