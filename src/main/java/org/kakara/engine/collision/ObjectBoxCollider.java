package org.kakara.engine.collision;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.KMath;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.utils.Time;

/**
 * Gives an objects a cube collision box that is automatically scaled around the object.
 * <p>This type works best for primitives, like a cube.</p>
 * <p>For a custom size collision box see: {@link BoxCollider}</p>
 */
public class ObjectBoxCollider implements Collider {

    boolean useGravity;
    boolean isTrigger;
    float gravity;

    private boolean isInAir = false;
    private float timeInAir;

    private Vector3 lastPosition;
    private Vector3 deltaPosition;
    private Collidable item;
    private GameHandler handler;

    public ObjectBoxCollider(boolean useGravity, boolean isTrigger, float gravity){
        this.useGravity = useGravity;
        this.isTrigger = isTrigger;
        this.handler = GameHandler.getInstance();
        this.gravity = gravity;
    }

    public ObjectBoxCollider(boolean useGravity, boolean isTrigger){
        this(useGravity, isTrigger, 0.07f);
    }

    public ObjectBoxCollider(){
        this(false, false, 0.07f);
    }

    public boolean usesGravity(){
        return useGravity;
    }

    public Collider setUseGravity(boolean value){
        this.useGravity = value;
        return this;
    }

    @Override
    public Vector3 getRelativePoint1() {
        return new Vector3(0, 0, 0);
    }

    @Override
    public Vector3 getAbsolutePoint1() {
        return item.getColPosition();
    }

    @Override
    public Vector3 getRelativePoint2() {
        return new Vector3(item.getColScale(), item.getColScale(), item.getColScale());
    }

    @Override
    public Vector3 getAbsolutePoint2() {
        return item.getColPosition().add(item.getColScale(), item.getColScale(), item.getColScale());
    }

    public Collider setTrigger(boolean value){
        this.isTrigger = value;
        return this;
    }

    public float getGravity(){
        return gravity;
    }

    @Override
    public float getGravityVelocity() {
        if(timeInAir < 1f) return getGravity();
        return getGravity() * timeInAir;
    }

    public void setGravity(float gravity){
        this.gravity = gravity;
    }

    public boolean isTrigger(){
        return isTrigger;
    }

    @Override
    public void update() {
        if(isTrigger) return;
        this.deltaPosition = item.getColPosition().clone().subtract(this.lastPosition);
        this.lastPosition = item.getColPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;
        for(Collidable gi : cm.getCollidngItems(item.getColPosition())){
            if(gi == item) continue;
            if(cm.isColliding(gi, item)){
                Vector3 currentPosition = item.getColPosition().subtract(deltaPosition);
                this.lastPosition = currentPosition;
                item.setColPosition(new Vector3(currentPosition.x, currentPosition.y, currentPosition.z));

            }
        }
        // If gravity is enabled move it by the gravitational velocity.
        if(useGravity){
            item.colTranslateBy(new Vector3(0, -getGravityVelocity(), 0));
        }

        boolean found = false;
        // Handle collision for gravity.
        for(Collidable gi : cm.getCollidngItems(item.getColPosition())){
            // Prevent object from colliding with itself.
            if(gi == item) continue;
            // If the object is not colliding, then prevent further calculations.
            if(!cm.isColliding(gi, item)) continue;
            // Check to see if it is possible for the object to collide. If not stop calculations.
            if(KMath.distance(gi.getColPosition(), item.getColPosition()) > 20) continue;
            //The bottom collision point of this object.
            Vector3 point1 = KMath.distance(this.getAbsolutePoint1(), item.getColPosition()) > KMath.distance(this.getAbsolutePoint2(), item.getColPosition()) ? item.getCollider().getAbsolutePoint2() : item.getCollider().getAbsolutePoint1();
            // The top collision point of the colliding object.
            Vector3 point2 = KMath.distance(gi.getCollider().getAbsolutePoint1(), gi.getColPosition()) < KMath.distance(gi.getCollider().getAbsolutePoint2(), gi.getColPosition()) ? gi.getCollider().getAbsolutePoint2() : gi.getCollider().getAbsolutePoint1();

            // Negate x and z.
            point1.x = 0;
            point1.z = 0;
            point2.x = 0;
            point2.z = 0;
            if(KMath.distance(point1, point2) <= getGravityVelocity()){
                isInAir = false;
                found = true;
                // Undo last gravitational action.
                item.colTranslateBy(new Vector3(0, getGravityVelocity(), 0));
            }
        }
        // If no collision actions are done then it is in the air.
        if(!found)
            isInAir = true;

        if(isInAir)
            timeInAir += Time.deltaTime;
        else
            timeInAir = 0;
    }

    @Override
    public void onRegister(Collidable item) {
        this.item = item;
        lastPosition = new Vector3(0, 0, 0);
        deltaPosition = new Vector3(0, 0, 0);
    }
}
