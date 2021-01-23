package org.kakara.engine.physics.collision;

import org.kakara.engine.GameHandler;
import org.kakara.engine.gameitems.GameItem;
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
public class ObjectBoxCollider extends ColliderComponent {

    private boolean isTrigger;
    private boolean resolvable;

    private Vector3 lastPosition;
    private GameItem item;
    private final GameHandler handler;
    private Predicate<ColliderComponent> predicate = gameItem -> false;
    private final List<OnTriggerEnter> triggerEvents;

    // This actually conserves memory since it acts as a cache.
    private final Vector3 relativePointOne;
    private final Vector3 relativePointTwo;
    private final Vector3 absolutePointOne;
    private final Vector3 absolutePointTwo;

    public ObjectBoxCollider() {
        this.handler = GameHandler.getInstance();
        this.triggerEvents = new ArrayList<>();
        this.relativePointOne = new Vector3();
        this.relativePointTwo = new Vector3();
        this.absolutePointOne = new Vector3();
        this.absolutePointTwo = new Vector3();
    }

    @Override
    public void start() {
        this.isTrigger = false;
        this.item = getGameItem();
        lastPosition = new Vector3(0, 0, 0);
    }

    @Override
    public void update() {

    }

    @Override
    public Vector3 getRelativePoint1() {
        return relativePointOne;
    }

    @Override
    public Vector3 getAbsolutePoint1() {
        absolutePointOne.set(item.transform.getPosition());
        return absolutePointOne;
    }

    @Override
    public Vector3 getRelativePoint2() {
        relativePointTwo.set(item.transform.getScale(), item.transform.getScale(), item.transform.getScale());
        return relativePointTwo;
    }

    @Override
    public Vector3 getAbsolutePoint2() {
        absolutePointTwo.set(item.transform.getPosition().getX() + item.transform.getScale(), item.transform.getPosition().getY() + item.transform.getScale(), item.transform.getPosition().getZ() + item.transform.getScale());
        return absolutePointTwo;
    }

    @Override
    public void updateX() {
        if (isTrigger || !resolvable) return;
        this.lastPosition = item.transform.getPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (ColliderComponent gi : cm.getCollidngItems(item.transform.getPosition())) {
            if (gi == this) continue;
            if (gi.isTrigger()) continue;
            if (getPredicate().test(gi)) continue;
            CollisionManager.Contact contact = cm.isCollidingX(gi, this);
            while (contact.isIntersecting()) {
                contact = cm.isCollidingX(gi, this);
                item.transform.getPosition().addMut(contact.getnEnter().mul(-1).mul(contact.getPenetration()));
            }
        }
    }

    @Override
    public void updateY() {
        if (isTrigger || !resolvable) return;
        this.lastPosition = item.transform.getPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (ColliderComponent gi : cm.getCollidngItems(item.transform.getPosition())) {
            if (gi == this) continue;
            if (gi.isTrigger()) continue;
            if (getPredicate().test(gi)) continue;
            CollisionManager.Contact contact = cm.isCollidingY(gi, this);
            while (contact.isIntersecting()) {
                contact = cm.isCollidingY(gi, this);
                item.transform.getPosition().addMut(contact.getnEnter().mul(-1).mul(contact.getPenetration()));
            }
        }
    }

    @Override
    public void updateZ() {
        if (isTrigger || !resolvable) return;
        this.lastPosition = item.transform.getPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (ColliderComponent gi : cm.getCollidngItems(item.transform.getPosition())) {
            if (gi == this) continue;
            if (gi.isTrigger()) continue;
            if (getPredicate().test(gi)) continue;
            CollisionManager.Contact contact = cm.isCollidingZ(gi, this);
            while (contact.isIntersecting()) {
                contact = cm.isCollidingZ(gi, this);
                item.transform.getPosition().addMut(contact.getnEnter().mul(-1).mul(contact.getPenetration()));
            }
        }
    }

    @Override
    public void addOnTriggerEnter(OnTriggerEnter enter) {
        this.triggerEvents.add(enter);
    }

    @Override
    public Predicate<ColliderComponent> getPredicate() {
        return predicate;
    }

    @Override
    public void setPredicate(Predicate<ColliderComponent> gameItemPredicate) {
        if (gameItemPredicate == null) {
            predicate = gameItem -> false;
            return;
        }
        predicate = gameItemPredicate;
    }

    public ColliderComponent setTrigger(boolean value) {
        this.isTrigger = value;
        return this;
    }

    public boolean isTrigger() {
        return isTrigger;
    }

    @Override
    public boolean isResolvable() {
        return resolvable;
    }

    @Override
    public void setResolvable(boolean value) {
        this.resolvable = value;
    }

    @Override
    public void physicsUpdate(float deltaTime) {
        if (isTrigger || !resolvable) return;
        this.lastPosition = item.transform.getPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (ColliderComponent gi : cm.getCollidngItems(item.transform.getPosition())) {
            if (gi == this) continue;
            if (getPredicate().test(gi)) {
                continue;
            }
            if (cm.isColliding(gi, this).isIntersecting()) {
                // Fire the trigger event.
                for (OnTriggerEnter evt : triggerEvents) {
                    evt.onTriggerEnter(gi);
                }
            }
        }
    }
}
