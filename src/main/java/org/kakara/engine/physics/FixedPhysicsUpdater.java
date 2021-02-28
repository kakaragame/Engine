package org.kakara.engine.physics;

import org.kakara.engine.components.Component;
import org.kakara.engine.gameitems.GameItem;
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
    private final Scene scene;
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
        // TODO come up with better solution. Maybe change getItems to copyonwritearray
        synchronized (Objects.requireNonNull(scene.getItemHandler()).getItems()) {
            for (GameItem item : Objects.requireNonNull(scene.getItemHandler()).getItems()) {
                try {
                    for (Component components : item.getComponents()) {
                        components.physicsUpdate(deltaTime);
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
//                try {
//                    GameItem meshItem = (GameItem) item;
//                    if (meshItem.getCollider() == null) continue;
//                    ColliderComponent collider = meshItem.getCollider();
//                    // Add by mutation to keep object creation down.
//                    meshItem.getVelocity().addMut(meshItem.getAcceleration().getX() * deltaTime, meshItem.getAcceleration().getY() * deltaTime, meshItem.getAcceleration().getZ() * deltaTime);
//                    meshItem.translateBy(meshItem.getVelocity().getX() * deltaTime, 0, 0);
//
//                    // Handles Triggers
//                    collider.update();
//
//                    collider.updateZ();
//                    meshItem.translateBy(0, 0, meshItem.getVelocity().getZ() * deltaTime);
//                    collider.updateZ();
//                    meshItem.translateBy(0, meshItem.getVelocity().getY() * deltaTime, 0);
//                    collider.updateY();
//                } catch (NullPointerException ex) {
//                    // Ignore.
//                    // This happens when it is doing calculations and a block is removed.
//                    // The next update should work just fine.
//                }
                //...
            }
        }
    }
}
