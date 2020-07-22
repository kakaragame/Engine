package org.kakara.engine.physics;

import org.kakara.engine.item.GameItem;
import org.kakara.engine.item.MeshGameItem;
import org.kakara.engine.physics.collision.Collider;
import org.kakara.engine.scene.Scene;

import java.util.Objects;
import java.util.TimerTask;

/**
 * This is the fixed physics updater.
 * Its job is to handle physics calculations every 20 ms no matter what.
 * This is completely independent of frame rate.
 * To use this system you must use the velocity system.
 *
 * @since 1.0-Pre2
 */
public class FixedPhysicsUpdater extends TimerTask {
    private Scene scene;
    private long oldTime;
    private long currentTime;

    public FixedPhysicsUpdater(Scene currentScene) {
        this.scene = currentScene;
        this.oldTime = System.currentTimeMillis();
    }

    @Override
    public void run() {
        oldTime = currentTime;
        currentTime = System.currentTimeMillis();
        float deltaTime = ((float) (currentTime - oldTime)) * 0.001f;
        for (GameItem item : Objects.requireNonNull(scene.getItemHandler()).getItems()) {
            try {
                if (!(item instanceof MeshGameItem)) continue;
                MeshGameItem meshItem = (MeshGameItem) item;
                if (meshItem.getCollider() == null) continue;
                Collider collider = meshItem.getCollider();
                meshItem.setVelocity(meshItem.getVelocity().add(meshItem.getAcceleration().getX() * deltaTime, meshItem.getAcceleration().getY() * deltaTime, meshItem.getAcceleration().getZ() * deltaTime));
                meshItem.translateBy(meshItem.getVelocity().getX() * deltaTime, 0, 0);
                if (collider.getPredicate().test(item)) {
                    continue;
                }
                // Handles Triggers
                collider.update();

                collider.updateZ();
                meshItem.translateBy(0, 0, meshItem.getVelocity().getZ() * deltaTime);
                collider.updateZ();
                meshItem.translateBy(0, meshItem.getVelocity().getY() * deltaTime, 0);
                collider.updateY();
            } catch (NullPointerException ex) {
                // Ignore.
                // This happens when it is doing calculations and a block is removed.
                // The next update should work just fine.
            }
            //...
        }
    }
}
