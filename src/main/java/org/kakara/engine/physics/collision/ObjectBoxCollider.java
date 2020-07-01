package org.kakara.engine.physics.collision;

import org.kakara.engine.GameHandler;
import org.kakara.engine.item.MeshGameItem;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.utils.Time;

/**
 * Gives an objects a cube collision box that is automatically scaled around the object.
 * <p>This type works best for primitives, like a cube.</p>
 * <p>For a custom size collision box see: {@link BoxCollider}</p>
 */
public class ObjectBoxCollider implements Collider {

    boolean isTrigger;

    private boolean isInAir = false;
    private float timeInAir;

    private Vector3 lastPosition;
    private Vector3 deltaPosition;
    private Collidable item;
    private GameHandler handler;

    public ObjectBoxCollider(boolean isTrigger){
        this.isTrigger = isTrigger;
        this.handler = GameHandler.getInstance();
    }

    public ObjectBoxCollider(){
        this(false);
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

    @Override
    public void updateX() {
        if(isTrigger) return;
        this.deltaPosition = item.getColPosition().clone().subtract(this.lastPosition);
        this.lastPosition = item.getColPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for(Collidable gi : cm.getCollidngItems(item.getColPosition())){
            if(gi == item) continue;
            CollisionManager.Contact contact = cm.isCollidingX(gi.getCollider(), item.getCollider());
            while (contact.isIntersecting()){
                contact = cm.isCollidingX(gi.getCollider(), item.getCollider());
                item.setColPosition(item.getColPosition().add(new Vector3(contact.getnEnter().mul(-1).mul(contact.getPenetration()))));
            }
        }
    }

    @Override
    public void updateY() {
        if(isTrigger) return;
        this.deltaPosition = item.getColPosition().clone().subtract(this.lastPosition);
        this.lastPosition = item.getColPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for(Collidable gi : cm.getCollidngItems(item.getColPosition())){
            if(gi == item) continue;
            CollisionManager.Contact contact = cm.isCollidingY(gi.getCollider(), item.getCollider());
            while (contact.isIntersecting()){
                contact = cm.isCollidingY(gi.getCollider(), item.getCollider());
                item.setColPosition(item.getColPosition().add(new Vector3(contact.getnEnter().mul(-1).mul(contact.getPenetration()))));
            }
        }
    }

    @Override
    public void updateZ() {
        if(isTrigger) return;
        this.deltaPosition = item.getColPosition().clone().subtract(this.lastPosition);
        this.lastPosition = item.getColPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for(Collidable gi : cm.getCollidngItems(item.getColPosition())){
            if(gi == item) continue;
            CollisionManager.Contact contact = cm.isCollidingZ(gi.getCollider(), item.getCollider());
            while (contact.isIntersecting()){
                contact = cm.isCollidingZ(gi.getCollider(), item.getCollider());
                item.setColPosition(item.getColPosition().add(new Vector3(contact.getnEnter().mul(-1).mul(contact.getPenetration()))));
            }
        }
    }

    public Collider setTrigger(boolean value){
        this.isTrigger = value;
        return this;
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
    }

    @Override
    public void onRegister(Collidable item) {
        this.item = item;
        lastPosition = new Vector3(0, 0, 0);
        deltaPosition = new Vector3(0, 0, 0);
    }
}
