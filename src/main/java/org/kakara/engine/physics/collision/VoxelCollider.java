package org.kakara.engine.physics.collision;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.physics.OnTriggerEnter;
import org.kakara.engine.voxels.Voxel;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * This is used to handle the Voxel collisions.
 * <p>This component has special behavior and is not attached to a GameItem.</p>
 * <p>{@link #getGameItem()} is null for this component. Please check that a {@link ColliderComponent}
 * is not an instance of this class before using that method.</p>
 */
public class VoxelCollider extends ColliderComponent {

    private Vector3 point1;
    private Vector3 point2;
    private Vector3 offset;
    private boolean isTrigger;

    private final Voxel voxel;
    private final GameHandler handler;
    private Predicate<ColliderComponent> predicate = gameItem -> false;
    private final List<OnTriggerEnter> triggerEvents;
    private final Vector3 scale = new Vector3(1, 1, 1);

    /**
     * Construct the render block collider.
     *
     * @param renderBlock The render block that this collider uses.
     */
    public VoxelCollider(Voxel renderBlock) {
        this.handler = GameHandler.getInstance();
        this.triggerEvents = new ArrayList<>();
        this.voxel = renderBlock;
    }

    @Override
    public void start() {
        this.point1 = new Vector3();
        this.point2 = new Vector3(1, 1, 1);
        this.offset = new Vector3();
        this.isTrigger = false;
    }

    @Override
    public void update() {
    }

    @Override
    public void afterInit() {
    }

    @Override
    public void onRemove() {
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
        return new Vector3(point1.x, point1.y, point1.z).addMut(offset).addMut(voxel.getWorldPosition());
    }

    @Override
    public Vector3 getRelativePoint2() {
        return point2.add(offset);
    }

    @Override
    public Vector3 getAbsolutePoint2() {
        return new Vector3(point2.x, point2.y, point2.z).addMut(offset).addMut(voxel.getWorldPosition());
    }

    @Override
    public void updateX() {

    }

    @Override
    public void updateY() {

    }

    @Override
    public void updateZ() {

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

    @Override
    public Vector3 getPosition() {
        return voxel.getWorldPosition();
    }

    @Override
    public Vector3 getScale() {
        return scale;
    }

    public Voxel getVoxel() {
        return voxel;
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

        for (ColliderComponent gi : cm.getCollidngItems(voxel.getWorldPosition())) {
            if (gi == this) continue;
            if (getPredicate().test(gi)) continue;
            if (cm.isColliding(gi, this).isIntersecting()) {
                // Fire the trigger event.
                for (OnTriggerEnter evt : triggerEvents) {
                    evt.onTriggerEnter(gi);
                }
            }
        }
    }

}
