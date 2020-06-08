package org.kakara.engine.ui.objectcanvas;

import org.joml.Matrix4f;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.kakara.engine.GameHandler;
import org.kakara.engine.gui.Window;
import org.kakara.engine.item.Mesh;
import org.kakara.engine.math.Vector2;
import org.kakara.engine.math.Vector3;

public class UIObject {
    private Mesh mesh;
    private final Vector2 position;
    private final Quaternionf rotation;
    private float scale;

    private Matrix4f customSpace;

    public UIObject(Mesh mesh){
        this(mesh, new Vector2(0, 0), new Quaternionf(), 10);
    }

    public UIObject(Mesh mesh, Vector2 position, Quaternionf rotation, float scale){
        this.mesh = mesh;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
        this.customSpace = new Matrix4f();
        this.customSpace.translationRotateScale(position.x, position.y, 0,
                rotation.x, rotation.y, rotation.z, rotation.w,
                scale, scale, scale);
    }

    public void setPosition(Vector2 vec){
        setPosition(vec.x, vec.y);
    }

    public void setPosition(float x, float y){
        position.x = x;
        position.y = y;
    }

    public Vector2 getPosition(){
        return position.clone();
    }

    public void setRotation(float x, float y, float z){
        rotation.x = x;
        rotation.y = y;
        rotation.z = z;
    }

    public float getScale(){
        return this.scale;
    }

    public void setScale(float scale){
        this.scale = scale;
    }

    public void setRotation(Vector3 vec){
        setRotation(vec.x, vec.y, vec.z);
    }

    public Quaternionf getRotation(){
        return rotation;
    }

    public Mesh getMesh(){
        return mesh;
    }

    public Vector3 getTruePos(){
        return new Vector3(((position.x/1080) - 0.5), ((position.y/720) + (0.5)), 0);
    }

    public float getTrueScale(){
        return scale/100;
    }

    public void updateMatrix(){
        this.customSpace.identity().translationRotateScale(0, 0, 0,
                rotation.x, rotation.y, rotation.z, rotation.w,
                scale, scale, scale);
    }

    public Matrix4f getMatrix() {
        return customSpace;
    }
}
