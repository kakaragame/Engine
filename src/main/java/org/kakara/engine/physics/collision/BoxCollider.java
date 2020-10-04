package org.kakara.engine.physics.collision;

import org.kakara.engine.GameHandler;
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
public class BoxCollider implements Collider {

    private Vector3 point1;
    private Vector3 point2;
    private Vector3 offset;
    private boolean relative;
    private boolean isTrigger;
    private boolean resolveable;

    private Vector3 lastPosition;
    private Collidable item;
    private final GameHandler handler;
    private Predicate<Collidable> predicate = gameItem -> false;
    private final List<OnTriggerEnter> triggerEvents;

    /**
     * Create a box collider
     *
     * @param point1   The first point
     * @param point2   The second point
     * @param relative If the object is relative.
     */
    public BoxCollider(Vector3 point1, Vector3 point2, boolean relative) {
        this.handler = GameHandler.getInstance();
        this.point1 = point1;
        this.point2 = point2;
        this.relative = relative;
        this.offset = new Vector3(0, 0, 0);
        this.isTrigger = false;
        this.resolveable = true;
        this.triggerEvents = new ArrayList<>();
    }

    public BoxCollider(Vector3 point1, Vector3 point2) {
        this(point1, point2, true);
    }

    @Override
    public Collider setTrigger(boolean value) {
        this.isTrigger = value;
        return this;
    }

    @Override
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

    /**
     * If the collider is in realtive mode.
     *
     * @return If the collider is relative
     */
    public boolean isRelative() {
        return relative;
    }

    /**
     * Set if the points provided are relative or absolute.
     *
     * @param relative If the points provided are relative.
     */
    public void setRelative(boolean relative) {
        this.relative = relative;
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
        if (!relative)
            return point1.add(offset).subtract(item.getColPosition());
        return point1.add(offset);
    }

    @Override
    public Vector3 getAbsolutePoint1() {
        if (relative)
            return new Vector3(point1.x, point1.y, point1.z).addMut(offset).addMut(item.getColPosition());
        return new Vector3(point1.x, point1.y, point1.z).addMut(offset);
    }

    @Override
    public Vector3 getRelativePoint2() {
        if (!relative)
            return point2.add(offset).subtract(item.getColPosition());
        return point2.add(offset);
    }

    @Override
    public Vector3 getAbsolutePoint2() {
        if (relative)
            return new Vector3(point2.x, point2.y, point2.z).addMut(offset).addMut(item.getColPosition());
        return new Vector3(point2.x, point2.y, point2.z).addMut(offset);
    }

    @Override
    public void updateX() {
        if (isTrigger || !resolveable) return;
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
        this.lastPosition = item.getColPosition().clone();

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (Collidable gi : cm.getCollidngItems(item.getColPosition())) {
            if (gi == item) continue;
            if (gi.getCollider().isTrigger()) continue;
            if (getPredicate().test(gi)) continue;
            CollisionManager.Contact contact = cm.isCollidingXZ(gi.getCollider(), item.getCollider());
            while (contact.isIntersecting()) {
                contact = cm.isCollidingXZ(gi.getCollider(), item.getCollider());
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
    public void update() {
        if (isTrigger || !resolveable) return;

        CollisionManager cm = handler.getCurrentScene().getCollisionManager();
        assert cm != null;

        for (Collidable gi : cm.getCollidngItems(item.getColPosition())) {
            if (gi == item) continue;
            if (getPredicate().test(gi)) continue;
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
    }

}
