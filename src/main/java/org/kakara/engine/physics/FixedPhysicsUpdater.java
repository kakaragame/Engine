package org.kakara.engine.physics;

import org.kakara.engine.item.GameItem;
import org.kakara.engine.item.MeshGameItem;
import org.kakara.engine.math.Vector3;
import org.kakara.engine.physics.collision.Collidable;
import org.kakara.engine.physics.collision.Collider;
import org.kakara.engine.scene.Scene;

import java.util.Objects;
import java.util.TimerTask;

/**
 * This is the fixed physics updater.
 * Its job is to handle physics calculations every 20 ms no matter what.
 * This is completely independent of frame rate.
 * To use this system you must use the velocity system.
 */
public class FixedPhysicsUpdater extends TimerTask {
    private Scene scene;
    public FixedPhysicsUpdater(Scene currentScene){
        this.scene = currentScene;
    }

    @Override
    public void run() {
        for(GameItem item : Objects.requireNonNull(scene.getItemHandler()).getItems()){
            if(!(item instanceof MeshGameItem)) continue;
            MeshGameItem meshItem = (MeshGameItem) item;
            if(meshItem.getCollider() == null) continue;
            Collider collider = meshItem.getCollider();

            System.out.println(meshItem.getPosition());

            meshItem.setVelocity(meshItem.getVelocity().add(meshItem.getAcceleration().getX() * 0.020f, meshItem.getAcceleration().getY() * 0.020f, meshItem.getAcceleration().getZ() * 0.20f));
            meshItem.translateBy(meshItem.getVelocity().getX() * 0.020f, 0, 0);
            collider.updateZ();
            meshItem.translateBy(0, 0, meshItem.getVelocity().getZ() * 0.020f);
            collider.updateZ();
            meshItem.translateBy(0, meshItem.getVelocity().getY() * 0.020f, 0);
            collider.updateY();
//            meshItem.setPosition(meshItem.getPosition().add(meshItem.getVelocity().getX() * 0.020f, meshItem.getVelocity().getY() * 0.020f, meshItem.getVelocity().getZ() * 0.020f));
            //...
        }
    }
}
