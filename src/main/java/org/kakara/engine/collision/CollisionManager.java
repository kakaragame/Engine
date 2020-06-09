package org.kakara.engine.collision;

import org.jetbrains.annotations.Nullable;
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

    private GameHandler handler;
    private List<Collidable> collidingItems = new ArrayList<>();

    public CollisionManager(GameHandler handler){
        this.handler = handler;
    }

    /**
     * Add an item to the colliding list.
     * @param item The colliding item.
     */
    public void addCollidingItem(Collidable item){
        collidingItems.add(item);
    }

    /**
     * Remove an item from the colliding list
     * @param item The item to remove.
     */
    public void removeCollidingItem(Collidable item){
        collidingItems.remove(item);
    }

    /**
     * Get all of the colliding items
     * <p>Returns items that are not render chunks</p>
     * @return Get all colliding items.
     */
    public List<Collidable> getNonChunkCollidingItems(){
        return collidingItems;
    }

    /**
     * Get items for collision.
     * <p>This includes all render chunk blocks in a radius of 16 for performance reasons.</p>
     * @param position The position of the current colliding object.
     * @return The list of collision objects.
     */
    public List<Collidable> getCollidngItems(@Nullable Vector3 position){
        if(position == null) return collidingItems;
        List<Collidable> colliders = new ArrayList<>(collidingItems);
        if(handler.getSceneManager().getCurrentScene() instanceof  AbstractGameScene)
            colliders.addAll(((AbstractGameScene) handler.getSceneManager().getCurrentScene()).getChunkHandler().getChunkCollisions(position));
        return colliders;
    }

    /**
     * Get the list of items for selection.
     * @param position The position of the current object (Normally the player).
     * @return The valid collidables.
     */
    public List<Collidable> getSelectionItems(Vector3 position){
        if(position == null) return collidingItems;
        List<Collidable> colliders = new ArrayList<>(collidingItems);
        if(handler.getSceneManager().getCurrentScene() instanceof  AbstractGameScene)
            colliders.addAll(((AbstractGameScene) handler.getSceneManager().getCurrentScene()).getChunkHandler().getChunkSelections(position));
        return colliders;
    }

    /**
     * Detect collision between two items.
     * @param item The first game item.
     * @param other The second game item.
     * @return If they are colliding.
     */
    public boolean isColliding(Collidable item, Collidable other){
        boolean xCollision;
        boolean yCollision;
        boolean zCollision;
        if(item.getCollider() instanceof BoxCollider && other.getCollider() instanceof ObjectBoxCollider){
            xCollision = (item.getColPosition().x + item.getCollider().getRelativePoint1().x) + (((BoxCollider) item.getCollider()).getRelativePoint2().x) >= other.getColPosition().x && other.getColPosition().x + other.getColScale()
                    >= item.getColPosition().x + ((BoxCollider) item.getCollider()).getRelativePoint1().x;
            yCollision = (item.getColPosition().y + ((BoxCollider) item.getCollider()).getRelativePoint1().y) + (((BoxCollider) item.getCollider()).getRelativePoint2().y) >= other.getColPosition().y && other.getColPosition().y + other.getColScale()
                    >= item.getColPosition().y + ((BoxCollider) item.getCollider()).getRelativePoint1().y;
            zCollision = (item.getColPosition().z + ((BoxCollider) item.getCollider()).getRelativePoint1().z) + (((BoxCollider) item.getCollider()).getRelativePoint2().z) >= other.getColPosition().z && other.getColPosition().z + other.getColScale()
                    >= item.getColPosition().z + ((BoxCollider) item.getCollider()).getRelativePoint1().z;
        }
        else if(item.getCollider() instanceof BoxCollider && other.getCollider() instanceof BoxCollider){
            xCollision = (item.getColPosition().x + ((BoxCollider) item.getCollider()).getRelativePoint1().x) + (((BoxCollider) item.getCollider()).getRelativePoint2().x) >= (other.getColPosition().x + ((BoxCollider) other.getCollider()).getRelativePoint1().x)
                    && (other.getColPosition().x + other.getCollider().getRelativePoint1().x) + (((BoxCollider) other.getCollider()).getRelativePoint2().x)
                    >= item.getColPosition().x + item.getCollider().getRelativePoint1().x;
            yCollision = (item.getColPosition().y + ((BoxCollider) item.getCollider()).getRelativePoint1().y) + (((BoxCollider) item.getCollider()).getRelativePoint2().y) >= (other.getColPosition().y + ((BoxCollider) other.getCollider()).getRelativePoint1().y)
                    && (other.getColPosition().y + ((BoxCollider) other.getCollider()).getRelativePoint1().y) + ( ((BoxCollider) other.getCollider()).getRelativePoint2().y)
                    >= item.getColPosition().y + item.getCollider().getRelativePoint1().y;
            zCollision = (item.getColPosition().z + ((BoxCollider) item.getCollider()).getRelativePoint1().z) + (((BoxCollider) item.getCollider()).getRelativePoint2().z) >= (other.getColPosition().z + ((BoxCollider) other.getCollider()).getRelativePoint1().z)
                    && (other.getColPosition().z + other.getCollider().getRelativePoint1().z) + (((BoxCollider) other.getCollider()).getRelativePoint2().z)
                    >= item.getColPosition().z + item.getCollider().getRelativePoint1().z;
        }
        else if(item.getCollider() instanceof  ObjectBoxCollider && other.getCollider() instanceof ObjectBoxCollider){
            xCollision = item.getColPosition().x + item.getColScale() >= other.getColPosition().x && other.getColPosition().x + other.getColScale() >= item.getColPosition().x;
            yCollision = item.getColPosition().y + item.getColScale() >= other.getColPosition().y && other.getColPosition().y + other.getColScale() >= item.getColPosition().y;
            zCollision = item.getColPosition().z + item.getColScale() >= other.getColPosition().z && other.getColPosition().z + other.getColScale() >= item.getColPosition().z;
        }
        else if(item.getCollider() instanceof ObjectBoxCollider && other.getCollider() instanceof BoxCollider){
            Collidable itemCopy = other;
            Collidable otherCopy = item;
            xCollision = (itemCopy.getColPosition().x + ((BoxCollider) itemCopy.getCollider()).getRelativePoint1().x) + (((BoxCollider) itemCopy.getCollider()).getRelativePoint2().x) >= otherCopy.getColPosition().x && otherCopy.getColPosition().x + otherCopy.getColScale()
                    >= itemCopy.getColPosition().x + ((BoxCollider) itemCopy.getCollider()).getRelativePoint1().x;
            yCollision = (itemCopy.getColPosition().y + ((BoxCollider) itemCopy.getCollider()).getRelativePoint1().y) + (((BoxCollider) itemCopy.getCollider()).getRelativePoint2().y) >= otherCopy.getColPosition().y && otherCopy.getColPosition().y + otherCopy.getColScale()
                    >= itemCopy.getColPosition().y + ((BoxCollider) itemCopy.getCollider()).getRelativePoint1().y;
            zCollision = (itemCopy.getColPosition().z + ((BoxCollider) itemCopy.getCollider()).getRelativePoint1().z) + (((BoxCollider) itemCopy.getCollider()).getRelativePoint2().z) >= otherCopy.getColPosition().z && otherCopy.getColPosition().z + otherCopy.getColScale()
                    >= itemCopy.getColPosition().z + ((BoxCollider) itemCopy.getCollider()).getRelativePoint1().z;
        }
        else{
            return false;
        }
        return xCollision && yCollision && zCollision;
    }
}
