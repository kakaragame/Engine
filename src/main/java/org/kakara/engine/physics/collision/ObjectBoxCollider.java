package org.kakara.engine.physics.collision;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.physics.OnTriggerEnter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Gives an objects a cube collision box that is automatically scaled around the object.
 * <p>This type works best for primitives, like a cube.</p>
 * <p>For a custom size collision box see: {@link BoxCollider}</p>
 */
public class ObjectBoxCollider implements Collider {

    private boolean isTrigger;
    private boolean resolveable;

    private boolean isInAir = false;
    private float timeInAir;

    private Vector3 lastPosition;
    private Vector3 deltaPosition;
    private Collidable item;
    private GameHandler handler;
    private Predicate<Collidable> predicate = gameItem -> false;
    private List<OnTriggerEnter> triggerEvents;

    public ObjectBoxCollider(boolean isTrigger, boolean resolveable) {
        this.isTrigger = isTrigger;
        this.resolveable = resolveable;
        this.handler = GameHandler.getInstance();
        this.triggerEvents = new ArrayList<>();
    }

    public ObjectBoxCollider() {
        this(false, true);
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
        if (isTrigger || !resolveable) return;
        this.deltaPosition = item.getColPosition().clone().subtract(this.lastPosition);
        this.lastPosition = item.getColPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (Collidable gi : cm.getCollidngItems(item.getColPosition())) {
            if (gi == item) continue;
            if (gi.getCollider().isTrigger()) continue;
            if (getPredicate().test(gi)) continue;
            CollisionManager.Contact contact = cm.isCollidingX(gi.getCollider(), item.getCollider());
            while (contact.isIntersecting()) {
                contact = cm.isCollidingX(gi.getCollider(), item.getCollider());
                item.setColPosition(item.getColPosition().add(new Vector3(contact.getnEnter().mul(-1).mul(contact.getPenetration()))));
            }
        }
    }

    @Override
    public void updateY() {
        if (isTrigger || !resolveable) return;
        this.deltaPosition = item.getColPosition().clone().subtract(this.lastPosition);
        this.lastPosition = item.getColPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (Collidable gi : cm.getCollidngItems(item.getColPosition())) {
            if (gi == item) continue;
            if (gi.getCollider().isTrigger()) continue;
            if (getPredicate().test(gi)) continue;
            CollisionManager.Contact contact = cm.isCollidingY(gi.getCollider(), item.getCollider());
            while (contact.isIntersecting()) {
                contact = cm.isCollidingY(gi.getCollider(), item.getCollider());
                item.setColPosition(item.getColPosition().add(new Vector3(contact.getnEnter().mul(-1).mul(contact.getPenetration()))));
            }
        }
    }

    @Override
    public void updateZ() {
        if (isTrigger || !resolveable) return;
        this.deltaPosition = item.getColPosition().clone().subtract(this.lastPosition);
        this.lastPosition = item.getColPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (Collidable gi : cm.getCollidngItems(item.getColPosition())) {
            if (gi == item) continue;
            if (gi.getCollider().isTrigger()) continue;
            if (getPredicate().test(gi)) continue;
            CollisionManager.Contact contact = cm.isCollidingZ(gi.getCollider(), item.getCollider());
            while (contact.isIntersecting()) {
                contact = cm.isCollidingZ(gi.getCollider(), item.getCollider());
                item.setColPosition(item.getColPosition().add(new Vector3(contact.getnEnter().mul(-1).mul(contact.getPenetration()))));
            }
        }
    }

    @Override
    public void addOnTriggerEnter(OnTriggerEnter enter) {
        this.triggerEvents.add(enter);
    }

    @Override
    public Predicate<Collidable> getPredicate() {
        return predicate;
    }

    @Override
    public void setPredicate(Predicate<Collidable> gameItemPredicate) {
        if (gameItemPredicate == null) {
            predicate = gameItem -> false;
            return;
        }
        predicate = gameItemPredicate;
    }

    public Collider setTrigger(boolean value) {
        this.isTrigger = value;
        return this;
    }

    public boolean isTrigger() {
        return isTrigger;
    }

    @Override
    public boolean isResolvable() {
        return resolveable;
    }

    @Override
    public void setResolvable(boolean value) {
        this.resolveable = value;
    }

    @Override
    public void update() {
        if (isTrigger || !resolveable) return;
        this.lastPosition = item.getColPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (Collidable gi : cm.getCollidngItems(item.getColPosition())) {
            if (gi == item) continue;
            if (getPredicate().test(gi)) {
                continue;
            }
            if (cm.isColliding(gi.getCollider(), item.getCollider()).isIntersecting()) {
                // Fire the trigger event.
                for (OnTriggerEnter evt : triggerEvents) {
                    evt.onTriggerEnter(gi);
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
