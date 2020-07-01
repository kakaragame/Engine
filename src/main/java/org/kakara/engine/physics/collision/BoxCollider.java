package org.kakara.engine.physics.collision;

import org.kakara.engine.GameHandler;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.utils.Time;

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

    private boolean isInAir = false;

    private Vector3 lastPosition;
    private Vector3 deltaPosition;
    private Collidable item;
    private GameHandler handler;

    /**
     * Create a box collider
     * @param point1 The first point
     * @param point2 The second point
     * @param relative If the object is relative.
     */
    public BoxCollider(Vector3 point1, Vector3 point2, boolean relative){
        this.handler = GameHandler.getInstance();
        this.point1 = point1;
        this.point2 = point2;
        this.relative = relative;
        this.offset = new Vector3(0, 0, 0);
       this.isTrigger = false;
    }

    public BoxCollider(Vector3 point1, Vector3 point2){
        this(point1, point2, true);
    }

    @Override
    public Collider setTrigger(boolean value){
        this.isTrigger = value;
        return this;
    }

    @Override
    public boolean isTrigger(){
        return isTrigger;
    }

    /**
     * If the collider is in realtive mode.
     * @return If the collider is relative
     */
    public boolean isRelative(){
        return relative;
    }

    /**
     * Set if the points provided are relative or absolute.
     * @param relative If the points provided are relative.
     */
    public void setRelative(boolean relative){
        this.relative = relative;
    }


    /**
     * Set the offset of the collider.
     * <p>Relative points will relate to (0,0,0) minus this vector.</p>
     * @param offset The offset vector
     */
    public void setOffset(Vector3 offset){
        this.offset = offset;
    }

    /**
     * Get the offset for this collider.
     * @return The offset.
     */
    public Vector3 getOffset(){
        return offset;
    }

    @Override
    public Vector3 getRelativePoint1(){
        if(!relative)
            return point1.add(offset).subtract(item.getColPosition());
        return point1.add(offset);
    }

    @Override
    public Vector3 getAbsolutePoint1(){
        if(relative)
            return point1.add(offset).add(item.getColPosition());
        return point1.add(offset);
    }

    @Override
    public Vector3 getRelativePoint2(){
        if(!relative)
            return point2.add(offset).subtract(item.getColPosition());
        return point2.add(offset);
    }

    @Override
    public Vector3 getAbsolutePoint2(){
        if(relative)
            return point2.add(offset).add(item.getColPosition());
        return point2.add(offset);
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
            CollisionManager.Contact contact = cm.isCollidingXZ(gi.getCollider(), item.getCollider());
            while (contact.isIntersecting()){
                contact = cm.isCollidingXZ(gi.getCollider(), item.getCollider());
                item.setColPosition(item.getColPosition().add(new Vector3(contact.getnEnter().mul(-1).mul(contact.getPenetration()))));
            }
        }
    }

    /**
     * Set point 1.
     * @param point1 The vector for the point.
     */
    public void setPoint1(Vector3 point1){
        this.point1 = point1;
    }

    /**
     * Get point 1 no matter if it is relative or absolute.
     * @return the vector.
     */
    public Vector3 getPoint1(){
        return point1;
    }

    /**
     * Get the second point no matter if it is relative or absolute.
     * @return the vector.
     */
    public Vector3 getPoint2(){
        return point2;
    }

    /**
     * Set the second point.
     * @param point2 The vector.
     */
    public void setPoint2(Vector3 point2){
        this.point2 = point2;
    }

    @Override
    public void update() {
        if(isTrigger) return;
        //Calculate delta position.
        this.deltaPosition = item.getColPosition().subtract(this.lastPosition);
        this.lastPosition = item.getColPosition();

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
