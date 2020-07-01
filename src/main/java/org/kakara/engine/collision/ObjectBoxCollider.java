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
            CollisionManager.Contact contact = cm.isColliding(gi.getCollider(), item.getCollider());
            while (contact.isIntersecting()){
                contact = cm.isColliding(gi.getCollider(), item.getCollider());
                item.setColPosition(item.getColPosition().add(new Vector3(contact.getnEnter().mul(-1).mul(contact.getPenetration()))));
            }
        }

        if(useGravity){
            item.colTranslateBy(new Vector3(0, -getGravityVelocity() * Time.deltaTime, 0));
            for(Collidable gi : cm.getCollidngItems(item.getColPosition())){
                if(gi == item) continue;
                CollisionManager.Contact contact = cm.isColliding(gi.getCollider(), item.getCollider());
                if(contact.isIntersecting()){
                    if(contact.getPenetration() > (getGravityVelocity() * Time.deltaTime) + 0.01 || contact.getPenetration() < (getGravityVelocity() * Time.deltaTime) - 0.01) continue;
                    item.setColPosition(item.getColPosition().add(new Vector3(contact.getnEnter().mul(-1).mul(contact.getPenetration()))));
                    break;
                }
            }
        }
    }

    @Override
    public void onRegister(Collidable item) {
        this.item = item;
        lastPosition = new Vector3(0, 0, 0);
        deltaPosition = new Vector3(0, 0, 0);
    }
}
