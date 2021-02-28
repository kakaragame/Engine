package org.kakara.engine.physics.collision;

import org.kakara.engine.GameHandler;
import org.kakara.engine.components.Component;
import org.kakara.engine.gameitems.GameItem;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.physics.OnTriggerEnter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * The BoxCollider class is to be used on non-primitive objects that are created using the model loader.
 * It is recommended to use the relative mode to define points.
 * To define the points pick two opposite corners.
 * <p>For a collider to fits around a cube see: {@link ObjectBoxCollider}</p>
 */
public class BoxCollider extends ColliderComponent {

    private Vector3 point1;
    private Vector3 point2;
    private Vector3 offset;
    private boolean isTrigger;

    private Vector3 lastPosition;
    private GameItem item;
    private final GameHandler handler;
    private Predicate<ColliderComponent> predicate = gameItem -> false;
    private final List<OnTriggerEnter> triggerEvents;

    public BoxCollider() {
        this.handler = GameHandler.getInstance();
        this.triggerEvents = new ArrayList<>();
    }

    @Override
    public void start() {
        this.item = getGameItem();
        lastPosition = new Vector3(0, 0, 0);
        this.point1 = new Vector3();
        this.point2 = new Vector3(1, 1, 1);
        this.offset = new Vector3();
        this.isTrigger = false;
    }

    @Override
    public void update() {
    }

    @Override
    public ColliderComponent setTrigger(boolean value) {
        this.isTrigger = value;
        return this;
    }

    @Override
    public boolean isTrigger() {
        return isTrigger;
    }

    /**
     * Get the offset for this collider.
     *
     * @return The offset.
     */
    public Vector3 getOffset() {
        return offset;
    }

    /**
     * Set the offset of the collider.
     * <p>Relative points will relate to (0,0,0) minus this vector.</p>
     *
     * @param offset The offset vector
     */
    public void setOffset(Vector3 offset) {
        this.offset = offset;
    }

    @Override
    public Vector3 getRelativePoint1() {
        return point1.add(offset);
    }

    @Override
    public Vector3 getAbsolutePoint1() {
        return new Vector3(point1.x, point1.y, point1.z).addMut(offset).addMut(item.transform.getPosition());
    }

    @Override
    public Vector3 getRelativePoint2() {
        return point2.add(offset);
    }

    @Override
    public Vector3 getAbsolutePoint2() {
        return new Vector3(point2.x, point2.y, point2.z).addMut(offset).addMut(item.transform.getPosition());
    }

    @Override
    public void updateX() {
        if (isTrigger) return;
        this.lastPosition = item.transform.getPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (ColliderComponent gi : cm.getCollidngItems(item.transform.getPosition())) {
            if (gi == this) continue;
            if (gi.isTrigger()) continue;
            if (getPredicate().test(gi)) continue;
            CollisionManager.Contact contact = cm.isCollidingX(gi, item.getComponent(ColliderComponent.class));
            while (contact.isIntersecting()) {
                contact = cm.isCollidingX(gi, item.getComponent(ColliderComponent.class));
                item.transform.getPosition().addMut(contact.getnEnter().mul(-1).mul(contact.getPenetration()));
            }
        }
    }

    @Override
    public void updateY() {
        if (isTrigger) return;
        this.lastPosition = item.transform.getPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (ColliderComponent gi : cm.getCollidngItems(item.transform.getPosition())) {
            if (gi == this) continue;
            if (gi.isTrigger()) continue;
            if (getPredicate().test(gi)) continue;
            CollisionManager.Contact contact = cm.isCollidingY(gi, item.getComponent(ColliderComponent.class));
            while (contact.isIntersecting()) {
                contact = cm.isCollidingY(gi, item.getComponent(ColliderComponent.class));
                item.transform.getPosition().addMut(contact.getnEnter().mul(-1).mul(contact.getPenetration()));
            }
        }
    }

    @Override
    public void updateZ() {
        if (isTrigger) return;
        this.lastPosition = item.transform.getPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (ColliderComponent gi : cm.getCollidngItems(item.transform.getPosition())) {
            if (gi == this) continue;
            if (gi.isTrigger()) continue;
            if (getPredicate().test(gi)) continue;
            CollisionManager.Contact contact = cm.isCollidingXZ(gi, item.getComponent(ColliderComponent.class));
            while (contact.isIntersecting()) {
                contact = cm.isCollidingXZ(gi, item.getComponent(ColliderComponent.class));
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
        this.predicate = gameItemPredicate;
    }

    /**
     * Get point 1 no matter if it is relative or absolute.
     *
     * @return the vector.
     */
    public Vector3 getPoint1() {
        return point1;
    }

    /**
     * Set point 1.
     *
     * @param point1 The vector for the point.
     */
    public void setPoint1(Vector3 point1) {
        this.point1 = point1;
    }

    /**
     * Get the second point no matter if it is relative or absolute.
     *
     * @return the vector.
     */
    public Vector3 getPoint2() {
        return point2;
    }

    /**
     * Set the second point.
     *
     * @param point2 The vector.
     */
    public void setPoint2(Vector3 point2) {
        this.point2 = point2;
    }

    @Override
    public void physicsUpdate(float deltaTime) {
        if (isTrigger) return;

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (ColliderComponent gi : cm.getCollidngItems(item.transform.getPosition())) {
            if (gi == this) continue;
            if (getPredicate().test(gi)) continue;
            if (cm.isColliding(gi, item.getComponent(ColliderComponent.class)).isIntersecting()) {
                for (Component component : item.getComponents()) {
                    component.onCollision(gi);
                }
                // Fire the trigger event.
                for (OnTriggerEnter evt : triggerEvents) {
                    evt.onTriggerEnter(gi);
                }
            }
        }
    }

    @Override
    public String toString() {
        return "BoxCollider{" +
                "point1=" + point1.toString() +
                ", point2=" + point2.toString() +
                ", offset=" + offset.toString() +
                ", isTrigger=" + isTrigger +
                ", lastPosition=" + lastPosition.toString() +
                ", item=" + item.toString() +
                '}';
    }
}
