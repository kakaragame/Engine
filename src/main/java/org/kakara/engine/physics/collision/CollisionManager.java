package org.kakara.engine.physics.collision;

import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.scene.AbstractGameScene;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is used to handle colliding objects.
 * (This class prevents the calculation of collision for non-colliding game items.)
 */
public class CollisionManager {

    private final GameHandler handler;
    private final List<Collidable> collidingItems = new ArrayList<>();

    public CollisionManager(GameHandler handler) {
        this.handler = handler;
    }

    /**
     * Add an item to the colliding list.
     *
     * @param item The colliding item.
     */
    public void addCollidingItem(Collidable item) {
        collidingItems.add(item);
    }

    /**
     * Remove an item from the colliding list
     *
     * @param item The item to remove.
     */
    public void removeCollidingItem(Collidable item) {
        collidingItems.remove(item);
    }

    /**
     * Get all of the colliding items
     * <p>Returns items that are not render chunks</p>
     *
     * @return Get all colliding items.
     */
    public List<Collidable> getNonChunkCollidingItems() {
        return collidingItems;
    }

    /**
     * Get items for collision.
     * <p>This includes all render chunk blocks in a radius of 16 for performance reasons.</p>
     *
     * @param position The position of the current colliding object.
     * @return The list of collision objects.
     */
    public List<Collidable> getCollidngItems(@Nullable Vector3 position) {
        if (position == null) return collidingItems;
        List<Collidable> colliders = new ArrayList<>(collidingItems);
        if (handler.getSceneManager().getCurrentScene() instanceof AbstractGameScene)
            colliders.addAll(((AbstractGameScene) handler.getSceneManager().getCurrentScene()).getChunkHandler().getChunkCollisions(position));
        return colliders;
    }

    /**
     * Get the list of items for selection.
     *
     * @param position The position of the current object (Normally the player).
     * @return The valid collidables.
     */
    public List<Collidable> getSelectionItems(Vector3 position) {
        if (position == null) return collidingItems;
        List<Collidable> colliders = new ArrayList<>(collidingItems);
        if (handler.getSceneManager().getCurrentScene() instanceof AbstractGameScene)
            colliders.addAll(((AbstractGameScene) handler.getSceneManager().getCurrentScene()).getChunkHandler().getChunkSelections(position));
        return colliders;
    }

    /*
           Thank you user1423893 for you assistance.
           https://gamedev.stackexchange.com/questions/32807/collision-resolve-sphere-aabb-and-aabb-aabb
     */

    /**
     * Check to see if two colliders are colliding.
     *
     * @param c1 The first collider
     * @param c2 The second collider.
     * @return The contact class.
     */
    public Contact isCollidingXZ(Collider c1, Collider c2) {
        FloatContainer mtvDistance = new FloatContainer(Float.MAX_VALUE);
        Vector3f mtvAxis = new Vector3f();
        Contact contact = new Contact();
        if (!testAxis(new Vector3f(1, 0, 0), c1.getAbsolutePoint1().x, c1.getAbsolutePoint2().x, c2.getAbsolutePoint1().x, c2.getAbsolutePoint2().x, mtvAxis, mtvDistance))
            return contact;

        if (!testAxis(new Vector3f(0, 1, 0), c1.getAbsolutePoint1().y, c1.getAbsolutePoint2().y, c2.getAbsolutePoint1().y, c2.getAbsolutePoint2().y, new Vector3f(), new FloatContainer(Float.MAX_VALUE)))
            return contact;

        if (!testAxis(new Vector3f(0, 0, 1), c1.getAbsolutePoint1().z, c1.getAbsolutePoint2().z, c2.getAbsolutePoint1().z, c2.getAbsolutePoint2().z, mtvAxis, mtvDistance))
            return contact;

        contact.setIntersecting(true);
        contact.setnEnter(mtvAxis.normalize());
        contact.setPenetration((float) Math.sqrt(mtvDistance.getFloat()) * 1.001f);

        return contact;
    }

    public Contact isColliding(Collider c1, Collider c2) {
        FloatContainer mtvDistance = new FloatContainer(Float.MAX_VALUE);
        Vector3f mtvAxis = new Vector3f();
        Contact contact = new Contact();
        if (!testAxis(new Vector3f(1, 0, 0), c1.getAbsolutePoint1().x, c1.getAbsolutePoint2().x, c2.getAbsolutePoint1().x, c2.getAbsolutePoint2().x, mtvAxis, mtvDistance))
            return contact;

        if (!testAxis(new Vector3f(0, 1, 0), c1.getAbsolutePoint1().y, c1.getAbsolutePoint2().y, c2.getAbsolutePoint1().y, c2.getAbsolutePoint2().y, mtvAxis, mtvDistance))
            return contact;

        if (!testAxis(new Vector3f(0, 0, 1), c1.getAbsolutePoint1().z, c1.getAbsolutePoint2().z, c2.getAbsolutePoint1().z, c2.getAbsolutePoint2().z, mtvAxis, mtvDistance))
            return contact;

        contact.setIntersecting(true);
        contact.setnEnter(mtvAxis.normalize());
        contact.setPenetration((float) Math.sqrt(mtvDistance.getFloat()) * 1.001f);

        return contact;
    }

    public Contact isCollidingY(Collider c1, Collider c2) {
        FloatContainer mtvDistance = new FloatContainer(Float.MAX_VALUE);
        Vector3f mtvAxis = new Vector3f();
        Contact contact = new Contact();

        if (!testAxis(new Vector3f(1, 0, 0), c1.getAbsolutePoint1().x, c1.getAbsolutePoint2().x, c2.getAbsolutePoint1().x, c2.getAbsolutePoint2().x, new Vector3f(), new FloatContainer(Float.MAX_VALUE)))
            return contact;

        if (!testAxis(new Vector3f(0, 1, 0), c1.getAbsolutePoint1().y, c1.getAbsolutePoint2().y, c2.getAbsolutePoint1().y, c2.getAbsolutePoint2().y, mtvAxis, mtvDistance))
            return contact;

        if (!testAxis(new Vector3f(0, 0, 1), c1.getAbsolutePoint1().z, c1.getAbsolutePoint2().z, c2.getAbsolutePoint1().z, c2.getAbsolutePoint2().z, new Vector3f(), new FloatContainer(Float.MAX_VALUE)))
            return contact;

        contact.setIntersecting(true);
        contact.setnEnter(mtvAxis.normalize());
        contact.setPenetration((float) Math.sqrt(mtvDistance.getFloat()) * 1.001f);

        return contact;
    }

    public Contact isCollidingX(Collider c1, Collider c2) {
        FloatContainer mtvDistance = new FloatContainer(Float.MAX_VALUE);
        Vector3f mtvAxis = new Vector3f();
        Contact contact = new Contact();

        if (!testAxis(new Vector3f(0, 1, 0), c1.getAbsolutePoint1().x, c1.getAbsolutePoint2().x, c2.getAbsolutePoint1().x, c2.getAbsolutePoint2().x, new Vector3f(), new FloatContainer(Float.MAX_VALUE)))
            return contact;

        if (!testAxis(new Vector3f(1, 0, 0), c1.getAbsolutePoint1().y, c1.getAbsolutePoint2().y, c2.getAbsolutePoint1().y, c2.getAbsolutePoint2().y, mtvAxis, mtvDistance))
            return contact;

        if (!testAxis(new Vector3f(0, 0, 1), c1.getAbsolutePoint1().z, c1.getAbsolutePoint2().z, c2.getAbsolutePoint1().z, c2.getAbsolutePoint2().z, new Vector3f(), new FloatContainer(Float.MAX_VALUE)))
            return contact;

        contact.setIntersecting(true);
        contact.setnEnter(mtvAxis.normalize());
        contact.setPenetration((float) Math.sqrt(mtvDistance.getFloat()) * 1.001f);

        return contact;
    }

    public Contact isCollidingZ(Collider c1, Collider c2) {
        FloatContainer mtvDistance = new FloatContainer(Float.MAX_VALUE);
        Vector3f mtvAxis = new Vector3f();
        Contact contact = new Contact();

        if (!testAxis(new Vector3f(1, 0, 0), c1.getAbsolutePoint1().x, c1.getAbsolutePoint2().x, c2.getAbsolutePoint1().x, c2.getAbsolutePoint2().x, new Vector3f(), new FloatContainer(Float.MAX_VALUE)))
            return contact;

        if (!testAxis(new Vector3f(0, 0, 1), c1.getAbsolutePoint1().y, c1.getAbsolutePoint2().y, c2.getAbsolutePoint1().y, c2.getAbsolutePoint2().y, mtvAxis, mtvDistance))
            return contact;

        if (!testAxis(new Vector3f(0, 1, 0), c1.getAbsolutePoint1().z, c1.getAbsolutePoint2().z, c2.getAbsolutePoint1().z, c2.getAbsolutePoint2().z, new Vector3f(), new FloatContainer(Float.MAX_VALUE)))
            return contact;

        contact.setIntersecting(true);
        contact.setnEnter(mtvAxis.normalize());
        contact.setPenetration((float) Math.sqrt(mtvDistance.getFloat()) * 1.001f);

        return contact;
    }

    /**
     * Check to see if an axis is colliding.
     *
     * @param axis        The axis to check on.
     * @param minA        The minimum point for collider A
     * @param maxA        The maximum point for collider A
     * @param minB        The minimum point for collider B
     * @param maxB        The maximum point for collider B
     * @param mtvAxis     The Minimum Translation Vector axis. (The axis to travel the minimum distance.
     *                    <p>The mtvAxis is mutated by this method.</p>
     * @param mtvDistance The Minimum Translation Vector distance. (The distance to travel)
     *                    <p>The mtvDistance is mutated by this method.</p>
     * @return If the axis is colliding.
     */
    private boolean testAxis(Vector3f axis, float minA, float maxA, float minB, float maxB, Vector3f mtvAxis, FloatContainer mtvDistance) {
        float axisLengthSq = new Vector3f(axis).dot(new Vector3f(axis));
        if (axisLengthSq < 1.0e-8f) {
            return true;
        }

        float d0 = (maxB - minA);
        float d1 = (maxA - minB);
        if (d0 <= 0.0f || d1 <= 0.0f)
            return false;
        float overlap = (d0 < d1) ? d0 : -d1;
        Vector3f sep = new Vector3f(axis).mul(overlap / axisLengthSq);

        float sepLengthSquared = new Vector3f(sep).dot(sep);

        if (sepLengthSquared < mtvDistance.getFloat()) {
            mtvDistance.setFloat(sepLengthSquared);
            mtvAxis.set(sep);
        }
        return true;
    }

    /**
     * Holds the contact information of a collision.
     */
    protected static class Contact {
        private boolean isIntersecting;
        private Vector3f nEnter;
        private float penetration;

        public Contact() {
            isIntersecting = false;
            nEnter = new Vector3f();
            penetration = 0;
        }

        /**
         * If there was a collision.
         *
         * @return If there was a collision.
         */
        public boolean isIntersecting() {
            return isIntersecting;
        }

        public void setIntersecting(boolean value) {
            this.isIntersecting = value;
        }

        /**
         * The normal vector.
         *
         * @return The normal vector.
         */
        public Vector3f getnEnter() {
            return this.nEnter;
        }

        public void setnEnter(Vector3f nEnter) {
            this.nEnter = nEnter;
        }

        /**
         * The distance into object.
         *
         * @return The distance into the object.
         */
        public float getPenetration() {
            return penetration;
        }

        public void setPenetration(float penetration) {
            this.penetration = penetration;
        }
    }

    /**
     * Used to contain a float value that can be mutated.
     */
    private static class FloatContainer {
        private float f;

        public FloatContainer(float f) {
            this.f = f;
        }

        public float getFloat() {
            return f;
        }

        public void setFloat(float f) {
            this.f = f;
        }
    }
}

